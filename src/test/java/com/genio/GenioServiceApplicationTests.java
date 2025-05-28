package com.genio;

import static org.assertj.core.api.Assertions.assertThat;

import com.genio.service.impl.HistorisationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class GenioServiceApplicationTests {

    @MockBean
    private HistorisationService historisationService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private GenioServiceApplication context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }


}