package gov.va.api.health.informational.r4.capability;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import gov.va.api.health.r4.api.resources.Capability;
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
  Set<MetadataController.SearchParam> search;

  /**
   * Convenience for building a lot of SupportedResources for a CapabilityStatement. Used in stream
   * pattern found in test.
   *
   * @return CapabilityResource to show as supported
   */
  public Capability.CapabilityResource asResource() {
    return Capability.CapabilityResource.builder()
        .type(type)
        .interaction(interactions())
        .searchParam(searchParams())
        .profile(profile)
        .build();
  }

  private List<Capability.ResourceInteraction> interactions() {
    if (search.isEmpty()) {
      return singletonList(readable());
    }
    return asList(searchable(), readable());
  }

  private Capability.ResourceInteraction readable() {
    return Capability.ResourceInteraction.builder()
        .code(Capability.TypeRestfulInteraction.read)
        .documentation(documentation)
        .build();
  }

  private List<Capability.SearchParam> searchParams() {
    if (search.isEmpty()) {
      return null;
    }
    return search
        .stream()
        .map(s -> Capability.SearchParam.builder().name(s.param()).type(s.type()).build())
        .collect(Collectors.toList());
  }

  private Capability.ResourceInteraction searchable() {
    return Capability.ResourceInteraction.builder()
        .code(Capability.TypeRestfulInteraction.search_type)
        .documentation(documentation)
        .build();
  }
}
