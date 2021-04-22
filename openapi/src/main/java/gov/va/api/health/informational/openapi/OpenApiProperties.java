package gov.va.api.health.informational.openapi;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@ConfigurationProperties("openapi")
@Data
@Slf4j
public class OpenApiProperties implements InitializingBean {

  private String title;

  private String description;

  private String version;

  private ExternalDocs externalDocs;

  private Server server;

  private Components components;

  @Override
  public void afterPropertiesSet() {
    Assert.notNull(title, "OpenApiProperties title must not be null.");
    Assert.notNull(description, "OpenApiProperties description must not be null.");
    Assert.notNull(version, "OpenApiProperties version must not be null.");
  }

  @Data
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ExternalDocs implements InitializingBean {

    private String description;
    private String url;

    @Override
    public void afterPropertiesSet() {}
  }

  @Data
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Server implements InitializingBean {

    private String url;
    private String description;

    @Override
    public void afterPropertiesSet() {
      Assert.notNull(url, "Server url must not be null");
      if (StringUtils.isBlank(description)) {
        log.warn("Server description is not set.");
      }
    }
  }

  @Data
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Components implements InitializingBean {

    private Map<String, SecurityScheme> securityScheme;

    @Override
    public void afterPropertiesSet() {}
  }

  @Data
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class SecurityScheme implements InitializingBean {

    private String authorizationUrl;
    private String tokenUrl;
    private io.swagger.v3.oas.models.security.SecurityScheme.Type type;
    private io.swagger.v3.oas.models.security.SecurityScheme.In in;

    @Override
    public void afterPropertiesSet() {
      Assert.notNull(authorizationUrl, "SecurityScheme authorizationUrl must not be null.");
      Assert.notNull(tokenUrl, "SecurityScheme tokenUrl must not be null.");
      Assert.notNull(type, "SecurityScheme type must not be null.");
      Assert.notNull(in, "SecurityScheme in must not be null.");
    }
  }
}
