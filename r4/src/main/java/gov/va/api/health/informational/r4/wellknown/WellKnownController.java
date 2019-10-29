package gov.va.api.health.informational.r4.wellknown;

import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties;
import gov.va.api.health.r4.api.information.WellKnown;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
public abstract class WellKnownController {
  private final WellKnownProperties wellKnownProperties;
  private final CapabilityStatementProperties capabilityStatementProperties;

  @GetMapping
  protected WellKnown read() {
    return WellKnown.builder()
        .authorizationEndpoint(capabilityStatementProperties.getSecurity().getAuthorizeEndpoint())
        .tokenEndpoint(capabilityStatementProperties.getSecurity().getTokenEndpoint())
        .capabilities(wellKnownProperties.getCapabilities())
        .responseTypeSupported(wellKnownProperties.getResponseTypeSupported())
        .scopesSupported(wellKnownProperties.getScopesSupported())
        .build();
  }
}
