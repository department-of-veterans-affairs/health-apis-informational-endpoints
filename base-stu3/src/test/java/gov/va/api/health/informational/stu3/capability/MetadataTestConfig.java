package gov.va.api.health.informational.stu3.capability;

import java.util.Collections;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MetadataTestConfig {
  @Bean
  public CapabilityResourcesProperties capabilityResourcesProperties() {
    return CapabilityResourcesProperties.builder()
        .resourcesToSupport(Collections.emptyList())
        .build();
  }
}
