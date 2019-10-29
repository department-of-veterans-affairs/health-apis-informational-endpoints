package gov.va.api.health.informational.r4.wellknown;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties;
import gov.va.api.health.r4.api.information.WellKnown;
import lombok.SneakyThrows;
import org.junit.Test;

public class WellKnownControllerTest {

  private WellKnown actual() {
    return WellKnown.builder()
        .tokenEndpoint("https://test.lighthouse.va.gov/token")
        .authorizationEndpoint("https://test.lighthouse.va.gov/authorize")
        .capabilities(
            asList(
                "context-standalone-patient",
                "launch-standalone",
                "permission-offline",
                "permission-patient"))
        .responseTypeSupported(asList("code", "refresh-token"))
        .scopesSupported(asList("patient/Test.read", "launch/patient", "offline_access"))
        .build();
  }

  private CapabilityStatementProperties conformanceProperties() {
    return CapabilityStatementProperties.builder()
        .security(
            CapabilityStatementProperties.SecurityProperties.builder()
                .authorizeEndpoint("https://test.lighthouse.va.gov/authorize")
                .tokenEndpoint("https://test.lighthouse.va.gov/token")
                .build())
        .build();
  }

  @SneakyThrows
  private String pretty(WellKnown wellKnown) {
    return JacksonConfig.createMapper()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(wellKnown);
  }

  @Test
  @SneakyThrows
  public void read() {
    TestWellKnownController controller =
        new TestWellKnownController(wellKnownProperties(), conformanceProperties());
    try {
      assertThat(pretty(controller.read())).isEqualTo(pretty(actual()));
    } catch (AssertionError e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  private WellKnownProperties wellKnownProperties() {
    return WellKnownProperties.builder()
        .capabilities(
            asList(
                "context-standalone-patient",
                "launch-standalone",
                "permission-offline",
                "permission-patient"))
        .responseTypeSupported(asList("code", "refresh-token"))
        .scopesSupported(asList("patient/Test.read", "launch/patient", "offline_access"))
        .build();
  }

  private class TestWellKnownController extends WellKnownController {

    public TestWellKnownController(
        WellKnownProperties wellKnownProperties,
        CapabilityStatementProperties capabilityStatementProperties) {
      super(wellKnownProperties, capabilityStatementProperties);
    }
  }
}