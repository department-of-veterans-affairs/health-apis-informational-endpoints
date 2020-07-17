package gov.va.api.health.informational.dstu2.conformance;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.dstu2.api.resources.Conformance;
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
    classes = {ConformanceUtilitiesTest.TestConfiguration.class, MetadataTestConfig.class},
    initializers = ConfigFileApplicationContextInitializer.class)
public class ConformanceUtilitiesTest {

  @Autowired ConformanceStatementProperties conformanceStatementProperties;

  @Autowired ConformanceResourcesProperties conformanceResourcesProperties;

  /** Object mapper to translate json to/from objects. */
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  @SneakyThrows
  @DirtiesContext
  public void capabilityUtilitiesMinTest() {
    conformanceStatementProperties.getSecurity().setManagementEndpoint(Optional.ofNullable(null));
    conformanceStatementProperties.getSecurity().setRevocationEndpoint(Optional.ofNullable(null));
    final Conformance expectedConformance =
        mapper.readValue(
            Paths.get("src", "test", "resources", "conformance_minimum.json").toFile(),
            Conformance.class);
    final Conformance conformance =
        ConformanceUtilities.initializeConformanceBuilder(
            "Conformance", conformanceStatementProperties, conformanceResourcesProperties);
    assertEquals(expectedConformance, conformance);
  }

  /** Demonstrate the capability utilities can create a capability statement from properties. */
  @Test
  @SneakyThrows
  public void capabilityUtilitiesTest() {
    final Conformance expectedConformance =
        mapper.readValue(
            Paths.get("src", "test", "resources", "conformance.json").toFile(), Conformance.class);
    final Conformance conformance =
        ConformanceUtilities.initializeConformanceBuilder(
            "Conformance", conformanceStatementProperties, conformanceResourcesProperties);
    assertEquals(expectedConformance, conformance);
  }

  // Loads our properties file into a ConformanceStatementProperties bean that we can use.
  @EnableConfigurationProperties(value = ConformanceStatementProperties.class)
  public static class TestConfiguration {}
}
