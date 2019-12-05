package gov.va.api.health.informational.dstu2.wellknown;

import gov.va.api.health.dstu2.api.information.WellKnown;
import gov.va.api.health.informational.dstu2.conformance.ConformanceStatementProperties;
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
      ConformanceStatementProperties capabilityStatementProperties,
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
