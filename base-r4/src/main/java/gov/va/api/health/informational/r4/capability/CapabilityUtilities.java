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
import gov.va.api.health.r4.api.resources.TerminologyCapabilities;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CapabilityUtilities {

  /**
   * Capability Software description.
   *
   * @return Capability Software.
   */
  private static Capability.Software capabilitySoftware(
      CapabilityStatementProperties capabilityStatementProperties) {
    return Capability.Software.builder()
        .name(capabilityStatementProperties.getSoftwareName())
        .build();
  }

  /**
   * Software Contact(s).
   *
   * <p>TODO: Currently implemented to support only one contact. Consider making more configurable
   * and supporting multiple contacts.
   *
   * @return List of Contact Detail.
   */
  private static List<ContactDetail> contact(
      CapabilityStatementProperties capabilityStatementProperties) {
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
   * Initialize a Capability (Statement) with content that is the same for full and normative modes.
   *
   * @param resourceType The resource type string to populate within the statement.
   * @param capabilityStatementProperties Configured capability properties.
   * @param resourcesProperties Configured resources properties.
   * @return Capability (Statement).
   */
  public static Capability initializeCapabilityBuilder(
      final String resourceType,
      CapabilityStatementProperties capabilityStatementProperties,
      CapabilityResourcesProperties resourcesProperties) {
    Capability.CapabilityBuilder capabilityBuilder =
        Capability.builder()
            .resourceType(resourceType)
            .id(capabilityStatementProperties.getId())
            .status(capabilityStatementProperties.getStatus())
            .date(capabilityStatementProperties.getPublicationDate())
            .kind(capabilityStatementProperties.getKind())
            .software(capabilitySoftware(capabilityStatementProperties))
            .fhirVersion(capabilityStatementProperties.getFhirVersion())
            .format(asList("application/json+fhir", "application/json", "application/fhir+json"))
            .rest(rest(capabilityStatementProperties, resourcesProperties));
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
      capabilityBuilder.contact(contact(capabilityStatementProperties));
    }
    // Description is optional.
    if ((capabilityStatementProperties.getDescription() != null)
        && !capabilityStatementProperties.getDescription().isBlank()) {
      capabilityBuilder.description(capabilityStatementProperties.getDescription());
    }
    return capabilityBuilder.build();
  }

  /**
   * Initialize a TerminologyCapabilities with content for terminology mode.
   *
   * @param resourceType The resource type string to populate within the statement.
   * @param capabilityStatementProperties Configured capability properties.
   * @return TerminologyCapabilities.
   */
  public static TerminologyCapabilities initializeTerminologyCapabilitiesBuilder(
      final String resourceType, CapabilityStatementProperties capabilityStatementProperties) {
    TerminologyCapabilities.TerminologyCapabilitiesBuilder terminologyCapabilitiesBuilder =
        TerminologyCapabilities.builder()
            .resourceType(resourceType)
            .id(capabilityStatementProperties.getId())
            .status(capabilityStatementProperties.getStatus())
            .date(capabilityStatementProperties.getPublicationDate())
            .kind(capabilityStatementProperties.getKind())
            .software(terminologyCapabilitiesSoftware(capabilityStatementProperties));
    // Version is optional.
    if ((capabilityStatementProperties.getVersion() != null)
        && !capabilityStatementProperties.getVersion().isBlank()) {
      terminologyCapabilitiesBuilder.version(capabilityStatementProperties.getVersion());
    }
    // Name is optional.
    if ((capabilityStatementProperties.getName() != null)
        && !capabilityStatementProperties.getName().isBlank()) {
      terminologyCapabilitiesBuilder.name(capabilityStatementProperties.getName());
    }
    // Publisher is optional.
    if ((capabilityStatementProperties.getPublisher() != null)
        && !capabilityStatementProperties.getPublisher().isBlank()) {
      terminologyCapabilitiesBuilder.publisher(capabilityStatementProperties.getPublisher());
    }
    // Contact is optional.
    if (capabilityStatementProperties.getContact() != null) {
      terminologyCapabilitiesBuilder.contact(contact(capabilityStatementProperties));
    }
    // Description is optional.
    if ((capabilityStatementProperties.getDescription() != null)
        && !capabilityStatementProperties.getDescription().isBlank()) {
      terminologyCapabilitiesBuilder.description(capabilityStatementProperties.getDescription());
    }
    return terminologyCapabilitiesBuilder.build();
  }

  /**
   * Technically FHIR optional but implemented as required. Description of RESTful endpoints. TODO:
   * Consider making more configurable and making more configurable and supporting multiple
   * endpoints.
   *
   * @return List of Rest endpoints.
   */
  private static List<Rest> rest(
      CapabilityStatementProperties capabilityStatementProperties,
      CapabilityResourcesProperties resourcesProperties) {
    return singletonList(
        Rest.builder()
            .mode(RestMode.server)
            .security(restSecurity(capabilityStatementProperties))
            .resource(resourcesProperties.getResourcesToSupport())
            .build());
  }

  /**
   * Security description for RESTful endpoint. Technically optional but implemented as if required
   * for endpoint. TODO: Consider making more configurable.
   *
   * @return Security description.
   */
  private static Security restSecurity(
      CapabilityStatementProperties capabilityStatementProperties) {
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
  private static CodeableConcept smartOnFhirCodeableConcept() {
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
   * Terminology Capabilities Software description.
   *
   * @return Terminology Capabilities Software.
   */
  private static TerminologyCapabilities.Software terminologyCapabilitiesSoftware(
      CapabilityStatementProperties capabilityStatementProperties) {
    return TerminologyCapabilities.Software.builder()
        .name(capabilityStatementProperties.getSoftwareName())
        .build();
  }
}
