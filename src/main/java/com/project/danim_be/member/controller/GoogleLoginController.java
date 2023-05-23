package com.project.danim_be.member.controller;

import com.project.danim_be.member.service.GoogleLoginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/user", produces = "application/json")
public class GoogleLoginController {

    GoogleLoginService googleLoginService;

    @GetMapping("/google/callback")
    public void googleLogin(@RequestParam String code, String registrationId) {
        System.out.println(code);
        googleLoginService.socialLogin(code, registrationId);
    }
}
