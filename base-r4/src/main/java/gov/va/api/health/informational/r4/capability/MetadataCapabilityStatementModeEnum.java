package gov.va.api.health.informational.r4.capability;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration to map a Metadata endpoint mode parameter value and the resource type of the returned
 * CapabilityStatement. TODO: Should not resource type "Capability" be "CapabilityStatement" to
 * fulfill FHIR specification? Until confirmation of one way or the other the resource type string
 * has been left as "Capability" for backward compatibility.
 */
@Getter
@AllArgsConstructor
public enum MetadataCapabilityStatementModeEnum {
  FULL("full", "Capability"),
  NORMATIVE("normative", "Capability"),
  TERMINOLOGY("terminology", "TerminologyCapabilities");
  private final String parameter;
  private final String resourceType;

  /**
   * Enum from parameter. If not recognized, FULL is returned.
   *
   * @param parameter Parameter value.
   * @return Enum or FULL if not found.
   */
  public static MetadataCapabilityStatementModeEnum fromParameter(final String parameter) {
    for (MetadataCapabilityStatementModeEnum e : MetadataCapabilityStatementModeEnum.values()) {
      if (String.valueOf(e.parameter).equalsIgnoreCase(parameter)) {
        return e;
      }
    }
    return FULL;
  }
}
