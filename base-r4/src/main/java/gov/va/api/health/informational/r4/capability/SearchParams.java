package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.Capability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
// TODO: How is this going to be updated, what other forms can this take for easier extensibility
public enum SearchParams {
  PATIENT("patient", Capability.SearchParamType.reference);

  private final String param;

  private final Capability.SearchParamType type;
}
