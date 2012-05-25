# Make that a proper extension, which should be configurable, i.e.:
# - log/silent output
# - wait timeout
# - selenium server port

def start_selenium
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

def stop_selenium
  puts "Stopping Selenium Server" if verbose
  Net::HTTP.get(URI.parse('http://localhost:4444/selenium-server/driver/?cmd=shutDownSeleniumServer'))
end