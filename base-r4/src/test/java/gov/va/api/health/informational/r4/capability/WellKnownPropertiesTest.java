package gov.va.api.health.informational.r4.capability;

import gov.va.api.health.informational.r4.wellknown.WellKnownProperties;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/** Exercise afterPropertiesSet of WellKnownProperties to increase code coverage. */
public class WellKnownPropertiesTest {

  /** Don't set optional fields so warnings are logged. */
  @Test
  public void minimumConfigTest() {
    final List<String> capabilitiesList = Arrays.asList("permission-dev");
    // Build and validate to get warnings.
    WellKnownProperties.builder().capabilities(capabilitiesList).build().afterPropertiesSet();
  }
}
