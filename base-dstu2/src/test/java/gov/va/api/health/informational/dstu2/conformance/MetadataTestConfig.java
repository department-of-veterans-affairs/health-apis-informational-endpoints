package gov.va.api.health.informational.dstu2.conformance;

import java.util.Collections;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MetadataTestConfig {
  @Bean
  public ConformanceResourcesProperties capabilityResourcesProperties() {
    return ConformanceResourcesProperties.builder()
        .resourcesToSupport(Collections.emptyList())
        .build();
  }
}
