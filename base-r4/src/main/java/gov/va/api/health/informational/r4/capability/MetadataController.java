package gov.va.api.health.informational.r4.capability;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import gov.va.api.health.r4.api.datatypes.CodeableConcept;
import gov.va.api.health.r4.api.datatypes.Coding;
import gov.va.api.health.r4.api.datatypes.ContactDetail;
import gov.va.api.health.r4.api.datatypes.ContactPoint;
import gov.va.api.health.r4.api.datatypes.ContactPoint.ContactPointSystem;
import gov.va.api.health.r4.api.elements.Extension;
import gov.va.api.health.r4.api.resources.Capability;
import gov.va.api.health.r4.api.resources.Capability.Rest;
import gov.va.api.health.r4.api.resources.Capability.RestMode;
import gov.va.api.health.r4.api.resources.Capability.Security;
import gov.va.api.health.r4.api.resources.Capability.Software;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
  value = {"/metadata"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class MetadataController {

  private final CapabilityStatementProperties capabilityStatementProperties;

  private final CapabilityResourcesProperties resourcesProperties;

  private List<ContactDetail> contact() {
    return singletonList(
        ContactDetail.builder()
            .name(capabilityStatementProperties.getContact().getName())
            .telecom(
                singletonList(
                    ContactPoint.builder()
                        .system(ContactPointSystem.email)
                        .value(capabilityStatementProperties.getContact().getEmail())
                        .build()))
            .build());
  }

  /**
   * This is provided in case you'd like to return metadata from an endpoint not provided by
   * default.
   *
   * @return Capability statement of how to use a FHIR server
   */
  @GetMapping
  public Capability read() {
    return Capability.builder()
        .resourceType("Capability")
        .id(capabilityStatementProperties.getId())
        .version(capabilityStatementProperties.getVersion())
        .status(capabilityStatementProperties.getStatus())
        .name(capabilityStatementProperties.getName())
        .publisher(capabilityStatementProperties.getPublisher())
        .contact(contact())
        .date(capabilityStatementProperties.getPublicationDate())
        .description(capabilityStatementProperties.getDescription())
        .kind(capabilityStatementProperties.getKind())
        .software(software())
        .fhirVersion(capabilityStatementProperties.getFhirVersion())
        // This can also be a property if we ever need to make it configurable
        .format(asList("application/json+fhir", "application/json", "application/fhir+json"))
        .rest(rest())
        .build();
  }

  private List<Rest> rest() {
    return singletonList(
        Rest.builder()
            .mode(RestMode.server)
            .security(restSecurity())
            .resource(resourcesProperties.getResourcesToSupport())
            .build());
  }

  private Security restSecurity() {
    return Security.builder()
        .extension(
            singletonList(
                Extension.builder()
                    .url("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris")
                    .extension(
                        asList(
                            Extension.builder()
                                .url("token")
                                .valueUri(
                                    capabilityStatementProperties.getSecurity().getTokenEndpoint())
                                .build(),
                            Extension.builder()
                                .url("authorize")
                                .valueUri(
                                    capabilityStatementProperties
                                        .getSecurity()
                                        .getAuthorizeEndpoint())
                                .build()))
                    .build()))
        .cors("true")
        .service(singletonList(smartOnFhirCodeableConcept()))
        .description(capabilityStatementProperties.getSecurity().getDescription())
        .build();
  }

  private CodeableConcept smartOnFhirCodeableConcept() {
    return CodeableConcept.builder()
        .coding(
            singletonList(
                Coding.builder()
                    .system("https://www.hl7.org/fhir/valueset-restful-security-service.html")
                    .code("SMART-on-FHIR")
                    .display("SMART-on-FHIR")
                    .build()))
        .build();
  }

  private Software software() {
    return Software.builder().name(capabilityStatementProperties.getSoftwareName()).build();
  }
}
