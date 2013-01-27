#require 'buildr/java/cobertura'
require 'buildr/jetty'

VERSION_NUMBER = '0.1-SNAPSHOT'

HAMCREST = [:hamcrest_core, :hamcrest_library, :hamcrest_extra]
NO_LOG = [:jcl_over_slf4j, :slf4j_api, :slf4j_silent]
MUSTACHE = [:guava, :mustache]

['db-migrate', 'db-clean', 'db-reset', 'db-drop', 'db-init'].each { |t| Project.local_task t }

define 'petstore', :group => 'org.testinfected.petstore', :version => VERSION_NUMBER do
  compile.options.source = '1.6'
  compile.options.target = '1.6'
  
  define 'domain' do
    compile.with
    test.with HAMCREST
    package :jar
  end

  define 'persistence' do
    compile.with project(:domain)
    test.with project(:domain).test.compile.target, HAMCREST, :flyway, :mysql, NO_LOG
    package(:jar)
  end

  define 'webapp' do
    compile.with :simpleframework, MUSTACHE
    compile.with project(:domain), project(:persistence)

    test.with project(:domain).test.compile.target, project(:persistence).test.compile.target, project(:persistence).test.resources.target
    test.with HAMCREST, :time, :antlr_runtime, :cssselectors, :hamcrest_dom, :flyway, NO_LOG, :mysql, :juniversalchardet
    test.with transitive(artifacts(:nekohtml, :htmlunit, :jmock_legacy))
    test.using :properties => { 'web.root' => _(:src, :main, :webapp) }

    package :jar
  end
  
  define 'main' do
    compile.with project(:domain), project(:persistence), project(:webapp), :cli, :flyway
    test.with project(:webapp).test.compile.target
    test.with :simpleframework, MUSTACHE, :time, HAMCREST, :flyway, :mysql, NO_LOG
    test.with transitive(artifacts(:selenium_firefox_driver, :selenium_ghost_driver, :windowlicker_web, :htmlunit))

    test.using :integration, :properties => { 
      'web.root' => project(:webapp).path_to(:src, :main, :webapp),
      'browser.driver' => 'remote',
      'browser.remote.url' => Buildr.settings.build['selenium']['server']['url'],
      'browser.capability.browserName' => Buildr.settings.build['selenium']['server']['browser']['name'],
      'browser.capability.name' => 'PetStore System Tests'
    }

    integration.setup do
      selenium.run
    end
    
    integration.teardown do
      selenium.stop
    end

    def migrations(action)
      Java::Commands.java("org.testinfected.petstore.Migrations", "-e", Buildr.environment, action.to_s,
        :classpath => [project.compile.target, project.resources.target, :mysql] + project.compile.dependencies) do
          exit
        end
    end

    task 'db-init' => :compile do migrations :init; end
    task 'db-migrate' => :compile do migrations :migrate; end
    task 'db-clean' => :compile do migrations :clean; end
    task 'db-drop' => :compile do migrations :drop; end
    task 'db-reset' => :compile do migrations :reset; end
  end

  task :run => project(:main) do
    cp = [project(:main).compile.target, project(:main).resources.target, project(:main).compile.dependencies,
          :mysql, :simpleframework, MUSTACHE, :time]
    Java::Commands.java("org.testinfected.petstore.Launcher", "-p", Buildr.settings.profile['server.port'], "-e", Buildr.environment, project(:webapp).path_to(:src, :main, :webapp), :classpath => cp) { exit }
  end

  task 'db-migrate' => project(:main).task('db-migrate')
  task 'db-clean' => project(:main).task('db-clean')
  task 'db-reset' => project(:main).task('db-reset')
  task 'db-init' => project(:main).task('db-init')
end
