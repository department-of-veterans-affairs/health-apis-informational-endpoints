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
  private static Conformance.Software conformanceSoftware(
      ConformanceStatementProperties conformanceStatementProperties) {
    return Conformance.Software.builder()
        .name(conformanceStatementProperties.getSoftwareName())
        .build();
  }

  /**
   * Software Contact(s).
   *
   * <p>TODO: Currently implemented to support only one contact. Consider making more configurable
   * and supporting multiple contacts.
   *
   * @param conformanceStatementProperties Configured conformance properties.
   * @return List of Contact Detail.
   */
  private static List<Conformance.Contact> contact(
      ConformanceStatementProperties conformanceStatementProperties) {
    Conformance.Contact.ContactBuilder contactDetailBuilder =
        Conformance.Contact.builder().name(conformanceStatementProperties.getContact().getName());
    if ((conformanceStatementProperties.getContact().getEmail() != null)
        && !conformanceStatementProperties.getContact().getEmail().isBlank()) {
      contactDetailBuilder.telecom(
          singletonList(
              ContactPoint.builder()
                  .system(ContactPoint.ContactPointSystem.email)
                  .value(conformanceStatementProperties.getContact().getEmail())
                  .build()));
    }
    return singletonList(contactDetailBuilder.build());
  }

  /**
   * Initialize a Conformance (Statement) with content that is the same for full and normative
   * modes.
   *
   * @param resourceType The resource type string to populate within the statement.
   * @param conformanceStatementProperties Configured conformance properties.
   * @param resourcesProperties Configured resources properties.
   * @return Conformance (Statement).
   */
  public static Conformance initializeConformanceBuilder(
      final String resourceType,
      ConformanceStatementProperties conformanceStatementProperties,
      ConformanceResourcesProperties resourcesProperties) {
    Conformance.ConformanceBuilder conformanceBuilder =
        Conformance.builder()
            .resourceType(resourceType)
            .id(conformanceStatementProperties.getId())
            .date(conformanceStatementProperties.getPublicationDate())
            .kind(Conformance.Kind.capability)
            .software(conformanceSoftware(conformanceStatementProperties))
            .fhirVersion(conformanceStatementProperties.getFhirVersion())
            .acceptUnknown(Conformance.AcceptUnknown.no)
            .format(asList("application/json+fhir", "application/json", "application/fhir+json"))
            .rest(rest(conformanceStatementProperties, resourcesProperties));
    // Version is optional.
    if ((conformanceStatementProperties.getVersion() != null)
        && !conformanceStatementProperties.getVersion().isBlank()) {
      conformanceBuilder.version(conformanceStatementProperties.getVersion());
    }
    // Name is optional.
    if ((conformanceStatementProperties.getName() != null)
        && !conformanceStatementProperties.getName().isBlank()) {
      conformanceBuilder.name(conformanceStatementProperties.getName());
    }
    // Publisher is optional.
    if ((conformanceStatementProperties.getPublisher() != null)
        && !conformanceStatementProperties.getPublisher().isBlank()) {
      conformanceBuilder.publisher(conformanceStatementProperties.getPublisher());
    }
    // Contact is optional.
    if (conformanceStatementProperties.getContact() != null) {
      conformanceBuilder.contact(contact(conformanceStatementProperties));
    }
    // Description is optional.
    if ((conformanceStatementProperties.getDescription() != null)
        && !conformanceStatementProperties.getDescription().isBlank()) {
      conformanceBuilder.description(conformanceStatementProperties.getDescription());
    }
    return conformanceBuilder.build();
  }

  /**
   * Technically FHIR optional but implemented as required. Description of RESTful endpoints. TODO:
   * Consider making more configurable and making more configurable and supporting multiple
   * endpoints.
   *
   * @param conformanceStatementProperties Configured conformance properties.
   * @param resourcesProperties Configured resources properties.
   * @return List of Rest endpoints.
   */
  private static List<Conformance.Rest> rest(
      ConformanceStatementProperties conformanceStatementProperties,
      ConformanceResourcesProperties resourcesProperties) {
    return singletonList(
        Conformance.Rest.builder()
            .mode(Conformance.RestMode.server)
            .security(restSecurity(conformanceStatementProperties))
            .resource(resourcesProperties.getResourcesToSupport())
            .build());
  }

  /**
   * Security description for RESTful endpoint. Technically optional but implemented as if required
   * for endpoint. TODO: Consider making more configurable.
   *
   * @param conformanceStatementProperties Configured conformance properties.
   * @return Security description.
   */
  private static Conformance.RestSecurity restSecurity(
      ConformanceStatementProperties conformanceStatementProperties) {
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
                                    conformanceStatementProperties.getSecurity().getTokenEndpoint())
                                .build(),
                            Extension.builder()
                                .url("authorize")
                                .valueUri(
                                    conformanceStatementProperties
                                        .getSecurity()
                                        .getAuthorizeEndpoint())
                                .build(),
                            Extension.builder()
                                .url("management")
                                .valueUri(
                                    conformanceStatementProperties
                                        .getSecurity()
                                        .getManagementEndpoint())
                                .build(),
                            Extension.builder()
                                .url("revocation")
                                .valueUri(
                                    conformanceStatementProperties
                                        .getSecurity()
                                        .getRevocationEndpoint())
                                .build()))
                    .build()))
        .cors(true)
        .service(singletonList(smartOnFhirCodeableConcept()))
        .description(conformanceStatementProperties.getSecurity().getDescription())
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
                    .system("http://hl7.org/fhir/restful-security-service")
                    .code("SMART-on-FHIR")
                    .display("SMART-on-FHIR")
                    .build()))
        .build();
  }
}
