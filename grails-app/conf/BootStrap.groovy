import org.powertac.common.Broker
import org.powertac.common.Competition
import org.powertac.common.Product
import org.powertac.common.Timeslot
import org.powertac.common.enumerations.ProductType
import org.powertac.common.Role
import org.powertac.common.BrokerRole

class BootStrap {

  def springSecurityService
  def simpleGencoService

  def init = { servletContext ->
    // Create admin role
    def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
    // Create default broker
    def adminUser = Broker.findByUsername('defaultBroker') ?: new Broker(
        username: 'defaultBroker',
        password: springSecurityService.encodePassword('password'),
        apiKey: '5d064dd7-1ec9-4a98-bece-2ca09b03e364',
        enabled: true).save(failOnError: true)

    // Add default broker to admin role
    if (!adminUser.authorities.contains(adminRole)) {
      BrokerRole.create adminUser, adminRole
    }
    
    // Initialize the genco service
    simpleGencoService.init()

    // Create default competition
    def competition = new Competition(name: "defaultCompetition").save()
    competition.addToBrokers(adminUser);

  }
  def destroy = {
  }
}
