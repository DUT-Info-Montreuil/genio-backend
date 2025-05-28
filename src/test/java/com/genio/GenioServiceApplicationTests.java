package com.genio;

import com.genio.service.impl.HistorisationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class GenioServiceApplicationTests {

    @MockBean
    private HistorisationService historisationService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
    }
}