package gov.va.api.health.informational.dstu2.conformance;

import gov.va.api.health.dstu2.api.resources.Conformance;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConformanceResourcesProperties {
  private List<Conformance.RestResource> resourcesToSupport;
}
