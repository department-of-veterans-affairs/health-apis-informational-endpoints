package gov.va.api.health.informational.r4.wellknown;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * Configuration for Well-Known controller settings.
 *
 * <p>NOTE: Some optional properties are not set/supported by current implementation.
 */
@SuppressWarnings("DefaultAnnotationParam")
@Configuration
@ConfigurationProperties("well-known")
@Data
@AllArgsConstructor
@Builder
@Slf4j
public class WellKnownProperties implements InitializingBean {

  /**
   * Required, array of strings representing SMART capabilities (e.g., single-sign-on or
   * launch-standalone) that the server supports.
   */
  private List<String> capabilities;

  /** Optional, array of OAuth2 response_type values that are supported. */
  private List<String> responseTypeSupported;

  /** Optional, array of scopes a client may request. See scopes and launch context. */
  private List<String> scopesSupported;

  @Override
  public void afterPropertiesSet() throws IllegalArgumentException {
    Assert.notEmpty(capabilities, "WellKnownProperties capabilities must not be null or empty.");
    if ((responseTypeSupported == null) || responseTypeSupported.isEmpty()) {
      log.warn("WellKnownProperties responseTypeSupported is not set.");
    }
    if ((scopesSupported == null) || scopesSupported.isEmpty()) {
      log.warn("WellKnownProperties scopesSupported is not set.");
    }
  }
}
