package gov.va.api.health.informational.r4.wellknown;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties;
import gov.va.api.health.r4.api.information.WellKnown;
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
    classes = {
      WellKnownUtilitiesTest.TestConfiguration.class,
      WellKnownUtilitiesTest.TestConfiguration2.class
    },
    initializers = ConfigFileApplicationContextInitializer.class)
public class WellKnownUtilitiesTest {
  @Autowired CapabilityStatementProperties capabilityStatementProperties;

  @Autowired WellKnownProperties wellKnownProperites;

  /** Object mapper to translate json to/from objects. */
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  @SneakyThrows
  public void testBuilder() {
    final WellKnown expected =
        mapper.readValue(
            Paths.get("src", "test", "resources", "wellknown.json").toFile(), WellKnown.class);
    final WellKnown result =
        WellKnownUtilities.initializeWellKnownBuilder(
            capabilityStatementProperties, wellKnownProperites);
    assertEquals(expected, result);
  }

  @Test
  @SneakyThrows
  @DirtiesContext
  public void testBuilderWithOptionals() {
    capabilityStatementProperties
        .getSecurity()
        .setManagementEndpoint(Optional.of("https://example.com/oauth2/manage"));
    capabilityStatementProperties
        .getSecurity()
        .setRevocationEndpoint(Optional.of("https://example.com/oauth2/revoke"));
    final WellKnown expected =
        mapper.readValue(
            Paths.get("src", "test", "resources", "wellknown_with_optionals.json").toFile(),
            WellKnown.class);

    final WellKnown result =
        WellKnownUtilities.initializeWellKnownBuilder(
            capabilityStatementProperties, wellKnownProperites);
    assertEquals(expected, result);
  }

  // Loads our properties file into a CapabilityStatementProperties bean that we can use.
  @EnableConfigurationProperties(value = WellKnownProperties.class)
  public static class TestConfiguration {}

  @EnableConfigurationProperties(value = CapabilityStatementProperties.class)
  public static class TestConfiguration2 {}
}
