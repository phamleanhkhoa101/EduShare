package com.edushare.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

  @Autowired private BackendApplication backendApplication;

  @Test
  void contextLoads() {
    String response = backendApplication.healthCheck();
    assertEquals("EduShare backend is running!", response);
  }
}
