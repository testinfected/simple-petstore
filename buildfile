require 'buildr/java/cobertura'

VERSION_NUMBER = '0.9-SNAPSHOT'

COMMONS = struct(
  :lang => 'commons-lang:commons-lang:jar:2.4',
  :dbcp => transitive('commons-dbcp:commons-dbcp:jar:1.2')
)

HIBERNATE = struct(
  :annotations => transitive('org.hibernate:hibernate-annotations:jar:3.5.1-Final'),
  :validator => transitive('org.hibernate:hibernate-validator:jar:4.0.2.GA')
)

JAVASSIST = 'javassist:javassist:jar:3.8.0.GA'

SPRING = struct(
  :core => transitive('org.springframework:spring-core:jar:3.0.2.RELEASE'),
  :context => transitive('org.springframework:spring-context:jar:3.0.2.RELEASE'),
  :tx => transitive('org.springframework:spring-tx:jar:3.0.2.RELEASE'),
  :orm => transitive('org.springframework:spring-orm:jar:3.0.2.RELEASE')
)

SLF4J = struct(
  :silent => 'org.slf4j:slf4j-nop:jar:1.5.8'
)

HAMCREST = group('hamcrest-core', 'hamcrest-library', :under => 'org.hamcrest', :version => '1.3.RC2')

HAMCREST_MATCHERS = struct(
  :core => 'org.testinfected.hamcrest-matchers:core-matchers:jar:1.5',
  :validation => 'org.testinfected.hamcrest-matchers:validation-matchers:jar:1.5',
  :jpa => transitive('org.testinfected.hamcrest-matchers:jpa-matchers:jar:1.5')
)

CARBON_5 = transitive('com.carbonfive.db-support:db-migration:jar:0.9.9-m2')

MYSQL = 'mysql:mysql-connector-java:jar:5.1.11'

define 'petstore', :group => 'com.pyxis.simple-petstore', :version => VERSION_NUMBER do
  
  compile.options.target = '1.6'

  desc 'The domain jar'
  define 'domain' do
    compile.with COMMONS.lang, HIBERNATE.annotations, HIBERNATE.validator, SPRING.context
    
    test.with HAMCREST, HAMCREST_MATCHERS.core, HAMCREST_MATCHERS.validation, SLF4J.silent

    package :jar
    package(:jar, :classifier => 'tests').include(_(:target, :test, :classes, '*'))
  end
  
  desc 'The infrastructure jar'
  define 'infrastructure' do
    compile.with project(:domain), project(:domain).compile.dependencies, SPRING.tx
    
    test.resources.filter.using 'migrations.dir' => _(:src, :main, :scripts, :migrations), 'test.log.dir' => _(:target, :logs)
    test.with HAMCREST, HAMCREST_MATCHERS.core, HAMCREST_MATCHERS.jpa, CARBON_5, COMMONS.dbcp, SPRING.orm, JAVASSIST, MYSQL, SLF4J.silent

    package :jar
  end
end
