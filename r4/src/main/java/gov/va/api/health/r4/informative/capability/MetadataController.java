package gov.va.api.health.r4.informative.capability;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import gov.va.api.health.r4.api.datatypes.CodeableConcept;
import gov.va.api.health.r4.api.datatypes.Coding;
import gov.va.api.health.r4.api.datatypes.ContactDetail;
import gov.va.api.health.r4.api.datatypes.ContactPoint;
import gov.va.api.health.r4.api.datatypes.ContactPoint.ContactPointSystem;
import gov.va.api.health.r4.api.elements.Extension;
import gov.va.api.health.r4.api.resources.Capability;
import gov.va.api.health.r4.api.resources.Capability.CapabilityResource;
import gov.va.api.health.r4.api.resources.Capability.Rest;
import gov.va.api.health.r4.api.resources.Capability.RestMode;
import gov.va.api.health.r4.api.resources.Capability.SearchParamType;
import gov.va.api.health.r4.api.resources.Capability.Security;
import gov.va.api.health.r4.api.resources.Capability.Software;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
  value = {"/metadata"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@AllArgsConstructor
public abstract class MetadataController {

  private final CapabilityStatementProperties properties;

  private final List<CapabilityResource> resources;

  private List<ContactDetail> contact() {
    return singletonList(
        ContactDetail.builder()
            .name(properties.getContact().getName())
            .telecom(
                singletonList(
                    ContactPoint.builder()
                        .system(ContactPointSystem.email)
                        .value(properties.getContact().getEmail())
                        .build()))
            .build());
  }

  @GetMapping
  protected Capability read() {
    return Capability.builder()
        .resourceType("Capability")
        .id(properties.getId())
        .version(properties.getVersion())
        .status(properties.getStatus())
        .name(properties.getName())
        .publisher(properties.getPublisher())
        .contact(contact())
        .date(properties.getPublicationDate())
        .description(properties.getDescription())
        .kind(properties.getKind())
        .software(software())
        .fhirVersion(properties.getFhirVersion())
        // This can also be a property if we ever need to make it configurable
        .format(asList("application/json+fhir", "application/json", "application/fhir+json"))
        .rest(rest())
        .build();
  }

  private List<Rest> rest() {
    return singletonList(
        Rest.builder().mode(RestMode.server).security(restSecurity()).resource(resources).build());
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
                                .valueUri(properties.getSecurity().getTokenEndpoint())
                                .build(),
                            Extension.builder()
                                .url("authorize")
                                .valueUri(properties.getSecurity().getAuthorizeEndpoint())
                                .build()))
                    .build()))
        .cors("true")
        .service(singletonList(smartOnFhirCodeableConcept()))
        .description(properties.getSecurity().getDescription())
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
    return Software.builder().name(properties.getSoftwareName()).build();
  }

  @Getter
  @AllArgsConstructor
  // TODO: How is this going to be updated, should PATIENT be removed and apps can extend
  // SearchParam as needed?
  enum SearchParam {
    PATIENT("patient", SearchParamType.reference);

    private final String param;

    private final SearchParamType type;
  }
}
