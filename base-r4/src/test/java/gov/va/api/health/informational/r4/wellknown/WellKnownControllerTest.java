package gov.va.api.health.informational.r4.wellknown;

import static gov.va.api.health.informational.r4.utils.JsonUtils.prettyJson;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties;
import gov.va.api.health.r4.api.information.WellKnown;
import java.io.IOException;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WellKnownControllerTest.TestConfiguration.class})
@TestPropertySource("/application.properties")
public class WellKnownControllerTest {

  @Autowired CapabilityStatementProperties capabilityStatementProperties;
  @Autowired WellKnownProperties wellKnownProperties;

  /**
   * TODO: Making these tests more generic would be cool considering the only difference is class to
   * interpret and type of controller
   */
  @Test
  @SneakyThrows
  public void read() {
    WellKnownController controller =
        new WellKnownController(wellKnownProperties, capabilityStatementProperties);
    controller.afterPropertiesSet();
    WellKnown old = null;
    try {
      old =
          JacksonConfig.createMapper()
              .readValue(getClass().getResourceAsStream("/well-known.json"), WellKnown.class);
    } catch (IOException e) {
      System.out.println("Couldn't load well-known.json into an object");
      e.printStackTrace();
    }
    try {
      assertThat(prettyJson(controller.read())).isEqualTo(prettyJson(old));
    } catch (AssertionError e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @EnableConfigurationProperties(
      value = {CapabilityStatementProperties.class, WellKnownProperties.class})
  public static class TestConfiguration {}
}
