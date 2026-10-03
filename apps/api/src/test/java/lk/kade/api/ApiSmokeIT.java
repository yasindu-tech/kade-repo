package lk.kade.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

/** Boots the whole API against a real PostgreSQL container and checks the walking skeleton. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ApiSmokeIT {

  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;

  @Test
  void flywayAppliesBaselineMigration() {
    String value =
        jdbc.queryForObject(
            "SELECT value FROM app_meta WHERE key = 'schema_baseline'", String.class);
    assertThat(value).isEqualTo("S0-1");
  }

  @Test
  void healthIsUp() throws Exception {
    mvc.perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  void metaEndpointResponds() throws Exception {
    mvc.perform(get("/api/v1/meta"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("kade-api"));
  }
}
