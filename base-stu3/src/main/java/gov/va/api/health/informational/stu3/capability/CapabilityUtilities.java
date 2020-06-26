package gov.va.api.health.informational.stu3.capability;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import gov.va.api.health.stu3.api.datatypes.CodeableConcept;
import gov.va.api.health.stu3.api.datatypes.Coding;
import gov.va.api.health.stu3.api.datatypes.ContactDetail;
import gov.va.api.health.stu3.api.datatypes.ContactPoint;
import gov.va.api.health.stu3.api.elements.Extension;
import gov.va.api.health.stu3.api.resources.CapabilityStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CapabilityUtilities {

  /**
   * CapabilityStatement Software description.
   *
   * @return CapabilityStatement Software.
   */
  private static CapabilityStatement.Software capabilitySoftware(
      CapabilityStatementProperties capabilityStatementProperties) {
    return CapabilityStatement.Software.builder()
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
                  .system(ContactPoint.ContactPointSystem.email)
                  .value(capabilityStatementProperties.getContact().getEmail())
                  .build()));
    }
    return singletonList(contactDetailBuilder.build());
  }

  private static List<Extension> extentionsFromSecurity(
      CapabilityStatementProperties.SecurityProperties security) {
    List<Extension> extentions =
        new ArrayList<Extension>(
            asList(
                Extension.builder().url("token").valueUri(security.getTokenEndpoint()).build(),
                Extension.builder()
                    .url("authorize")
                    .valueUri(security.getAuthorizeEndpoint())
                    .build()));
    security
        .getManagementEndpoint()
        .ifPresent(val -> extentions.add(Extension.builder().url("manage").valueUri(val).build()));
    security
        .getRevocationEndpoint()
        .ifPresent(val -> extentions.add(Extension.builder().url("revoke").valueUri(val).build()));
    return Collections.unmodifiableList(extentions);
  }

  /**
   * Initialize a CapabilityStatement (Statement) with content that is the same for full and
   * normative modes.
   *
   * @param resourceType The resource type string to populate within the statement.
   * @param capabilityStatementProperties Configured capability properties.
   * @param resourcesProperties Configured resources properties.
   * @return CapabilityStatement (Statement).
   */
  public static CapabilityStatement initializeCapabilityBuilder(
      final String resourceType,
      CapabilityStatementProperties capabilityStatementProperties,
      CapabilityResourcesProperties resourcesProperties) {
    CapabilityStatement.CapabilityStatementBuilder capabilityBuilder =
        CapabilityStatement.builder()
            .resourceType(resourceType)
            .id(capabilityStatementProperties.getId())
            .status(capabilityStatementProperties.getStatus())
            .date(capabilityStatementProperties.getPublicationDate())
            .kind(CapabilityStatement.Kind.capability)
            .software(capabilitySoftware(capabilityStatementProperties))
            .fhirVersion(capabilityStatementProperties.getFhirVersion())
            .acceptUnknown(CapabilityStatement.AcceptUnknown.no)
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
  private static List<CapabilityStatement.Rest> rest(
      CapabilityStatementProperties capabilityStatementProperties,
      CapabilityResourcesProperties resourcesProperties) {
    return singletonList(
        CapabilityStatement.Rest.builder()
            .mode(CapabilityStatement.RestMode.server)
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
  private static CapabilityStatement.RestSecurity restSecurity(
      CapabilityStatementProperties capabilityStatementProperties) {
    return CapabilityStatement.RestSecurity.builder()
        .extension(
            singletonList(
                Extension.builder()
                    .url("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris")
                    .extension(extentionsFromSecurity(capabilityStatementProperties.getSecurity()))
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
