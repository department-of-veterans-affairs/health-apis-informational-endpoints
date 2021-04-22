package gov.va.api.health.informational.openapi;

import static java.util.stream.Collectors.toMap;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OpenApiUtilities {
  private static Components components(OpenApiProperties openApiProperties) {
    if (openApiProperties.getComponents() == null
        || openApiProperties.getComponents().getSecurityScheme() == null) {
      return null;
    }
    var maps =
        openApiProperties.getComponents().getSecurityScheme().entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> securityScheme(e.getValue())));

    Components components = new Components();
    components.securitySchemes(maps);
    return components;
  }

  private static ExternalDocumentation externalDocumentation(OpenApiProperties openApiProperties) {
    if (openApiProperties.getExternalDocs() == null) {
      return null;
    }
    ExternalDocumentation externalDocs = new ExternalDocumentation();
    externalDocs.description(openApiProperties.getExternalDocs().getDescription());
    externalDocs.url(openApiProperties.getExternalDocs().getUrl());
    return externalDocs;
  }

  private static Info info(OpenApiProperties openApiProperties) {
    Info info = new Info();
    info.title(openApiProperties.getTitle());
    info.description(openApiProperties.getDescription());
    info.version(openApiProperties.getVersion());
    return info;
  }

  /**
   * Initialize a OpenAPI definition.
   *
   * @param openApiProperties Configured OpenAPI properties.
   * @return OpenAPI.
   */
  public static OpenAPI initializeOpenApi(OpenApiProperties openApiProperties) {
    var openapi = new OpenAPI();
    openapi.info(info(openApiProperties));
    openapi.externalDocs(externalDocumentation(openApiProperties));
    openapi.servers(servers(openApiProperties));
    openapi.components(components(openApiProperties));
    return openapi;
  }

  private static SecurityScheme securityScheme(
      OpenApiProperties.SecurityScheme openApiSecurityScheme) {
    OAuthFlow implicitFlow = new OAuthFlow();
    implicitFlow.authorizationUrl(openApiSecurityScheme.getAuthorizationUrl());
    implicitFlow.tokenUrl(openApiSecurityScheme.getTokenUrl());
    implicitFlow.scopes(new Scopes());
    OAuthFlows flows = new OAuthFlows();
    flows.implicit(implicitFlow);
    SecurityScheme securityScheme = new SecurityScheme();
    securityScheme.type(openApiSecurityScheme.getType());
    securityScheme.in(openApiSecurityScheme.getIn());
    securityScheme.flows(flows);
    return securityScheme;
  }

  private static List<Server> servers(OpenApiProperties openApiProperties) {
    if (openApiProperties.getServer() == null) {
      return null;
    }
    Server server = new Server();
    server.url(openApiProperties.getServer().getUrl());
    server.description(openApiProperties.getServer().getDescription());
    return new ArrayList<>(List.of(server));
  }
}
