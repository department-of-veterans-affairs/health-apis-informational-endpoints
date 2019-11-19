package gov.va.api.health.informational.r4.wellknown;

import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties;
import gov.va.api.health.r4.api.information.WellKnown;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
  value = {".well-known/smart-configuration"},
  produces = {"application/json", "application/fhir+json", "application/json+fhir"}
)
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class WellKnownController {
  private final WellKnownProperties wellKnownProperties;
  private final CapabilityStatementProperties capabilityStatementProperties;

  /**
   * This is provided in case you'd like to return well-known from an endpoint not provided by
   * default.
   *
   * <p>NOTE: Some, but not all optional fields are supported but can be added as necessary. See:
   * http://www.hl7.org/fhir/smart-app-launch/conformance/index.html#using-well-known
   *
   * @return JSON file of basic security properties
   */
  @GetMapping
  public WellKnown read() {
    WellKnown.WellKnownBuilder wellKnownBuilder =
        WellKnown.builder()
            .authorizationEndpoint(
                capabilityStatementProperties.getSecurity().getAuthorizeEndpoint())
            .tokenEndpoint(capabilityStatementProperties.getSecurity().getTokenEndpoint())
            .capabilities(wellKnownProperties.getCapabilities());
    if ((wellKnownProperties.getResponseTypeSupported() != null)
        && !wellKnownProperties.getResponseTypeSupported().isEmpty()) {
      wellKnownBuilder.responseTypeSupported(wellKnownProperties.getResponseTypeSupported());
    }
    if ((wellKnownProperties.getScopesSupported() != null)
        && !wellKnownProperties.getScopesSupported().isEmpty()) {
      wellKnownBuilder.scopesSupported(wellKnownProperties.getScopesSupported());
    }
    return wellKnownBuilder.build();
  }
}
