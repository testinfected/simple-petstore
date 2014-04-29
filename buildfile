VERSION_NUMBER = '0.2-SNAPSHOT'

HAMCREST = [:hamcrest_core, :hamcrest_library, :hamcrest_extra]
NO_LOG = [:jcl_over_slf4j, :slf4j_api, :slf4j_silent]

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
    package :jar
  end

  define 'molecule' do
    compile.with :simpleweb, :jmustache

    test.with HAMCREST, NO_LOG, :juniversalchardet
    test.with transitive(artifacts(:nekohtml, :htmlunit, :jmock_legacy))

    package :jar
  end

  define 'webapp' do
    compile.with :simpleweb, :jmustache
    compile.with project(:domain), project(:persistence), project(:molecule)

    test.with project(:domain).test.compile.target,
              project(:persistence).test.compile.target,
              project(:persistence).test.resources.target,
              project(:molecule).test.compile.target

    test.with HAMCREST, :antlr_runtime, :guava, :cssselectors, :hamcrest_dom, :flyway, NO_LOG, :mysql, :juniversalchardet
    test.with transitive(artifacts(:nekohtml, :htmlunit))
    test.using :properties => { 'web.root' => _(:src, :main, :webapp) }

    package :jar
  end
  
  define 'server' do
    compile.with project(:domain), project(:persistence), project(:molecule), project(:webapp), :cli, :flyway

    test.using :integration, :properties => {
      'web.root' => project(:webapp).path_to(:src, :main, :webapp),
      'browser.driver' => 'remote',
      'browser.remote.url' => Buildr.settings.build['selenium']['server']['url'],
      'browser.capability.browserName' => Buildr.settings.build['selenium']['server']['browser']['name'],
      'browser.capability.name' => 'PetStore System Tests'
    }
    test.with project(:molecule).test.compile.target, project(:webapp).test.compile.target
    test.with :simpleweb, :jmustache, HAMCREST, :mysql, NO_LOG
    test.with transitive(artifacts(:htmlunit, :selenium_firefox_driver, :selenium_ghost_driver, :windowlicker_web))
    integration.setup { selenium.run }
    integration.teardown { selenium.stop }

    package(:jar).tap do |jar|
      jar.merge artifacts(:cli, :simpleweb, :jmustache, :mysql, :flyway)
      jar.merge artifacts(project(:domain), project(:persistence), project(:molecule), project(:webapp))
      jar.with :manifest => manifest.merge( 'Main-Class' => 'org.testinfected.petstore.Launcher' )
    end
  end

  task :run => project(:server).package do
    Java::Commands.java("-jar", project(:server).package.to_s,
      "-p", Buildr.settings.profile['server.port'],
      "-e", Buildr.environment,
      "-q",
      project(:webapp).path_to(:src, :main, :webapp)) { exit }
  end

  def migrations(action)
    Java::Commands.java("org.testinfected.petstore.Migrations", "-e", Buildr.environment, action.to_s,
      :classpath => project(:server).package.to_s) { exit }
  end

  task 'db-init' do
    migrations :init
  end
  task 'db-migrate' do
    migrations :migrate
  end
  task 'db-clean' do
    migrations :clean
  end
  task 'db-drop' do
    migrations :drop
  end
  task 'db-reset' do
    migrations :reset
  end
end
