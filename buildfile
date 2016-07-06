VERSION_NUMBER = '0.2-SNAPSHOT'

SIMPLE = [:simple_common, :simple_transport, :simple_http]
NO_LOG = [:jcl_over_slf4j, :slf4j_api, :slf4j_silent]

%w(db-migrate db-clean db-reset db-drop db-init).each { |t| Project.local_task t }

define 'petstore', :group => 'org.testinfected.petstore', :version => VERSION_NUMBER do
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  
  define 'domain' do
    compile.with
    test.with :hamcrest
    package :jar
  end

  define 'persistence' do
    compile.with project(:domain), :tape
    test.with project(:domain).test.compile.target, :hamcrest, :flyway, :mysql, NO_LOG
    package :jar
  end

  define 'webapp' do
    compile.with :molecule, :jmustache
    compile.with project(:domain), project(:persistence)

    test.with project(:domain).test.compile.target,
              project(:persistence).test.compile.target,
              project(:persistence).test.resources.target

    test.with :hamcrest, :cssselectors, :hamcrest_dom, :molecule_test, NO_LOG
    test.with transitive(artifacts(:nekohtml))
    test.using :properties => { 'web.root' => _(:src, :main, :content) }

    package :jar
  end
  
  define 'server' do
    compile.with project(:domain), project(:persistence), project(:webapp), :cli, :flyway, :molecule, SIMPLE, :tape

    test.using :integration, :properties => {
      'web.root' => project(:webapp).path_to(:src, :main, :content),
      'browser.driver' => 'remote',
      'browser.remote.url' => Buildr.settings.build['selenium']['server']['url'],
      'browser.capability.browserName' => Buildr.settings.build['selenium']['server']['browser']['name'],
      'browser.capability.name' => 'PetStore System Tests'
    }

    test.with project(:domain).test.compile.target,
              project(:persistence).test.compile.target,
              project(:persistence).test.resources.target,
              project(:webapp).test.compile.target

    test.with :molecule_test, :jmustache, :hamcrest, :tape, :flyway, :mysql, NO_LOG, :juniversalchardet, :mario
    test.with transitive(artifacts(:selenium_firefox_driver, :selenium_ghost_driver))
    integration.setup { selenium.run }
    integration.teardown { selenium.stop }

    package(:jar).tap do |jar|
      jar.merge artifacts(:cli, :molecule, :jmustache, SIMPLE, :mysql, :flyway, :tape)
      jar.merge artifacts(project(:domain), project(:persistence), project(:webapp))
      jar.with :manifest => manifest.merge( 'Main-Class' => 'org.testinfected.petstore.Launcher' )
    end
  end

  task :run => project(:server).package do
    args = []
    args << '-h' << Buildr.settings.profile['server.host']
    args << '-p' << Buildr.settings.profile['server.port']
    args << '-e' << Buildr.environment
    args << '--timeout' << Buildr.settings.profile['sessions.timeout']
    args << '-q' if Buildr.settings.profile['server.quiet']
    args << project(:webapp).path_to(:src, :main, :content)

    Java::Commands.java("-jar", project(:server).package.to_s, *args) { exit }
  end

  def migrations(action)
    Java::Commands.java('org.testinfected.petstore.Migrations', '-e', Buildr.environment, action.to_s,
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
