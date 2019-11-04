package gov.va.api.health.informational.r4.capability;

import java.util.Collections;
import org.junit.Test;

/** Test bad SearchParamsEnumLookupServiceConfig use cases. */
public class SearchParamsEnumLookupServiceFailedConfigTest {

  /** Test the config list cannot be null. */
  @Test(expected = IllegalArgumentException.class)
  public void searchParamsEnumServiceEmptyConfigTest() {
    SearchParamsEnumLookupServiceConfig.builder()
        .searchParamsEnumList(Collections.emptyList())
        .build()
        .afterPropertiesSet();
  }

  /** Test the config list cannot be null. */
  @Test(expected = IllegalArgumentException.class)
  public void searchParamsEnumServiceNullConfigTest() {
    SearchParamsEnumLookupServiceConfig.builder()
        .searchParamsEnumList(null)
        .build()
        .afterPropertiesSet();
  }
}
