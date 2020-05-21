package gov.va.api.health.informational.r4.capability;

import static org.junit.Assert.assertEquals;

import gov.va.api.health.r4.api.resources.CapabilityStatement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.junit.Test;

/** Test we can use a custom enumerations. */
public class SupportedResourceTest {

  /** Test custom enum. */
  @Test
  public void customEnumTest() {
    SupportedResource supportedResource =
        SupportedResource.builder()
            .type("Search with Custom Enum")
            .searchBy(SearchParamsCustomEnum.DOCTOR)
            .searchBy(SearchParamsCustomEnum.PATIENT)
            .profile("https://fhir.com/r4/test.html")
            .documentation(
                "Implemented per specification. This is configurable. See http://hl7.org/fhir/R4/http.html")
            .build();
    CapabilityStatement.CapabilityResource capabilityResource = supportedResource.asResource();
    List<CapabilityStatement.SearchParam> searchParamsList = capabilityResource.searchParam();
    assertEquals(2, searchParamsList.size());
    assertEquals(SearchParamsCustomEnum.DOCTOR.param(), searchParamsList.get(0).name());
    assertEquals(SearchParamsCustomEnum.DOCTOR.type(), searchParamsList.get(0).type());
    assertEquals(SearchParamsCustomEnum.PATIENT.param(), searchParamsList.get(1).name());
    assertEquals(SearchParamsCustomEnum.PATIENT.type(), searchParamsList.get(1).type());
  }

  /** Enumeration with a DOCTOR mapping and changes the PATIENT to just a number. */
  @Getter
  @AllArgsConstructor
  @Accessors(fluent = true)
  public enum SearchParamsCustomEnum implements SearchParamsEnumInterface {
    DOCTOR("doctor", CapabilityStatement.SearchParamType.special),
    PATIENT("patient", CapabilityStatement.SearchParamType.number);

    private final String param;

    private final CapabilityStatement.SearchParamType type;
  }
}
