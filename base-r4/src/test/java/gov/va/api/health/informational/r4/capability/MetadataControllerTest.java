package gov.va.api.health.informational.r4.capability;

import static gov.va.api.health.informational.r4.utils.JsonUtils.prettyJson;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.r4.api.resources.CapabilityStatement;
import java.io.IOException;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {MetadataControllerTest.TestConfiguration.class, MetadataTestConfig.class})
@TestPropertySource("/application.properties")
public class MetadataControllerTest {

  @Autowired CapabilityStatementProperties capabilityStatementProperties;

  @Autowired CapabilityResourcesProperties capabilityResourcesProperties;

  @Test
  @SneakyThrows
  public void read() {
    MetadataController controller =
        new MetadataController(capabilityStatementProperties, capabilityResourcesProperties);
    controller.afterPropertiesSet();
    CapabilityStatement old = null;
    try {
      old =
          JacksonConfig.createMapper()
              .readValue(
                  getClass().getResourceAsStream("/capability.json"), CapabilityStatement.class);
    } catch (IOException e) {
      System.out.println("Couldn't load capability.json into an object");
      e.printStackTrace();
    }
    try {
      assertThat(prettyJson(controller.read(Optional.empty()))).isEqualTo(prettyJson(old));
    } catch (AssertionError e) {
      System.out.println(e.getMessage());
      throw e;
    }
    try {
      assertThat(
              prettyJson(
                  controller.read(
                      Optional.of(MetadataCapabilityStatementModeEnum.FULL.getParameter()))))
          .isEqualTo(prettyJson(old));
    } catch (AssertionError e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  // Loads our properties file into a CapabilityStatementProperties bean that we can use
  @EnableConfigurationProperties(value = CapabilityStatementProperties.class)
  public static class TestConfiguration {}
}
