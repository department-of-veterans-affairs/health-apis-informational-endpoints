package gov.va.api.health.informational.r4.capability;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties.ContactProperties;
import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties.SecurityProperties;
import gov.va.api.health.r4.api.resources.Capability;
import gov.va.api.health.r4.api.resources.Capability.Kind;
import gov.va.api.health.r4.api.resources.Capability.Status;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.Test;

public class MetadataControllerTest {

  @SneakyThrows
  private String pretty(Capability capability) {
    return JacksonConfig.createMapper()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(capability);
  }

  private CapabilityStatementProperties properties() {
    return CapabilityStatementProperties.builder()
        .id("health-api-test-service")
        .version("2.0.0")
        .status(Status.draft)
        .name("API Management Platform | Health - Test Service")
        .publisher("Department of Veterans Affairs")
        .contact(
            ContactProperties.builder().name("Test Person").email("test.person@va.gov").build())
        .publicationDate("2019-10-25T19:34:29Z")
        .description("Capability service meant for testing only.")
        .kind(Kind.instance)
        .softwareName("test-service")
        .fhirVersion("4.0.0")
        .resourceDocumentation(
            "Implemented per specification. See http://hl7.org/fhir/R4/http.html")
        .security(
            SecurityProperties.builder()
                .tokenEndpoint("https://example.com/oauth2/token")
                .authorizeEndpoint("https://example.com/oauth2/authorization")
                .description("http://docs.smarthealthit.org/")
                .build())
        .resourcesToSupport(resources())
        .build();
  }

  @Test
  @SneakyThrows
  public void read() {
    MetadataController controller = new MetadataController(properties());
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

  private List<Capability.CapabilityResource> resources() {
    return Stream.of(
            SupportedResource.builder()
                .type("Search By Patient Service")
                .searchBy(MetadataController.SearchParam.PATIENT)
                .profile("https://fhir.com/r4/test.html")
                .documentation(
                    "Implemented per specification. This is configurable. See http://hl7.org/fhir/R4/http.html")
                .build(),
            SupportedResource.builder()
                .type("Read Only Service")
                .profile("https://fhir.com/r4/test.html")
                .documentation(
                    "Implemented per specification. Also configurable. See http://hl7.org/fhir/R4/http.html")
                .build())
        .map(SupportedResource::asResource)
        .collect(Collectors.toList());
  }
}
