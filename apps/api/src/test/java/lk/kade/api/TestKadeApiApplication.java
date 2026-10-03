package lk.kade.api;

import org.springframework.boot.SpringApplication;

public class TestKadeApiApplication {

  public static void main(String[] args) {
    SpringApplication.from(KadeApiApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
