grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // uncomment to disable ehcache
    // excludes 'ehcache'
  }
  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  repositories {
    grailsPlugins()
    grailsHome()
    mavenRepo "http://ibwstinger.iw.uni-karlsruhe.de/artifactory/plugins-release-local/"
    mavenRepo "http://ibwstinger.iw.uni-karlsruhe.de/artifactory/libs-release-local/"
    grailsCentral()

    // uncomment the below to enable remote dependency resolution
    // from public Maven repositories
    mavenLocal()
    mavenCentral()
    mavenRepo "http://snapshots.repository.codehaus.org"
    mavenRepo "http://repository.codehaus.org"
    mavenRepo "http://download.java.net/maven/2/"
    mavenRepo "http://repository.jboss.com/maven2/"
    mavenRepo "http://repository.apache.org"
    mavenRepo "http://repository.apache.org/snapshots"
  }

  plugins {
    compile "org.powertac.plugins:powertac-common:0.1"
  }

  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
    // runtime 'mysql:mysql-connector-java:5.1.13'
    compile('org.apache.activemq:activemq-core:5.4.1') {
      excludes 'commons-collections'
      //excludes 'commons-pool'
      excludes 'commons-logging'
      excludes 'junit'
      excludes 'log4j'
      excludes 'spring-context'
      excludes 'spring-parent'
      excludes 'spring-aop'
      excludes 'spring-asm'
      excludes 'spring-beans'
      excludes 'spring-expression'
    }

    compile('org.apache.activemq:activemq-pool:5.4.1') {
      excludes 'commons-collections'
      excludes 'commons-pool'
      excludes 'commons-logging'
      excludes 'junit'
      excludes 'log4j'
      excludes 'spring-context'
      excludes 'spring-parent'
      excludes 'spring-aop'
      excludes 'spring-asm'
      excludes 'spring-beans'
      excludes 'spring-expression'
    }
  }
}

coverage {
  xml = true
  nopost = true

  exclusions = [
      "org/grails/**",
      "**BuildConfig**"]
}
