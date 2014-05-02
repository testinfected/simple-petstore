require 'uri'
require 'thread'

module Buildr
  class SeleniumServer
    # Which version of Selenium Server we're using by default (change with :selenium :version in build settings).
    VERSION = "2.21.0"
    # Which version of SLF4J we use (change with :selenium :slf4j_version in build settings).
    SLF4J_VERSION = "1.5.6"
    # Use selenium server default port (change with :selenium :port in build settings).
    PORT = 4444
    # By default, enable server output (change with :selenium :silent in build settings)
    SILENT = false
    
    class << self
      # :call-seq:
      #   instance() => SeleniumServer
      #
      # Returns an instance of SeleniumServer.
      def instance
        @instance ||= SeleniumServer.new(port)
      end
      
      def settings
        Buildr.settings.build['selenium'] || {}
      end
      
      def port
        settings['port'] || PORT
      end
      
      def version
        settings['version'] || VERSION
      end
      
      def slf4j_version
        settings['slf4j_version'] || SLF4J_VERSION
      end
      
      def silent?
        settings['silent'] || SILENT
      end
    end
    
    DOWNLOAD_URL = "http://selenium-release.storage.googleapis.com/#{version[0..-3]}/selenium-server-standalone-#{version}.jar"
    SELENIUM_SERVER = Buildr.artifact("org.seleniumhq.selenium:selenium-server-standalone:jar:#{version}")
    Buildr.download( SELENIUM_SERVER => DOWNLOAD_URL  )
    SLF4J_BINDING = silent? ? 'slf4j-nop' : 'slf4j-simple'
    REQUIRED_LIBRAIRIES = [ SELENIUM_SERVER, 
                            "org.slf4j:slf4j-api:jar:#{slf4j_version}", 
                            "org.slf4j:jul-to-slf4j:jar:#{slf4j_version}",
                            "org.slf4j:jcl-over-slf4j:jar:#{slf4j_version}",
                            "org.slf4j:#{SLF4J_BINDING}:jar:#{slf4j_version}" ]
                            
    Java.classpath << REQUIRED_LIBRAIRIES

    attr_accessor :port
  
    def initialize(port)
      @port = port
      assimilate_logs
    end
  
    def start(sync = nil)
      puts "Starting Selenium Server at #{url}" if verbose
      begin
        Java.org.openqa.selenium.server.SeleniumServer.main ["-port", port.to_s]
        sync << "Started" if sync
        sleep
      rescue Interrupt
        puts "Shutting down" if verbose
      rescue Exception => error
        puts "An error occured starting Selenium Server:" if verbose
        puts "#{error.class}: #{error.message}"
      end
      exit!
    end
    
    def running?
      begin
        response  = Net::HTTP.get_response(URI.parse(url))
        response.is_a?(Net::HTTPSuccess)
      rescue Errno::ECONNREFUSED
        false
      end
    end
    
    def run
      unless running?
        sync = Queue.new
        Thread.new { start sync }
        sync.pop == "Started" or fail "Selenium Server not started"
        puts "Selenium Server started" if verbose
      end
    end
  
    def stop
      puts "Stopping Selenium Server" if verbose
      begin
        Net::HTTP.get(URI.parse("#{url}/driver/?cmd=shutDownSeleniumServer"))
      rescue Errno::ECONNREFUSED
        # Expected if server not running.
      rescue EOFError
        # We get EOFError when Selenium Server is brutally killed.
      end
    end
    
    protected
    def assimilate_logs
      Java.load
      Java.java.util.logging.LogManager.getLogManager.reset
      Java.org.slf4j.bridge.SLF4JBridgeHandler.install
    end
      
    def url
      "http://localhost:#{port}/selenium-server"
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
  # Returns a SeleniumServer.
  def selenium
    @selenium ||= SeleniumServer.instance
  end
end