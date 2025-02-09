package com.github.rayinfinite.auth.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.github.rayinfinite.auth.config.exception.BusinessException;
import com.github.rayinfinite.auth.entity.Response;
import com.github.rayinfinite.auth.utils.LoginUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginUtil loginUtil;
    @Value("${public-key}")
    private String publicKeyBase64;
    @Value("${private-key}")
    private String privateKeyBase64;

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
        return filterClaims(principal);
    }

    @GetMapping("/token")
    public String token(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient.getAccessToken().getTokenValue();
    }

    @GetMapping("/jwt")
    public Response jwt(@AuthenticationPrincipal OidcUser principal) {
        if (principal == null) {
            return new Response();
        }
        if(!login(principal).equals("true")){
            return new Response();
        }
        OidcIdToken idToken = principal.getIdToken();
        Map<String, Object> claims = idToken.getClaims();
        try {
            ECPublicKey publicKey = (ECPublicKey) KeyFactory.getInstance("EC")
                    .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase64)));
            ECPrivateKey privateKey = (ECPrivateKey) KeyFactory.getInstance("EC")
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64)));
            Algorithm algorithm = Algorithm.ECDSA256(publicKey, privateKey);
            String token = JWT.create()
                    .withExpiresAt(idToken.getExpiresAt())
                    .withIssuedAt(idToken.getIssuedAt())
                    .withSubject(claims.get("sub").toString())
                    .withClaim("name", claims.get("name").toString())
                    .withClaim("email", claims.get("preferred_username").toString())
                    .withClaim("oid", claims.get("oid").toString())
                    .withIssuer("AIcourse")
                    .sign(algorithm);
            return new Response(token);
        } catch (JWTCreationException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        return new Response();
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
