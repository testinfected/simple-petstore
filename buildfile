require 'buildr/java/cobertura'
require 'buildr/jetty'

VERSION_NUMBER = '0.1-SNAPSHOT'

HAMCREST = [:hamcrest_core, :hamcrest_library, :hamcrest_extra]
LOG = [:jcl_over_slf4j, :slf4j_api, :slf4j_log4j, :log4j]
NO_LOG = [:jcl_over_slf4j, :slf4j_api, :slf4j_silent]
VELOCITY = [:commons_beanutils, :commons_digester, :commons_chain, :velocity_engine, :velocity_tools]
JETTY = [:jetty, :jetty_util]

MUSTACHE = [:jsr305, :guava, :mustache]

Project.local_task :jetty
['db-migrate', 'db-clean', 'db-reset', 'db-init'].each { |t| Project.local_task t }

define 'petstore', :group => 'org.testinfected.petstore', :version => VERSION_NUMBER do
  compile.options.target = '1.6'
  
  define 'domain' do
    compile.with_transitive :commons_lang, :hibernate_annotations, :hibernate_validator, :spring_context
    test.with HAMCREST, :hamcrest_validation, NO_LOG
    package :jar
  end

  define 'persistence' do
    compile.with_transitive project(:domain), project(:domain).compile.dependencies

    test.with project(:domain).test.compile.target, HAMCREST, :flyway, NO_LOG
    test.with_transitive :mysql

    package(:jar)
  end

  define 'oldinfra' do
    resources.filter.deactivate
    compile.with project(:domain)
    compile.with_transitive :hibernate_annotations, :hibernate_validator, :spring_context, :spring_tx
    
    test.resources.filter.using 'test.log.dir' => _(:target, :logs)
    test.with project(:domain).test.compile.target, project(:persistence).test.compile.target, :flyway, HAMCREST, LOG
    test.with_transitive :hamcrest_jpa, :commons_dbcp, :spring_orm, :javassist, :mysql

    package :jar
  end
  
  define 'oldapp' do
    resources.filter.using 'log.dir' => _(:target, :logs)
    
    compile.with project(:domain), project(:domain).compile.dependencies, project(:oldinfra), project(:oldinfra).compile.dependencies, VELOCITY
    compile.with_transitive :spring_web, :spring_mvc, :servlet_api
    
    test.setup { makedirs _(:target, :logs) }
    test.resources.filter.using 'webapp.dir' => _(:src, :main, :webapp), 'test.log.dir' => _(:target, :logs)
    test.with project(:domain).test.compile.target, HAMCREST 
    test.with_transitive :hamcrest_dom, :hamcrest_spring, :nekohtml, :commons_lang, :spring_support, :spring_test

    package :war
    package(:war).exclude :servlet_api
    package(:war).add LOG, :commons_pool, :commons_dbcp, :javassist, :asm, :cglib, :spring_orm, :spring_jdbc, :sitemesh, :url_rewrite, :mysql
    
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

  define 'webapp' do
    compile.with :simpleframework, MUSTACHE, :time
    compile.with_transitive project(:domain), project(:persistence), project(:persistence).compile.dependencies

    test.with project(:domain).test.compile.target, project(:persistence).test.compile.target,
      project(:persistence).test.resources.target, HAMCREST, :antlr_runtime, :cssselectors, :hamcrest_dom
    test.with_transitive :nekohtml, :htmlunit, :juniversalchardet, :jmock_legacy, :mysql
    test.using :properties => { 'web.root' => _(:src, :main, :webapp) }

    package :jar
  end
  
  define 'main' do
    compile.with project(:webapp).package, project(:webapp).compile.dependencies, :flyway, :jcl_over_slf4j

    test.resources.filter.using 'webapp.dir' => project(:oldapp).path_to(:src, :main, :webapp),
                                'test.log.dir' => _(:target, :logs)
    test.with project(:oldapp).compile.target, project(:oldapp).resources.target, project(:oldapp).package(:war).libs, 
              project(:domain).test.compile.target, project(:oldinfra).test.compile.target,
              project(:webapp).test.compile.target, project(:persistence).test.compile.target,
              :flyway, HAMCREST, LOG
    test.with_transitive :selenium_firefox_driver, :windowlicker_web, :jetty

    test.using :integration, :properties => { 
      'web.root' => project(:webapp).path_to(:src, :main, :webapp),
      'server.lifecycle' => 'external',
      'browser.lifecycle' => 'remote',
      'browser.remote.url' => Buildr.settings.profile['filter']['selenium.server.url'],
      'browser.remote.capability.browserName' => Buildr.settings.profile['filter']['selenium.server.browser'],
      'browser.remote.capability.version' => Buildr.settings.profile['filter']['selenium.server.version'],
      'browser.remote.capability.name' => 'PetStore System Tests'
    }

    integration(project(:oldapp).package(:war)).setup do
      selenium.run
      jetty.url = "http://localhost:#{Buildr.settings.profile['filter']['test.server.port']}"
      jetty.with :properties => {
        'jdbc.url' => Buildr.settings.profile['filter']['test.jdbc.url'],
        'jdbc.username' => Buildr.settings.profile['filter']['test.jdbc.username'],
        'jdbc.password' => Buildr.settings.profile['filter']['test.jdbc.password']
      }
      jetty.use.invoke
      jetty.deploy("#{jetty.url}#{Buildr.settings.profile['filter']['context.path']}", project(:oldapp).package(:war))
    end
    
    integration.teardown do
      selenium.stop
    end

    def run_migrations(action)
      Java::Commands.java("org.testinfected.petstore.Migrations", action.to_s,
        Buildr.settings.profile['filter']['jdbc.url'],
        Buildr.settings.profile['filter']['jdbc.username'],
        Buildr.settings.profile['filter']['jdbc.password'],
        :classpath => [project.compile.target] + [:slf4j_simple, :mysql] + project.compile.dependencies) do
          exit
        end
    end

    task 'db-migrate' => :compile do run_migrations :migrate; end
    task 'db-clean' => :compile do run_migrations :clean; end
    task 'db-reset' => :compile do run_migrations :reset; end
    task 'db-init' => :compile do run_migrations :init; end
  end

  task :run => project(:main) do
    cp = [project(:main).compile.target] + project(:main).compile.dependencies + [:mysql]
    Java::Commands.java("org.testinfected.petstore.Launcher", Buildr.settings.profile['server.port'], project(:webapp).path_to(:src, :main, :webapp), :classpath => cp) { exit }
  end

  task 'db-migrate' => project(:main).task('db-migrate')
  task 'db-clean' => project(:main).task('db-clean')
  task 'db-reset' => project(:main).task('db-reset')
  task 'db-init' => project(:main).task('db-init')
end
