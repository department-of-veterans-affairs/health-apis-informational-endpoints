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

/** Test use of the SearchParamsEnumLookupService with an extended and overridden configuration. */
@RunWith(SpringRunner.class)
@ContextConfiguration(
  classes = {
    SearchParamsEnumLookupServiceOverrideTest.TestConfiguration.class,
    SearchParamsEnumLookupService.class
  }
)
public class SearchParamsEnumLookupServiceOverrideTest {

  @Autowired private SearchParamsEnumLookupService lookupService;

  /** Test the default standardized API enum is overridden. */
  @Test
  public void searchParamsEnumServiceOverrideConfigTest() {
    assertEquals(
        SearchParamsOverrideEnum.PATIENT, lookupService.getEnum(StandardSearchParamsEnum.PATIENT));
    assertEquals(
        SearchParamsOverrideEnum.PATIENT, lookupService.getEnum(SearchParamsOverrideEnum.PATIENT));
    assertEquals(
        SearchParamsOverrideEnum.DOCTOR, lookupService.getEnum(SearchParamsOverrideEnum.DOCTOR));
  }

  /**
   * Enumeration that extends the StandardSearchParamsEnum via the SearchParamsEnumLookupService
   * with a DOCTOR mapping and overrides the PATIENT to just a number.
   */
  @Getter
  @AllArgsConstructor
  @Accessors(fluent = true)
  public enum SearchParamsOverrideEnum implements SearchParamsEnumInterface {
    DOCTOR("doctor", Capability.SearchParamType.special),
    PATIENT("patient", Capability.SearchParamType.number);

    private final String param;

    private final Capability.SearchParamType type;
  }

  /** Create a SearchParamsEnumLookupServiceConfig with the specified enum. */
  public static class TestConfiguration {
    @Bean
    public SearchParamsEnumLookupServiceConfig searchParamsEnumLookupServiceConfig() {
      return SearchParamsEnumLookupServiceConfig.builder()
          .searchParamsEnumList(Arrays.asList(SearchParamsOverrideEnum.values()))
          .build();
    }
  }
}
