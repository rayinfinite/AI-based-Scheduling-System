package com.github.rayinfinite.auth.config.security;

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
    @Value("${success-url}")
    private String successUrl;
    private final SecurityHandler securityHandler;
    private final CorsConfig corsConfig;

    /**
     * 禁用不必要的默认filter，处理异常响应内容
     */
    private void commonHttpSetting(HttpSecurity http) throws Exception {
        // 禁用SpringSecurity默认filter。这些filter都是非前后端分离项目的产物，用不上.
        // yml配置文件将日志设置DEBUG模式，就能看到加载了哪些filter
        // logging:
        //    level:
        //       org.springframework.security: DEBUG
        // 表单登录/登出、session管理、csrf防护等默认配置，如果不disable。会默认创建默认filter
        http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable).sessionManagement(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
//                .csrf(AbstractHttpConfigurer::disable)
                // requestCache用于重定向，前后端分析项目无需重定向，requestCache也用不上
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))
                // 无需给用户一个匿名身份
                .anonymous(AbstractHttpConfigurer::disable);

        // 处理 SpringSecurity 异常响应结果。响应数据的结构，改成业务统一的JSON结构。不要框架默认的响应结构
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                // 认证失败异常
                .authenticationEntryPoint(securityHandler)
                // 鉴权失败异常
                .accessDeniedHandler(securityHandler));
        // 其他未知异常. 尽量提前加载。
        //http.addFilterBefore(globalSpringSecurityExceptionHandler, SecurityContextHolderFilter.class);
    }

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        String[] protectedPaths = {"/classroom", "/upload"};
        commonHttpSetting(http);
        http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));
        http.logout(Customizer.withDefaults());
        http.oauth2Login(oauth2->oauth2.defaultSuccessUrl(successUrl,true));

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
