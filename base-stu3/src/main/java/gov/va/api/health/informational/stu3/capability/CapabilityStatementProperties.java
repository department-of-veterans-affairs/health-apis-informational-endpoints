package gov.va.api.health.informational.stu3.capability;

import gov.va.api.health.stu3.api.resources.CapabilityStatement;
import java.util.Optional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * Configuration for Metadata (Capability Statement) controller settings.
 *
 * <p>NOTE: Some optional properties are not set/supported by current implementation.
 */
@Configuration
@ConfigurationProperties("capability")
@Data
@Slf4j
public class CapabilityStatementProperties implements InitializingBean {

  /** Required. */
  private String id;

  /** Optional. Business version. */
  private String version;

  /** Optional. Computer friendly name. */
  private String name;

  /** Required. Publication status. */
  private CapabilityStatement.Status status;

  /** Required. Date last changed. */
  private String publicationDate;

  /** Optional. Name of publisher (organization or individual). */
  private String publisher;

  /** Optional. Natural language description. */
  private String description;

  /** Required. FHIR Version supported. */
  private String fhirVersion;

  /**
   * Required for Capability resource type which is how this configuration class is currently used.
   * A name the software is known by.
   */
  private String softwareName;

  /**
   * Optional. Contact details for the publisher. TODO: For FHIR specification this should be a list
   * but currently sufficient.
   */
  private ContactProperties contact;

  /**
   * Required for current implementation (but technically optional for FHIR). Security properties
   * for rest. TODO: For FHIR specification this should be a list but currently sufficient.
   */
  private SecurityProperties security;

  @Override
  public void afterPropertiesSet() throws IllegalArgumentException {
    // Required properties check.
    Assert.notNull(id, "CapabilityStatementProperties id must not be null.");
    Assert.notNull(status, "CapabilityStatementProperties status must not be null.");
    Assert.hasText(
        publicationDate,
        "CapabilityStatementProperties publicationDate must not be null or empty.");
    Assert.hasText(
        fhirVersion, "CapabilityStatementProperties fhirVersion must not be null or empty.");
    Assert.hasText(
        softwareName, "CapabilityStatementProperties softwareName must not be null or empty.");
    Assert.notNull(security, "CapabilityStatementProperties security must not be null.");
    security.afterPropertiesSet();
    // Optional properties check.
    if (contact != null) {
      contact.afterPropertiesSet();
    } else {
      log.warn("CapabilityStatementProperties contact is not set.");
    }
    if ((version == null) || version.isEmpty()) {
      log.warn("CapabilityStatementProperties version is not set.");
    }
    if ((name == null) || name.isEmpty()) {
      log.warn("CapabilityStatementProperties name is not set.");
    }
    if ((publisher == null) || publisher.isEmpty()) {
      log.warn("CapabilityStatementProperties publisher is not set.");
    }
    if ((description == null) || description.isEmpty()) {
      log.warn("CapabilityStatementProperties description is not set.");
    }
  }

  /**
   * Configuration class for FHIR ContactDetail. TODO: This is incorrect in regard to FHIR
   * specification but currently sufficient.
   */
  @Data
  public static class ContactProperties implements InitializingBean {

    /** Required. */
    private String name;

    /** Optional. */
    private String email;

    @Override
    public void afterPropertiesSet() throws IllegalArgumentException {
      Assert.hasText(name, "ContactProperties name must not be null or empty.");
      if ((email == null) || email.isEmpty()) {
        log.warn("ContactProperties email is not set.");
      }
    }
  }

  /** Rest security properties. TODO: This could be made more configurable. */
  @Data
  public static class SecurityProperties implements InitializingBean {

    /** Required. */
    private String tokenEndpoint;

    /** Required. */
    private String authorizeEndpoint;

    /** Required. */
    private String description;

    /** Optional. */
    private Optional<String> managementEndpoint = Optional.ofNullable(null);

    /** Optional. */
    private Optional<String> revocationEndpoint = Optional.ofNullable(null);

    @Override
    public void afterPropertiesSet() throws IllegalArgumentException {
      Assert.hasText(tokenEndpoint, "SecurityProperties tokenEndpoint must not be null or empty.");
      Assert.hasText(
          authorizeEndpoint, "SecurityProperties authorizeEndpoint must not be null or empty.");
      Assert.hasText(description, "SecurityProperties description must not be null or empty.");
    }
  }
}
