package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.config.exception.BusinessException;
import com.github.rayinfinite.scheduler.utils.LoginUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
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
@RequiredArgsConstructor
public class LoginController {
    private final LoginUtil loginUtil;

    @GetMapping
    public String login(@AuthenticationPrincipal OidcUser principal) {
        if (principal == null) {
            return "false";
        }
        Map<String, Object> claim = principal.getIdToken().getClaims();
        String username = claim.get("name").toString();
        String email = claim.get("preferred_username").toString();
        try {
            loginUtil.checkLogin(username, email);
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return "Not Authorized";
        }
        return "true";
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
        return principal == null ? Map.of("name", "test") : filterClaims(principal);
    }

    @GetMapping("/token")
    public String token(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient.getAccessToken().getTokenValue();
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
