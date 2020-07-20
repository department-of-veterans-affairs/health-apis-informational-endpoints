package gov.va.api.health.informational.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {
  private static final YAMLMapper MAPPER = new YAMLMapper();

  private final Resource openapi;

  @Autowired
  public OpenApiController(@Value("classpath:/openapi.yaml") Resource openapi) {
    this.openapi = openapi;
  }

  /** The OpenAPI specific content in yaml form. */
  @SuppressWarnings("WeakerAccess")
  @Bean
  public String openapiContent() throws IOException {
    try (InputStream is = openapi.getInputStream()) {
      return StreamUtils.copyToString(is, Charset.defaultCharset());
    }
  }

  /**
   * Provide access to the OpenAPI as JSON via RESTful interface. This is also used as the /
   * redirect. This doesn't use the actual json file that we can produce. TODO: come back and see if
   * serving the openapi.json directly is any different
   */
  @GetMapping(
      value = {"/openapi.json", "/api/openapi.json"},
      produces = "application/json")
  @ResponseBody
  public String openapiJson() throws IOException {
    Object obj = OpenApiController.MAPPER.readValue(openapiContent(), Object.class);
    ObjectMapper jsonWriter = new ObjectMapper();
    return jsonWriter.writeValueAsString(obj);
  }

  /** Provide access to the OpenAPI yaml via RESTful interface. */
  @GetMapping(
      value = {"/openapi.yaml", "/api/openapi.yaml"},
      produces = "application/vnd.oai.openapi")
  @ResponseBody
  public String openapiYaml() throws IOException {
    return openapiContent();
  }
}
