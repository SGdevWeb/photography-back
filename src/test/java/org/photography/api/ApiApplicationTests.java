package org.photography.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.photography.api.controller.LoginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiApplicationTests {

    @Autowired
    private LoginController loginController;

    @Test
    void contextLoads() {

        Assertions.assertThat(loginController).isNotNull();

    }

}
