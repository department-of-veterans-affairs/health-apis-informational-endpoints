package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.CapabilityStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Standard enum for our APIs which can be added to as warranted.
 *
 * <p>Note however, that if different mappings are necessary, an API may use it's own enumeration
 * with desired implementation of SearchParamsEnumInterface.
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum StandardSearchParamsEnum implements SearchParamsEnumInterface {
  PATIENT("patient", CapabilityStatement.SearchParamType.reference);
  private final String param;
  private final CapabilityStatement.SearchParamType type;
}
