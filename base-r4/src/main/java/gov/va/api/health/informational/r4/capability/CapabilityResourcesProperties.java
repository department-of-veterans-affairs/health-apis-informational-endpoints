package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.CapabilityStatement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CapabilityResourcesProperties {
  private List<CapabilityStatement.CapabilityResource> resourcesToSupport;
}
