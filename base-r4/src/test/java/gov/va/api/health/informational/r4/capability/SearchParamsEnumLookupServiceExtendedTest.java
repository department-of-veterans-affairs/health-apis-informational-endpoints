package gov.va.api.health.informational.r4.capability;

import static org.junit.Assert.assertEquals;

import gov.va.api.health.r4.api.resources.Capability;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/** Test use of the SearchParamsEnumLookupService with an extended configuration. */
@RunWith(SpringRunner.class)
@ContextConfiguration(
  classes = {
    SearchParamsEnumLookupServiceExtendedTest.TestConfiguration.class,
    SearchParamsEnumLookupService.class
  }
)
public class SearchParamsEnumLookupServiceExtendedTest {

  @Autowired private SearchParamsEnumLookupService lookupService;

  /** Test the default standardized API enum and extended enum values. */
  @Test
  public void searchParamsEnumServiceExtendedConfigTest() {
    assertEquals(
        StandardSearchParamsEnum.PATIENT, lookupService.getEnum(StandardSearchParamsEnum.PATIENT));
    assertEquals(
        ExtendedSearchParamsEnum.DOCTOR, lookupService.getEnum(ExtendedSearchParamsEnum.DOCTOR));
    assertEquals(
        ExtendedSearchParamsEnum.INSURANCE,
        lookupService.getEnum(ExtendedSearchParamsEnum.INSURANCE));
  }

  /**
   * Enumeration that extends the StandardSearchParamsEnum via the SearchParamsEnumLookupService.
   */
  @Getter
  @AllArgsConstructor
  @Accessors(fluent = true)
  public enum ExtendedSearchParamsEnum implements SearchParamsEnumInterface {
    DOCTOR("doctor", Capability.SearchParamType.special),
    INSURANCE("insurance", Capability.SearchParamType.quantity);

    private final String param;

    private final Capability.SearchParamType type;
  }

  /** Create a SearchParamsEnumLookupServiceConfig with the specified enum. */
  public static class TestConfiguration {
    @Bean
    public SearchParamsEnumLookupServiceConfig searchParamsEnumLookupServiceConfig() {
      return SearchParamsEnumLookupServiceConfig.builder()
          .searchParamsEnumList(Arrays.asList(ExtendedSearchParamsEnum.values()))
          .build();
    }
  }
}
