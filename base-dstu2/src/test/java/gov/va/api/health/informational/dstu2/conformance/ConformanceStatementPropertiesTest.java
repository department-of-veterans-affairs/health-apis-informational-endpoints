package gov.va.api.health.informational.dstu2.conformance;

import org.junit.Test;

/** Exercise afterPropertiesSet of ConformanceStatementProperties to increase code coverage. */
public class ConformanceStatementPropertiesTest {

  /**
   * Minimum viable configuration for ConformanceStatementProperties to instantiate.
   *
   * @return Instance of ConformanceStatementProperties.
   */
  private ConformanceStatementProperties getMinimumFakeConfig() {
    ConformanceStatementProperties config = new ConformanceStatementProperties();
    // Set required properties.
    config.setId("ID");
    config.setPublicationDate("date");
    config.setFhirVersion("1.0.2");
    config.setSoftwareName("test-service");
    ConformanceStatementProperties.SecurityProperties securityProperties =
        new ConformanceStatementProperties.SecurityProperties();
    securityProperties.setAuthorizeEndpoint("https://example.com/oauth2/authorization");
    securityProperties.setTokenEndpoint("https://example.com/oauth2/token");
    securityProperties.setDescription("http://docs.smarthealthit.org/");
    config.setSecurity(securityProperties);
    return config;
  }

  /** Don't set optional fields so warnings are logged. */
  @Test
  public void minimumConfigTest() {
    ConformanceStatementProperties config = getMinimumFakeConfig();
    // Validate to get warnings.
    config.afterPropertiesSet();
  }

  /** Don't set optional fields of ContactProperties so warnings are logged. */
  @Test
  public void minimumContactConfigTest() {
    ConformanceStatementProperties config = getMinimumFakeConfig();
    ConformanceStatementProperties.ContactProperties contactProperties =
        new ConformanceStatementProperties.ContactProperties();
    contactProperties.setName("Test Person");
    config.setContact(contactProperties);
    // Validate to get warnings.
    config.afterPropertiesSet();
  }
}
