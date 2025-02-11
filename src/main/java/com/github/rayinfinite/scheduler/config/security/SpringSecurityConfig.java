package com.github.rayinfinite.scheduler.config.security;

import com.github.rayinfinite.scheduler.config.CorsConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final SecurityHandler securityHandler;
    private final CorsConfig corsConfig;
    @Value("${success-url}")
    private String successUrl;

    /**
     * Disable unnecessary default filters to handle exception response content
     */
    private void commonHttpSetting(HttpSecurity http) throws Exception {
        // Disable SpringSecurity default filter
        // logging:
        //    level:
        //       org.springframework.security: DEBUG
        http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable).sessionManagement(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
//                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))
                .anonymous(AbstractHttpConfigurer::disable);

        // Handles SpringSecurity exception response results.
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                // Authentication Failure Exception
                .authenticationEntryPoint(securityHandler)
                // Failed authentication exception
                .accessDeniedHandler(securityHandler));
    }

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        String[] protectedPaths = {"/classroom", "/upload"};
        commonHttpSetting(http);
        http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));
        http.logout(Customizer.withDefaults());
        http.oauth2Login(oauth2 -> oauth2.defaultSuccessUrl(successUrl, true));

        http.securityMatcher("**").authorizeHttpRequests(authorize ->
                authorize.requestMatchers(protectedPaths).authenticated().anyRequest().permitAll());
        return http.build();
    }
}

/**
 * <a href="https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa-configuration">...</a>
 */
final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        /*
         * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
         * the CsrfToken when it is rendered in the response body.
         */
        this.xor.handle(request, response, csrfToken);
        /*
         * Render the token value to a cookie by causing the deferred token to be loaded.
         */
        csrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String headerValue = request.getHeader(csrfToken.getHeaderName());
        /*
         * If the request contains a request header, use CsrfTokenRequestAttributeHandler
         * to resolve the CsrfToken. This applies when a single-page application includes
         * the header value automatically, which was obtained via a cookie containing the
         * raw CsrfToken.
         *
         * In all other cases (e.g. if the request contains a request parameter), use
         * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
         * when a server-side rendered form includes the _csrf request parameter as a
         * hidden input.
         */
        return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
    }
}
