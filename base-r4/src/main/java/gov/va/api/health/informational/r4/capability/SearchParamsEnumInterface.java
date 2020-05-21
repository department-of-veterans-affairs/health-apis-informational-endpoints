package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.CapabilityStatement;

/** Interface definition for SearchParam enumerations. */
public interface SearchParamsEnumInterface {
  public String param();

  public CapabilityStatement.SearchParamType type();
}
