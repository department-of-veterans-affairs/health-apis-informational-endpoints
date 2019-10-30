package gov.va.api.health.informational.r4.capability;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.r4.api.resources.Capability;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
  classes = {MetadataTestConfig.class, CapabilityStatementProperties.class},
  initializers = ConfigFileApplicationContextInitializer.class
)
public class MetadataControllerTest {

  @Autowired CapabilityStatementProperties capabilityStatementProperties;

  @Autowired CapabilityResourcesProperties capabilityResourcesProperties;

  @SneakyThrows
  private String pretty(Capability capability) {
    return JacksonConfig.createMapper()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(capability);
  }

  @Test
  @SneakyThrows
  public void read() {
    MetadataController controller =
        new MetadataController(capabilityStatementProperties, capabilityResourcesProperties);
    Capability old =
        JacksonConfig.createMapper()
            .readValue(getClass().getResourceAsStream("/capability.json"), Capability.class);
    try {
      assertThat(pretty(controller.read())).isEqualTo(pretty(old));
    } catch (AssertionError e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @EnableAutoConfiguration
  @EnableConfigurationProperties(value = CapabilityStatementProperties.class)
  public static class TestConfiguration {}
}
