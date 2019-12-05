package gov.va.api.health.informational.stu3.wellknown;

import gov.va.api.health.informational.stu3.capability.CapabilityStatementProperties;
import gov.va.api.health.stu3.api.information.WellKnown;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class WellKnownUtilities {

  /**
   * Initialize a WellKnown with content provided.
   *
   * @param capabilityStatementProperties Configured capability properties.
   * @param wellKnownProperties Configured wellknown properties.
   * @return WellKnown.
   */
  public static WellKnown initializeWellKnownBuilder(
      CapabilityStatementProperties capabilityStatementProperties,
      WellKnownProperties wellKnownProperties) {
    WellKnown.WellKnownBuilder wellKnownBuilder =
        WellKnown.builder()
            .authorizationEndpoint(
                capabilityStatementProperties.getSecurity().getAuthorizeEndpoint())
            .tokenEndpoint(capabilityStatementProperties.getSecurity().getTokenEndpoint());
    if (wellKnownProperties != null) {
      if ((wellKnownProperties.getCapabilities() != null)
          && !wellKnownProperties.getCapabilities().isEmpty()) {
        wellKnownBuilder.capabilities(wellKnownProperties.getCapabilities());
      }
      if ((wellKnownProperties.getResponseTypeSupported() != null)
          && !wellKnownProperties.getResponseTypeSupported().isEmpty()) {
        wellKnownBuilder.responseTypeSupported(wellKnownProperties.getResponseTypeSupported());
      }
      if ((wellKnownProperties.getScopesSupported() != null)
          && !wellKnownProperties.getScopesSupported().isEmpty()) {
        wellKnownBuilder.scopesSupported(wellKnownProperties.getScopesSupported());
      }
    }
    return wellKnownBuilder.build();
  }
}
