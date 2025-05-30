package com.genio.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class PasswordDebugController {

    private final BCryptPasswordEncoder encoder;

    @GetMapping("/check")
    public boolean check(@RequestParam String raw, @RequestParam String hash) {
        return encoder.matches(raw, hash);
    }
}