# Make that a proper addon, which should be configurable, i.e.:
# - log/silent output
# - wait timeout
# - selenium server port
class SeleniumServer
  
  class << self

    # :call-seq:
    #   instance() => SeleniumServer
    #
    # Returns an instance of SeleniumServer.
    def instance()
      @instance ||= SeleniumServer.new
    end
  end
  
  def start
    puts "Starting Selenium Server" if verbose
    # Disable selenium command logging, which is way too verbose
    logger = Java.java.util.logging.Logger.getLogger("org.openqa")
    logger.setLevel(Java.java.util.logging.Level.WARNING)
    Java.org.openqa.selenium.server.SeleniumServer.main []
    started = false
    20.times do
      begin
        Net::HTTP.get(URI.parse('http://localhost:4444/selenium-server'))
        started = true
      rescue Errno::ECONNREFUSED
        sleep 1
      end
    end 
    started or fail "Could not start Selenium Server"
    puts "Selenium Server started" if verbose
  end

  def stop
    puts "Stopping Selenium Server" if verbose
    Net::HTTP.get(URI.parse('http://localhost:4444/selenium-server/driver/?cmd=shutDownSeleniumServer'))
  end
end

# For this to work, start selenium in a separate thread and have it sleep indefinitely
#namespace "selenium" do
#  desc "Start an instance of Selenium Server running in the background"
#  task("start") { SeleniumServer.instance.start }
#  desc "Stop an instance of Selenium Server running in the background"
#  task("stop") { SeleniumServer.instance.stop }
#end

# :call-seq:
#   selenium() => SeleniumServer
#
# Returns a SeleniumServer. You can use this to discover the Jetty#use task,
# configure the Jetty#setup and Jetty#teardown tasks, deploy and undeploy to Jetty.
def selenium()
  @selenium ||= SeleniumServer.instance
end