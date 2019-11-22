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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
  value = {"/metadata"},
  produces = {"application/json", "application/json+fhir", "application/fhir+json"}
)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MetadataController implements InitializingBean {

  private final CapabilityStatementProperties capabilityStatementProperties;

  private final CapabilityResourcesProperties resourcesProperties;

  private Capability capability;

  @Override
  public void afterPropertiesSet() throws Exception {
    Capability.CapabilityBuilder capabilityBuilder =
        Capability.builder()
            .resourceType("Capability")
            .id(capabilityStatementProperties.getId())
            .status(capabilityStatementProperties.getStatus())
            .date(capabilityStatementProperties.getPublicationDate())
            .kind(capabilityStatementProperties.getKind())
            .fhirVersion(capabilityStatementProperties.getFhirVersion())
            .software(software())
            .format(asList("application/json+fhir", "application/json", "application/fhir+json"))
            .rest(rest());
    // Version is optional.
    if ((capabilityStatementProperties.getVersion() != null)
        && !capabilityStatementProperties.getVersion().isBlank()) {
      capabilityBuilder.version(capabilityStatementProperties.getVersion());
    }
    // Name is optional.
    if ((capabilityStatementProperties.getName() != null)
        && !capabilityStatementProperties.getName().isBlank()) {
      capabilityBuilder.name(capabilityStatementProperties.getName());
    }
    // Publisher is optional.
    if ((capabilityStatementProperties.getPublisher() != null)
        && !capabilityStatementProperties.getPublisher().isBlank()) {
      capabilityBuilder.publisher(capabilityStatementProperties.getPublisher());
    }
    // Contact is optional.
    if (capabilityStatementProperties.getContact() != null) {
      capabilityBuilder.contact(contact());
    }
    // Description is optional.
    if ((capabilityStatementProperties.getDescription() != null)
        && !capabilityStatementProperties.getDescription().isBlank()) {
      capabilityBuilder.description(capabilityStatementProperties.getDescription());
    }
    capability = capabilityBuilder.build();
  }

  /**
   * Software Contact(s).
   *
   * <p>TODO: Currently implemented to support only one contact. Consider making more configurable
   * and supporting multiple contacts.
   *
   * @return List of Contact Detail.
   */
  private List<ContactDetail> contact() {
    ContactDetail.ContactDetailBuilder contactDetailBuilder =
        ContactDetail.builder().name(capabilityStatementProperties.getContact().getName());
    if ((capabilityStatementProperties.getContact().getEmail() != null)
        && !capabilityStatementProperties.getContact().getEmail().isBlank()) {
      contactDetailBuilder.telecom(
          singletonList(
              ContactPoint.builder()
                  .system(ContactPointSystem.email)
                  .value(capabilityStatementProperties.getContact().getEmail())
                  .build()));
    }
    return singletonList(contactDetailBuilder.build());
  }

  /**
   * This is provided in case you'd like to return metadata from an endpoint not provided by
   * default.
   *
   * <p>NOTE: Some, but not all optional fields are supported but can be added as necessary. See:
   * http://hl7.org/fhir/r4/capabilitystatement.html
   *
   * @return Capability statement of how to use a FHIR server.
   */
  @GetMapping
  public Capability read() {
    return capability;
  }

  /**
   * Technically FHIR optional but implemented as required. Description of RESTful endpoints. TODO:
   * Consider making more configurable and supporting multiple endpoints.
   *
   * @return List of Rest endpoints.
   */
  private List<Rest> rest() {
    return singletonList(
        Rest.builder()
            .mode(RestMode.server)
            .security(restSecurity())
            .resource(resourcesProperties.getResourcesToSupport())
            .build());
  }

  /**
   * Security description for RESTful endpoint. Technically optional but implemented as if required
   * for endpoint. TODO: Consider making more configurable.
   *
   * @return Security description.
   */
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

  /**
   * Smart on FHIR codeable concept. TODO: Consider making configurable.
   *
   * @return CodeableConcept.
   */
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

  /**
   * Software description.
   *
   * @return Software.
   */
  private Software software() {
    return Software.builder().name(capabilityStatementProperties.getSoftwareName()).build();
  }
}
