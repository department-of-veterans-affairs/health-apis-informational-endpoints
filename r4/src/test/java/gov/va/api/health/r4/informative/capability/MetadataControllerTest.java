package gov.va.api.health.r4.informative.capability;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.r4.api.resources.Capability;
import gov.va.api.health.r4.api.resources.Capability.Kind;
import gov.va.api.health.r4.api.resources.Capability.Status;
import gov.va.api.health.r4.informative.capability.CapabilityStatementProperties.ContactProperties;
import gov.va.api.health.r4.informative.capability.CapabilityStatementProperties.SecurityProperties;
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
        .build();
  }

  @Test
  @SneakyThrows
  public void read() {
    CapabilityStatementProperties properties = properties();

    List<Capability.CapabilityResource> resources =
        Stream.of(
                MetadataController.SupportedResource.builder()
                    .type("Search By Patient Service")
                    .searchBy(MetadataController.SearchParam.PATIENT)
                    .profile("https://fhir.com/r4/test.html")
                    .documentation(properties.getResourceDocumentation())
                    .build(),
                MetadataController.SupportedResource.builder()
                    .type("Read Only Service")
                    .profile("https://fhir.com/r4/test.html")
                    .documentation(properties.getResourceDocumentation())
                    .build())
            .map(MetadataController.SupportedResource::asResource)
            .collect(Collectors.toList());

    MetadataController controller = new MetadataController(properties, resources);
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
}
