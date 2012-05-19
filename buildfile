require 'buildr/java/cobertura'
require 'buildr/jetty'

VERSION_NUMBER = '0.9-SNAPSHOT'

HAMCREST = [:hamcrest_core, :hamcrest_library, :hamcrest_extra]
LOG = [:slf4j_api, :slf4j_log4j, :slf4j_jcl, :log4j]
NO_LOG = [:slf4j_api, :slf4j_silent]
VELOCITY = [:commons_beanutils, :commons_digester, :commons_chain, :velocity_engine, :velocity_tools]

Project.local_task 'jetty'

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
  
  define 'app' do
    resources.filter.using 'log.dir' => _(:target, :logs)
    
    compile.with project(:domain), project(:domain).compile.dependencies, project(:infrastructure), project(:infrastructure).compile.dependencies, VELOCITY
    compile.with_transitive :spring_web, :spring_mvc, :servlet_api
    
    test.setup { makedirs _(:target, :logs) }
    test.resources.filter.using 'webapp.dir' => _(:src, :main, :webapp), 'test.log.dir' => _(:target, :logs)
    test.with project(:domain).test.compile.target, HAMCREST 
    test.with_transitive :hamcrest_dom, :hamcrest_spring, :neko_html, :commons_lang, :spring_support, :spring_test
    
    package(:war).provided_dependencies :servlet_api
    package(:war).runtime_dependencies LOG, :commons_pool, :commons_dbcp, :javassist, :asm, :cglib, :spring_orm, :spring_jdbc, :sitemesh, :url_rewrite, :mysql
  end
  
  task('jetty' => [project(:app).package(:war), jetty.use]) do |task|
    port = Buildr.settings.profile['server.port']
    context_path = Buildr.settings.profile['filter']['context.path']
    
    jetty.deploy("http://localhost:#{port}#{context_path}", task.prerequisites.first)
    puts 'Press CTRL-C to stop Jetty'
    trap 'SIGINT' do
      jetty.stop
    end
    Thread.stop
  end
end
