require 'buildr/java/cobertura'

VERSION_NUMBER = '0.9-SNAPSHOT'

APACHE_COMMONS = ['commons-lang:commons-lang:jar:2.4']
HIBERNATE_ANNOTATIONS = transitive('org.hibernate:hibernate-annotations:jar:3.5.1-Final')
HIBERNATE_VALIDATOR = transitive('org.hibernate:hibernate-validator:jar:4.0.2.GA')
SPRING_CONTEXT = transitive('org.springframework:spring-context:jar:3.0.2.RELEASE')

NO_LOGGING = group('slf4j-nop', :under => 'org.slf4j', :version => '1.5.10')
HAMCREST = group('hamcrest-core', 'hamcrest-library', :under => 'org.hamcrest', :version => '1.3.RC2')
HAMCREST_MATCHERS = group('core-matchers', 'validation-matchers', :under => 'org.testinfected.hamcrest-matchers', :version => '1.5')

define 'petstore', :group => 'com.pyxis.simple-petstore', :version => VERSION_NUMBER do
  compile.options.target = '1.6'

  desc 'The domain'
  define 'domain' do
    compile.with APACHE_COMMONS, HIBERNATE_ANNOTATIONS, HIBERNATE_VALIDATOR, SPRING_CONTEXT
    
    test.with HAMCREST, HAMCREST_MATCHERS, NO_LOGGING
    package :jar
    package(:jar, :classifier => 'tests').include(_('target/test/classes/*'))
  end
end
