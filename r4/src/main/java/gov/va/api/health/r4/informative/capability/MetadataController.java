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
import gov.va.api.health.r4.api.resources.Capability.ResourceInteraction;
import gov.va.api.health.r4.api.resources.Capability.Rest;
import gov.va.api.health.r4.api.resources.Capability.RestMode;
import gov.va.api.health.r4.api.resources.Capability.SearchParamType;
import gov.va.api.health.r4.api.resources.Capability.Security;
import gov.va.api.health.r4.api.resources.Capability.Software;
import gov.va.api.health.r4.api.resources.Capability.TypeRestfulInteraction;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;
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
class MetadataController {

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
  Capability read() {
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

  @Value
  @Builder
  public static class SupportedResource {

    String type;

    String profile;

    String documentation;

    @Singular("searchBy")
    Set<SearchParam> search;

    CapabilityResource asResource() {
      return CapabilityResource.builder()
          .type(type)
          .interaction(interactions())
          .searchParam(searchParams())
          .profile(profile)
          .build();
    }

    private List<ResourceInteraction> interactions() {
      if (search.isEmpty()) {
        return singletonList(readable());
      }
      return asList(searchable(), readable());
    }

    private ResourceInteraction readable() {
      return ResourceInteraction.builder()
          .code(TypeRestfulInteraction.read)
          .documentation(documentation)
          .build();
    }

    private List<Capability.SearchParam> searchParams() {
      if (search.isEmpty()) {
        return null;
      }
      return search
          .stream()
          .map(s -> Capability.SearchParam.builder().name(s.param()).type(s.type()).build())
          .collect(Collectors.toList());
    }

    private ResourceInteraction searchable() {
      return ResourceInteraction.builder()
          .code(TypeRestfulInteraction.search_type)
          .documentation(documentation)
          .build();
    }
  }
}
