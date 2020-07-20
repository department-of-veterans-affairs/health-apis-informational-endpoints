package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.r4.api.resources.CapabilityStatement;
import gov.va.api.health.r4.api.resources.CapabilityStatement.Rest;
import gov.va.api.health.r4.api.resources.Resource;
import gov.va.api.health.r4.api.resources.TerminologyCapabilities;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = {"/metadata"},
    produces = {"application/json", "application/json+fhir", "application/fhir+json"})
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MetadataController implements InitializingBean {

  private final CapabilityStatementProperties capabilityStatementProperties;

  private final CapabilityResourcesProperties resourcesProperties;

  private CapabilityStatement capability;

  private CapabilityStatement normative;

  private TerminologyCapabilities terminology;

  @Override
  public void afterPropertiesSet() throws Exception {
    // Full Capability Statement.
    capability =
        CapabilityUtilities.initializeCapabilityStatementBuilder(
            MetadataCapabilityStatementModeEnum.FULL.getResourceType(),
            capabilityStatementProperties,
            resourcesProperties);
    // Normative Statement is the same as a full CapabilityStatement with trial-use portions
    // removed.
    normative =
        CapabilityUtilities.initializeCapabilityStatementBuilder(
            MetadataCapabilityStatementModeEnum.NORMATIVE.getResourceType(),
            capabilityStatementProperties,
            resourcesProperties);
    normative.useContext(null);
    normative.imports(null);
    if (normative.implementation() != null) {
      normative.implementation().custodian(null);
    }
    if (normative.rest() != null) {
      for (Rest rest : normative.rest()) {
        rest.security(null);
        if (rest.resource() != null) {
          for (CapabilityStatement.CapabilityResource r : rest.resource()) {
            r.supportedProfile(null);
            r.versioning(null);
            r.readHistory(null);
            r.updateCreate(null);
            r.conditionalCreate(null);
            r.conditionalRead(null);
            r.conditionalUpdate(null);
            r.conditionalDelete(null);
            r.referencePolicy(null);
            r.searchInclude(null);
            r.searchRevInclude(null);
          }
        }
      }
    }
    normative.messaging(null);
    normative.document(null);
    // Terminology Statement.
    terminology =
        CapabilityUtilities.initializeTerminologyCapabilitiesBuilder(
            MetadataCapabilityStatementModeEnum.TERMINOLOGY.getResourceType(),
            capabilityStatementProperties);
  }

  /**
   * This is provided in case you'd like to return metadata from an endpoint not provided by
   * default.
   *
   * <p>NOTE: Some, but not all optional fields are supported but can be added as necessary. See:
   * http://hl7.org/fhir/r4/capabilitystatement.html
   *
   * @param mode Optional mode parameter to specify response resource type.
   * @return CapabilityStatement of how to use a FHIR server.
   */
  @GetMapping
  public Resource read(@RequestParam("mode") Optional<String> mode) {
    // If not specified or unrecognized mode just return regular capability statement.
    MetadataCapabilityStatementModeEnum modeEnum = MetadataCapabilityStatementModeEnum.FULL;
    if (mode.isPresent()) {
      modeEnum = MetadataCapabilityStatementModeEnum.fromParameter(mode.get());
    }
    switch (modeEnum) {
      case FULL:
        return capability;
      case NORMATIVE:
        return normative;
      case TERMINOLOGY:
        return terminology;
      default:
        return capability;
    }
  }
}
