package com.github.rayinfinite.algorithm.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${public-key}")
    private String publicKeyBase64;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        // Acquire JWT token from request
        String token = getTokenFromRequest(request);
        // Verify token
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                ECPublicKey publicKey = (ECPublicKey) KeyFactory.getInstance("EC")
                        .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase64)));
                Algorithm algorithm = Algorithm.ECDSA256(publicKey, null);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("AIcourse")
                        .build(); // Reusable verifier instance

                DecodedJWT jwt = verifier.verify(token);
                String sub = jwt.getSubject();
                String name = jwt.getClaim("name").asString();
                String email = jwt.getClaim("email").asString();
                String oid = jwt.getClaim("oid").asString();
                log.debug("\nSubject: {},\nName: {},\nEmail: {},\nOID: {}", sub, name, email, oid);
                UserDetails userDetails = new UserInfoDetails(name, email, oid, sub);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (JWTVerificationException | InvalidKeySpecException | NoSuchAlgorithmException e) {
                // Invalid token or invalid key specification
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}