package gov.va.api.health.informational.r4.capability;

import java.util.HashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Optional Service to facilitate SearchParamEnum mapping for standardized reuse or extension.
 *
 * <p>To use this service, 1) import this class, 2) create an enum that implements the
 * SearchParamsEnumInterface, and 3) create a bean of type SearchParamsEnumLookupServiceConfig.
 *
 * <p>Note this service is only necessary if the StandardSearchParamsEnum needs to be extended.
 */
@Slf4j
@Service
@NoArgsConstructor
public class SearchParamsEnumLookupService implements InitializingBean {

  @Autowired(required = false)
  private SearchParamsEnumLookupServiceConfig config;

  /** Map of lookup search parameter mapping based on enumerated values. */
  private Map<SearchParamsEnumInterface, SearchParamsEnumInterface> searchParamMap =
      new HashMap<>();

  @Override
  public void afterPropertiesSet() throws IllegalArgumentException {
    // Initialize the service with the default standardized enum.
    for (StandardSearchParamsEnum param : StandardSearchParamsEnum.values()) {
      searchParamMap.put(param, param);
    }
    // Optionally xtend the search parameter enum mapping.
    if (config != null) {
      extendEnum();
    }
  }

  /** Extend or override standard SearchParams enumeration. */
  private void extendEnum() {
    for (SearchParamsEnumInterface enumVal : config.getSearchParamsEnumList()) {
      // Override default enumeration mapping.
      for (StandardSearchParamsEnum param : StandardSearchParamsEnum.values()) {
        if (param.param().equals(enumVal.param())) {
          // Log a warning if a default mapping is overridden.
          log.warn(
              "Overridding default mapping of ["
                  + param.param()
                  + ":"
                  + param.type()
                  + "] to ["
                  + enumVal.type()
                  + "].");
          searchParamMap.put(param, enumVal);
          break;
        }
      }
      // Add the enum to the list.
      searchParamMap.put(enumVal, enumVal);
    }
  }

  /**
   * Get the search parameter configuration mapping by enum.
   *
   * @param enumVal The enum value to lookup.
   * @return The mapped search parameter enum or if not found the original enum.
   */
  public SearchParamsEnumInterface getEnum(SearchParamsEnumInterface enumVal) {
    if (searchParamMap.containsKey(enumVal)) {
      return searchParamMap.get(enumVal);
    }
    log.warn("WARNING: Enum {} not mapped.  Check configuration.", enumVal);
    return enumVal;
  }
}
