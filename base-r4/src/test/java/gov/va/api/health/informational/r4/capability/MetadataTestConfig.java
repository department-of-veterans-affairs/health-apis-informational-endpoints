package gov.va.api.health.informational.r4.capability;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MetadataTestConfig {
  @Bean
  public CapabilityResourcesProperties capabilityResourcesProperties() {
    return CapabilityResourcesProperties.builder()
        .resourcesToSupport(
            Stream.of(
                    SupportedResource.builder()
                        .type("Search By Patient Service")
                        .searchBy(StandardSearchParamsEnum.PATIENT)
                        .profile("https://fhir.com/r4/test.html")
                        .documentation(
                            "Implemented per specification. This is configurable. See http://hl7.org/fhir/R4/http.html")
                        .build(),
                    SupportedResource.builder()
                        .type("Read Only Service")
                        .profile("https://fhir.com/r4/test.html")
                        .documentation(
                            "Implemented per specification. Also configurable. See http://hl7.org/fhir/R4/http.html")
                        .build())
                .map(SupportedResource::asResource)
                .collect(Collectors.toList()))
        .build();
  }
}
