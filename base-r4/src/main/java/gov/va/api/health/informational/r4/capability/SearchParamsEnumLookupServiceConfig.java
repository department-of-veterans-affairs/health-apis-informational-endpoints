package gov.va.api.health.informational.r4.capability;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Configuration class to facilitate extension or override of the StandardSearchParamsEnum via the
 * SearchParamsEnumLookupService.
 */
@Data
@Builder
public class SearchParamsEnumLookupServiceConfig implements InitializingBean {

  private List<SearchParamsEnumInterface> searchParamsEnumList;

  @Override
  public void afterPropertiesSet() throws IllegalArgumentException {
    Assert.notNull(
        searchParamsEnumList,
        "SearchParamsEnumLookupServiceConfig searchParamsEnumList must not be null.");
    Assert.notEmpty(
        searchParamsEnumList,
        "SearchParamsEnumLookupServiceConfig searchParamsEnumList must not be empty.");
  }
}
