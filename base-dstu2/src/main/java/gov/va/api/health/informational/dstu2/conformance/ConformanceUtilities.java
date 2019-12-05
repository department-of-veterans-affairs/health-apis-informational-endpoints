package gov.va.api.health.informational.dstu2.conformance;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import gov.va.api.health.dstu2.api.datatypes.CodeableConcept;
import gov.va.api.health.dstu2.api.datatypes.Coding;
import gov.va.api.health.dstu2.api.datatypes.ContactPoint;
import gov.va.api.health.dstu2.api.elements.Extension;
import gov.va.api.health.dstu2.api.resources.Conformance;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ConformanceUtilities {

  /**
   * Conformance Software description.
   *
   * @return Conformance Software.
   */
  private static Conformance.Software capabilitySoftware(
      ConformanceStatementProperties capabilityStatementProperties) {
    return Conformance.Software.builder()
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
  private static List<Conformance.Contact> contact(
      ConformanceStatementProperties capabilityStatementProperties) {
    Conformance.Contact.ContactBuilder contactDetailBuilder =
        Conformance.Contact.builder().name(capabilityStatementProperties.getContact().getName());
    if ((capabilityStatementProperties.getContact().getEmail() != null)
        && !capabilityStatementProperties.getContact().getEmail().isBlank()) {
      contactDetailBuilder.telecom(
          singletonList(
              ContactPoint.builder()
                  .system(ContactPoint.ContactPointSystem.email)
                  .value(capabilityStatementProperties.getContact().getEmail())
                  .build()));
    }
    return singletonList(contactDetailBuilder.build());
  }

  /**
   * Initialize a Conformance (Statement) with content that is the same for full and normative
   * modes.
   *
   * @param resourceType The resource type string to populate within the statement.
   * @param capabilityStatementProperties Configured capability properties.
   * @param resourcesProperties Configured resources properties.
   * @return Conformance (Statement).
   */
  public static Conformance initializeConformanceBuilder(
      final String resourceType,
      ConformanceStatementProperties capabilityStatementProperties,
      ConformanceResourcesProperties resourcesProperties) {
    Conformance.ConformanceBuilder capabilityBuilder =
        Conformance.builder()
            .resourceType(resourceType)
            .id(capabilityStatementProperties.getId())
            .date(capabilityStatementProperties.getPublicationDate())
            .kind(Conformance.Kind.capability)
            .software(capabilitySoftware(capabilityStatementProperties))
            .fhirVersion(capabilityStatementProperties.getFhirVersion())
            .acceptUnknown(Conformance.AcceptUnknown.no)
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
   * Technically FHIR optional but implemented as required. Description of RESTful endpoints. TODO:
   * Consider making more configurable and making more configurable and supporting multiple
   * endpoints.
   *
   * @return List of Rest endpoints.
   */
  private static List<Conformance.Rest> rest(
      ConformanceStatementProperties capabilityStatementProperties,
      ConformanceResourcesProperties resourcesProperties) {
    return singletonList(
        Conformance.Rest.builder()
            .mode(Conformance.RestMode.server)
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
  private static Conformance.RestSecurity restSecurity(
      ConformanceStatementProperties capabilityStatementProperties) {
    return Conformance.RestSecurity.builder()
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
        .cors(true)
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
                    .system("https://www.hl7.org/fhir/restful-security-service")
                    .code("SMART-on-FHIR")
                    .display("SMART-on-FHIR")
                    .build()))
        .build();
  }
}
