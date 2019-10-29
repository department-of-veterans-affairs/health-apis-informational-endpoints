package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.Capability;
import gov.va.api.health.r4.api.resources.Capability.Kind;
import gov.va.api.health.r4.api.resources.Capability.Status;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("DefaultAnnotationParam")
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("capability")
@Data
@Accessors(fluent = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

  @Autowired private List<Capability.CapabilityResource> resourcesToSupport;

  @Data
  @Accessors(fluent = false)
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ContactProperties {

    private String name;

    private String email;
  }

  @Data
  @Accessors(fluent = false)
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SecurityProperties {

    private String tokenEndpoint;

    private String authorizeEndpoint;

    private String description;
  }
}
