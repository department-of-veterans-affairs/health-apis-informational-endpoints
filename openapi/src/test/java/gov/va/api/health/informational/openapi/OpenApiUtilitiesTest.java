package gov.va.api.health.informational.openapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {OpenApiUtilitiesTest.TestConfiguration.class},
    initializers = ConfigFileApplicationContextInitializer.class)
public class OpenApiUtilitiesTest {
  @Autowired OpenApiProperties openApiProperties;

  private static OpenApiProperties minimumProperties() {
    OpenApiProperties properties = new OpenApiProperties();
    properties.setTitle("TITLE");
    properties.setDescription("DESCRIPTION");
    properties.setVersion("V1");
    return properties;
  }

  @Test
  @SneakyThrows
  public void openApiInitializer() {
    OpenAPI openapi = OpenApiUtilities.initializeOpenApi(openApiProperties);
    assertThat(openapi.getInfo().getTitle()).isEqualTo("My Title");
    assertThat(openapi.getInfo().getDescription()).isEqualTo("My Description");
    assertThat(openapi.getInfo().getVersion()).isEqualTo("v1");
    assertThat(openapi.getExternalDocs().getUrl()).isEqualTo("https://hellodocs.com/docs");
    assertThat(openapi.getExternalDocs().getDescription()).isEqualTo("External Docs Description");
    assertThat(openapi.getServers().size()).isEqualTo(1);
    Optional<Server> server = openapi.getServers().stream().findFirst().stream().findFirst();
    assertThat(server).isPresent();
    assertThat(server.get().getUrl()).isEqualTo("https://sandbox-api.va.gov/r4/");
    assertThat(server.get().getDescription()).isEqualTo("Sandbox");
    assertThat(openapi.getComponents().getSecuritySchemes().size()).isEqualTo(1);
    var oauthflow = openapi.getComponents().getSecuritySchemes().get("OauthFlow");
    assertNotNull(oauthflow);
    assertThat(oauthflow.getType()).isEqualTo(Type.OAUTH2);
    assertThat(oauthflow.getIn()).isEqualTo(In.HEADER);
    assertThat(oauthflow.getFlows().getImplicit().getAuthorizationUrl())
        .isEqualTo("https://sandbox-api.va.gov/oauth2/authorization");
    assertThat(oauthflow.getFlows().getImplicit().getTokenUrl())
        .isEqualTo("https://sandbox-api.va.gov/oauth2/token");
  }

  /** Simple test for nullchecks on optional fields. */
  @Test
  public void openApiMinimumInitializer() {
    OpenAPI openapi = OpenApiUtilities.initializeOpenApi(minimumProperties());
    assertThat(openapi.getInfo().getTitle()).isEqualTo("TITLE");
  }

  /** Checks that asserts are not throwing exceptions. */
  @Test
  public void propertiesValidation() {
    openApiProperties.afterPropertiesSet();
    openApiProperties.getExternalDocs().afterPropertiesSet();
    openApiProperties.getServer().afterPropertiesSet();
    openApiProperties.getComponents().afterPropertiesSet();
    openApiProperties
        .getComponents()
        .getSecurityScheme()
        .forEach((key, value) -> value.afterPropertiesSet());
  }

  // Loads our properties file into a CapabilityStatementProperties bean that we can use.
  @EnableConfigurationProperties(value = OpenApiProperties.class)
  public static class TestConfiguration {}
}
