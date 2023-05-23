package com.project.danim_be.member.controller;

import com.project.danim_be.member.service.GoogleLoginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/user/google", produces = "application/json")
public class GoogleLoginController {

    GoogleLoginService googleLoginService;

    @GetMapping("/callback/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        googleLoginService.socialLogin(code, registrationId);
    }
}
