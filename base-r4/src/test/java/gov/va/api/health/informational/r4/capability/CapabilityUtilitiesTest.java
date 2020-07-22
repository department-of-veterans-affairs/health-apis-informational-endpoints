package gov.va.api.health.informational.r4.capability;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.r4.api.resources.CapabilityStatement;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {CapabilityUtilitiesTest.TestConfiguration.class, MetadataTestConfig.class},
    initializers = ConfigFileApplicationContextInitializer.class)
public class CapabilityUtilitiesTest {

  @Autowired CapabilityStatementProperties capabilityStatementProperties;

  @Autowired CapabilityResourcesProperties capabilityResourcesProperties;

  /** Object mapper to translate json to/from objects. */
  private ObjectMapper mapper = new ObjectMapper();

  /** Demonstrate the capability utilities can create a capability statement from properties. */
  @Test
  @SneakyThrows
  public void capabilityUtilitiesTest() {

    final CapabilityStatement expectedCapability =
        mapper.readValue(
            Paths.get("src", "test", "resources", "capability-statement.json").toFile(),
            CapabilityStatement.class);

    final CapabilityStatement capability =
        CapabilityUtilities.initializeCapabilityStatementBuilder(
            "CapabilityStatement", capabilityStatementProperties, capabilityResourcesProperties);

    assertEquals(expectedCapability, capability);
  }

  @Test
  @SneakyThrows
  @DirtiesContext
  public void capabilityUtilitiesWithOptionalsTest() {

    capabilityStatementProperties
        .getSecurity()
        .setManagementEndpoint(Optional.of("https://example.com/oauth2/manage"));
    capabilityStatementProperties
        .getSecurity()
        .setRevocationEndpoint(Optional.of("https://example.com/oauth2/revoke"));

    final CapabilityStatement expectedCapability =
        mapper.readValue(
            Paths.get("src", "test", "resources", "capability-statement_with_optionals.json")
                .toFile(),
            CapabilityStatement.class);

    final CapabilityStatement capability =
        CapabilityUtilities.initializeCapabilityStatementBuilder(
            "CapabilityStatement", capabilityStatementProperties, capabilityResourcesProperties);

    assertEquals(expectedCapability, capability);
  }

  // Loads our properties file into a CapabilityStatementProperties bean that we can use.
  @EnableConfigurationProperties(value = CapabilityStatementProperties.class)
  public static class TestConfiguration {}
}
