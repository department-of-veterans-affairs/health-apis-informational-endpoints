package gov.va.api.health.informational.r4.capability;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import gov.va.api.health.r4.api.resources.CapabilityStatement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class SupportedResource {

  String type;

  String profile;

  String documentation;

  @Singular("searchBy")
  Set<SearchParamsEnumInterface> search;

  /**
   * Convenience for building a lot of SupportedResources for a CapabilityStatementStatement. Used
   * in stream pattern found in test.
   *
   * @return CapabilityResource to show as supported
   */
  public CapabilityStatement.CapabilityResource asResource() {
    return CapabilityStatement.CapabilityResource.builder()
        .type(type)
        .interaction(interactions())
        .searchParam(searchParams())
        .profile(profile)
        .build();
  }

  private List<CapabilityStatement.ResourceInteraction> interactions() {
    if (search.isEmpty()) {
      return singletonList(readable());
    }
    return asList(searchable(), readable());
  }

  private CapabilityStatement.ResourceInteraction readable() {
    return CapabilityStatement.ResourceInteraction.builder()
        .code(CapabilityStatement.TypeRestfulInteraction.read)
        .documentation(documentation)
        .build();
  }

  private List<CapabilityStatement.SearchParam> searchParams() {
    if (search.isEmpty()) {
      return null;
    }
    return search.stream()
        .map(s -> CapabilityStatement.SearchParam.builder().name(s.param()).type(s.type()).build())
        .collect(Collectors.toList());
  }

  private CapabilityStatement.ResourceInteraction searchable() {
    return CapabilityStatement.ResourceInteraction.builder()
        .code(CapabilityStatement.TypeRestfulInteraction.search_type)
        .documentation(documentation)
        .build();
  }
}
