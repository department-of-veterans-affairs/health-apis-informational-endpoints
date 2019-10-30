package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.Capability.Kind;
import gov.va.api.health.r4.api.resources.Capability.Status;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("capability")
@Data
public class CapabilityStatementProperties {

  private String id;

  private String version;

  private String name;

  private Status status;

  private String publicationDate;

  private String publisher;

  private String description;

  private Kind kind;

  private String fhirVersion;

  private String softwareName;

  private String resourceDocumentation;

  private ContactProperties contact;

  private SecurityProperties security;

  @Data
  public static class ContactProperties {

    private String name;

    private String email;
  }

  @Data
  public static class SecurityProperties {

    private String tokenEndpoint;

    private String authorizeEndpoint;

    private String description;
  }
}