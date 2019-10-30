package gov.va.api.health.informational.r4.wellknown;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("DefaultAnnotationParam")
@Configuration
@ConfigurationProperties("well-known")
@Data
@AllArgsConstructor
@Builder
public class WellKnownProperties {
  private List<String> capabilities;
  private List<String> responseTypeSupported;
  private List<String> scopesSupported;
}
