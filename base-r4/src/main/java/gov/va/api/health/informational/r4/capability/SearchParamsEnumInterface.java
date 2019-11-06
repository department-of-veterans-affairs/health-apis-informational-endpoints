package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.Capability;

/** Interface definition for SearchParam enumerations. */
public interface SearchParamsEnumInterface {
  public String param();

  public Capability.SearchParamType type();
}
