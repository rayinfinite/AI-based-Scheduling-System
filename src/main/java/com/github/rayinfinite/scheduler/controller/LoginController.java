package com.github.rayinfinite.scheduler.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public boolean login(Authentication authentication) {
        return authentication != null;
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities());
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/token_details")
    public Map<String, String> tokenDetails(@AuthenticationPrincipal OidcUser principal) {
        return filterClaims(principal);
    }

    public Map<String, String> filterClaims(OidcUser principal) {
        final String[] claimKeys = {"sub", "aud", "ver", "iss", "name", "oid", "preferred_username"};
        final List<String> includeClaims = Arrays.asList(claimKeys);

        Map<String, String> filteredClaims = new HashMap<>();
        includeClaims.forEach(claim -> {
            if (principal.getIdToken().getClaims().containsKey(claim)) {
                filteredClaims.put(claim, principal.getIdToken().getClaims().get(claim).toString());
            }
        });
        return filteredClaims;
    }
}
