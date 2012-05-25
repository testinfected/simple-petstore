# Disable logging in Jetty and Selenium Server before requiring jetty addon.
# Find a way to make this configurable
Java.classpath << [:slf4j_api, :jcl_over_slf4j, :slf4j_silent]

require 'buildr/java/cobertura'
require 'buildr/jetty'

VERSION_NUMBER = '0.9-SNAPSHOT'

HAMCREST = [:hamcrest_core, :hamcrest_library, :hamcrest_extra]
LOG = [:slf4j_api, :slf4j_log4j, :slf4j_jcl, :log4j]
NO_LOG = [:slf4j_api, :slf4j_silent]
VELOCITY = [:commons_beanutils, :commons_digester, :commons_chain, :velocity_engine, :velocity_tools]
JETTY = [:jetty, :jetty_util]

SELENIUM_SERVER = artifact("org.seleniumhq:selenium-server-standalone:jar:2.21.0")
download( SELENIUM_SERVER => "http://selenium.googlecode.com/files/selenium-server-standalone-2.21.0.jar" )
Java.classpath << SELENIUM_SERVER

Project.local_task :jetty

define 'petstore', :group => 'com.pyxis.simple-petstore', :version => VERSION_NUMBER do
  compile.options.target = '1.6'

  define 'domain' do
    compile.with_transitive :commons_lang, :hibernate_annotations, :hibernate_validator, :spring_context
    
    test.with HAMCREST, :hamcrest_validation, NO_LOG
    
    package :jar
  end
  
  define 'infrastructure' do
    resources.no_filtering
    compile.with project(:domain)
    compile.with_transitive :hibernate_annotations, :hibernate_validator, :spring_context, :spring_tx
    
    test.resources.filter.using 'migrations.dir' => _(:src, :main, :scripts, :migrations), 'test.log.dir' => _(:target, :logs)
    test.with project(:domain).test.compile.target, HAMCREST, LOG
    test.with_transitive :hamcrest_jpa, :carbon_5, :commons_dbcp, :spring_orm, :javassist, :mysql

    package :jar
  end
  
  define 'webapp' do
    resources.filter.using 'log.dir' => _(:target, :logs)
    
    compile.with project(:domain), project(:domain).compile.dependencies, project(:infrastructure), project(:infrastructure).compile.dependencies, VELOCITY
    compile.with_transitive :spring_web, :spring_mvc, :servlet_api
    
    test.setup { makedirs _(:target, :logs) }
    test.resources.filter.using 'webapp.dir' => _(:src, :main, :webapp), 'test.log.dir' => _(:target, :logs)
    test.with project(:domain).test.compile.target, HAMCREST 
    test.with_transitive :hamcrest_dom, :hamcrest_spring, :neko_html, :commons_lang, :spring_support, :spring_test
    
    package(:war).provided_dependencies :servlet_api
    package(:war).runtime_dependencies LOG, :commons_pool, :commons_dbcp, :javassist, :asm, :cglib, :spring_orm, :spring_jdbc, :sitemesh, :url_rewrite, :mysql
    
    task :jetty => package(:war) do |task|
      jetty.url = "http://localhost:#{Buildr.settings.profile['server.port']}"
      jetty.use.invoke
      jetty.deploy("#{jetty.url}#{Buildr.settings.profile['filter']['context.path']}", task.prerequisites.first)
      puts 'Press CTRL-C to stop Jetty'
      trap 'SIGINT' do
        jetty.stop
      end
      Thread.stop
    end
  end
  
  define 'system-tests' do
    test.resources.filter.using 'webapp.dir' => project(:webapp).path_to(:src, :main, :webapp), 
                                'migrations.dir' => project(:infrastructure).path_to(:src, :main, :scripts, :migrations),
                                'test.log.dir' => _(:target, :logs)
    test.with project(:webapp).compile.target, project(:webapp).resources.target, project(:webapp).package(:war).libs, 
              project(:domain).test.compile.target, project(:infrastructure).test.compile.target, HAMCREST, LOG
    test.with_transitive :selenium_firefox_driver, :windowlicker_web, :jetty, :simpleframework, :carbon_5

    test.using :integration, :properties => { 
      'server.lifecycle' => 'external',
      'browser.lifecycle' => 'remote',
      'browser.remote.url' => Buildr.settings.profile['filter']['selenium.server.url'],
      'browser.remote.capability.browserName' => Buildr.settings.profile['filter']['selenium.server.browser'],
      'browser.remote.capability.version' => Buildr.settings.profile['filter']['selenium.server.version'],
      'browser.remote.capability.name' => 'PetStore System Tests'
    }
    integration project(:webapp).package(:war)
    integration.setup do
      #Rake::Task['petstore:system-tests:start-selenium'].invoke
      start_selenium
      jetty.url = "http://localhost:#{Buildr.settings.profile['filter']['test.server.port']}"
      jetty.with :properties => {
        'jdbc.url' => Buildr.settings.profile['filter']['test.jdbc.url'],
        'jdbc.username' => Buildr.settings.profile['filter']['test.jdbc.username'],
        'jdbc.password' => Buildr.settings.profile['filter']['test.jdbc.password']
      }
      jetty.use.invoke
      jetty.deploy("#{jetty.url}#{Buildr.settings.profile['filter']['context.path']}", project(:webapp).package(:war))
    end
    
    integration.teardown do
      jetty.stop
      stop_selenium
    end
  end
end
