package lk.kade.api.meta;

import java.time.Instant;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Tiny public endpoint the web app and uptime checks use to confirm the API is reachable. */
@RestController
@RequestMapping("/api/v1/meta")
class MetaController {

  private final String version;

  MetaController(ObjectProvider<BuildProperties> buildProperties) {
    BuildProperties props = buildProperties.getIfAvailable();
    this.version = props != null ? props.getVersion() : "dev";
  }

  @GetMapping
  MetaResponse meta() {
    return new MetaResponse("kade-api", version, Instant.now());
  }

  record MetaResponse(String name, String version, Instant serverTime) {}
}
