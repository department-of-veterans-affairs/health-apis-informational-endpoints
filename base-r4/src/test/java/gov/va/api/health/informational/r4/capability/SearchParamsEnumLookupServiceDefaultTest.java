package gov.va.api.health.informational.r4.capability;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/** Test use of the SearchParamsEnumLookupService without a configuration. */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SearchParamsEnumLookupService.class})
public class SearchParamsEnumLookupServiceDefaultTest {

  @Autowired private SearchParamsEnumLookupService lookupService;

  /** Test the SearchParamsEnumLookupService will work without a config. */
  @Test
  public void searchParamsEnumServiceNoConfigTest() {
    assertEquals(
        StandardSearchParamsEnum.PATIENT, lookupService.getEnum(StandardSearchParamsEnum.PATIENT));
  }
}
