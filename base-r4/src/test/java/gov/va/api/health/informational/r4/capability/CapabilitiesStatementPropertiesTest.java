package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.CapabilityStatement;
import org.junit.Test;

/** Exercise afterPropertiesSet of CapabilitiesStatementProperties to increase code coverage. */
public class CapabilitiesStatementPropertiesTest {

  /**
   * Minimum viable configuration for CapabilityStatementProperties to instantiate.
   *
   * @return Instance of CapabilityStatementProperties.
   */
  private CapabilityStatementProperties getMinimumFakeConfig() {
    CapabilityStatementProperties config = new CapabilityStatementProperties();
    // Set required properties.
    config.setId("ID");
    config.setStatus(CapabilityStatement.Status.draft);
    config.setPublicationDate("date");
    config.setKind(CapabilityStatement.Kind.instance);
    config.setFhirVersion("4.0.0");
    config.setSoftwareName("test-service");
    CapabilityStatementProperties.SecurityProperties securityProperties =
        new CapabilityStatementProperties.SecurityProperties();
    securityProperties.setAuthorizeEndpoint("https://example.com/oauth2/authorization");
    securityProperties.setTokenEndpoint("https://example.com/oauth2/token");
    securityProperties.setDescription("http://docs.smarthealthit.org/");
    config.setSecurity(securityProperties);
    return config;
  }

  /** Don't set optional fields so warnings are logged. */
  @Test
  public void minimumConfigTest() {
    CapabilityStatementProperties config = getMinimumFakeConfig();
    // Validate to get warnings.
    config.afterPropertiesSet();
  }

  /** Don't set optional fields of ContactProperties so warnings are logged. */
  @Test
  public void minimumContactConfigTest() {
    CapabilityStatementProperties config = getMinimumFakeConfig();
    CapabilityStatementProperties.ContactProperties contactProperties =
        new CapabilityStatementProperties.ContactProperties();
    contactProperties.setName("Test Person");
    config.setContact(contactProperties);
    // Validate to get warnings.
    config.afterPropertiesSet();
  }
}
