# Make that configurable, i.e.:
# - Selenium version
# - SLF4J version
# - Server port
# - Log/silent output
# - Wait timeout
module Buildr
  class SeleniumServer
  
    # Which version of Selenium Server we're using by default
    VERSION = "2.21.0"
    DOWNLOAD_URL = "http://selenium.googlecode.com/files/selenium-server-standalone-#{VERSION}.jar"

    SELENIUM_SERVER = Buildr.artifact("org.seleniumhq.selenium:selenium-server-standalone:jar:#{VERSION}")
    Buildr.download( SELENIUM_SERVER => DOWNLOAD_URL  )
    
    SLF4J_VERSION = "1.5.8"

    # Librairies that we need
    REQUIRES = [ SELENIUM_SERVER, "org.slf4j:slf4j-api:jar:#{SLF4J_VERSION}", "org.slf4j:slf4j-simple:jar:#{SLF4J_VERSION}", "org.slf4j:jcl104-over-slf4j:jar:#{SLF4J_VERSION}" ]
  
    Java.classpath << REQUIRES

    # Default URL for Selenium Server (change with options.url).
    URL = "http://localhost:4444"
  
    class << self

      # :call-seq:
      #   instance() => SeleniumServer
      #
      # Returns an instance of SeleniumServer.
      def instance
        @instance ||= SeleniumServer.new(URL)
      end
    end
    
    attr_reader :url
  
    def initialize(url)
      @url = url
    end
  
    def start(sync = nil)
      puts "Starting Selenium Server at #{url}" if verbose

      begin
        Java.load
        Java.org.openqa.selenium.server.SeleniumServer.main []
        sync << "Starting" if sync
        sleep # Forever
      rescue Interrupt # Stopped from console
        puts "Shutting down" if verbose
      rescue Exception => error
        puts "An error occured" if verbose
        puts "#{error.class}: #{error.message}"
      end
      exit!
    end
    
    def running?
      false
    end
  
    def run
      unless running?
        Java.load
        logger = Java.java.util.logging.Logger.getLogger("org.openqa")
        logger.setLevel(Java.java.util.logging.Level.WARNING)

        sync = Queue.new
        Thread.new { start sync }
        sync.pop == "Starting" or fail "Selenium Server not started"
      
        # Wait for Selenium to fire up before doing anything else.
        started = false
        20.times do
          begin
            Net::HTTP.get(URI.parse("#{url}/selenium-server"))
            started = true
          rescue Errno::ECONNREFUSED
            sleep 1
          end
        end 
        started or fail "Selenium Server not responding"
      
        puts "Selenium Server started" if verbose
      end
    end
  
    def stop
      puts "Stopping Selenium Server" if verbose
      begin
        Net::HTTP.get(URI.parse("#{url}/selenium-server/driver/?cmd=shutDownSeleniumServer"))
      rescue Errno::ECONNREFUSED
        # Expected if server not running.
      rescue EOFError
        # We get EOFError when Selenium Server is brutally killed.
      end
    end
  end

  namespace "selenium" do
    desc "Start an instance of Selenium Server running in the background"
    task("start") { SeleniumServer.instance.start }
    desc "Stop an instance of Selenium Server running in the background"
    task("stop") { SeleniumServer.instance.stop }
  end

  # :call-seq:
  #   selenium() => SeleniumServer
  #
  # Returns a SeleniumServer. You can use this to discover the Jetty#use task,
  # configure the Jetty#setup and Jetty#teardown tasks, deploy and undeploy to Jetty.
  def selenium
    @selenium ||= SeleniumServer.instance
  end
end