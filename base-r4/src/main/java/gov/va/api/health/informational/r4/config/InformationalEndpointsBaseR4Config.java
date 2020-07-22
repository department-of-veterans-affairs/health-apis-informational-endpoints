package gov.va.api.health.informational.r4.config;

import gov.va.api.health.informational.r4.capability.CapabilityStatementProperties;
import gov.va.api.health.informational.r4.capability.MetadataController;
import gov.va.api.health.informational.r4.wellknown.WellKnownController;
import gov.va.api.health.informational.r4.wellknown.WellKnownProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This is a convenience class meant to import everything you'd need to support a capability
 * statement under /metadata and a well-known endpoint under /.well-known/smart-configuration.
 * Importing them piecemeal can still be done if desired.
 */
@Configuration
@Import({
  WellKnownController.class,
  CapabilityStatementProperties.class,
  WellKnownProperties.class,
  MetadataController.class
})
public class InformationalEndpointsBaseR4Config {}
