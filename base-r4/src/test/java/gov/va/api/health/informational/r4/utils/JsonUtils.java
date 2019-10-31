package gov.va.api.health.informational.r4.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;

public class JsonUtils {
  public static String prettyJson(Object value) {
    String pretty = "";
    try {
      pretty =
          JacksonConfig.createMapper().writerWithDefaultPrettyPrinter().writeValueAsString(value);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return pretty;
  }
}
