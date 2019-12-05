package gov.va.api.health.informational.stu3.capability;

import gov.va.api.health.stu3.api.resources.CapabilityStatement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CapabilityResourcesProperties {
  private List<CapabilityStatement.RestResource> resourcesToSupport;
}
