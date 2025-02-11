package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.repository.AuditLogRepository;
import com.github.rayinfinite.scheduler.repository.UserRepository;
import com.github.rayinfinite.scheduler.utils.LoginUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ContextConfiguration(classes = {LoginController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class LoginControllerDiffblueTest {
    @Autowired
    private LoginController loginController;

    @MockitoBean
    private LoginUtil loginUtil;

    /**
     * Test {@link LoginController#login(OidcUser)}.
     * <ul>
     *   <li>When {@code null}.</li>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link LoginController#login(OidcUser)}
     */
    @Test
    @DisplayName("Test login(OidcUser); when 'null'; then return 'false'")
    void testLogin_whenNull_thenReturnFalse() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange, Act and Assert
        assertEquals("false",
                (new LoginController(new LoginUtil(mock(AuditLogRepository.class), mock(UserRepository.class)))).login(null));
    }

    /**
     * Test {@link LoginController#getUser(Authentication)}.
     * <ul>
     *   <li>When {@code null}.</li>
     *   <li>Then return Body is {@code null}.</li>
     * </ul>
     * <p>
     * Method under test: {@link LoginController#getUser(Authentication)}
     */
    @Test
    @DisplayName("Test getUser(Authentication); when 'null'; then return Body is 'null'")
    void testGetUser_whenNull_thenReturnBodyIsNull() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange and Act
        ResponseEntity<Map<String, Object>> actualUser = (new LoginController(
                new LoginUtil(mock(AuditLogRepository.class), mock(UserRepository.class)))).getUser(null);

        // Assert
        HttpStatusCode statusCode = actualUser.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        assertNull(actualUser.getBody());
        assertEquals(401, actualUser.getStatusCodeValue());
        assertEquals(HttpStatus.UNAUTHORIZED, statusCode);
        assertFalse(actualUser.hasBody());
    }

    /**
     * Test {@link LoginController#getUser(Authentication)}.
     * <ul>
     *   <li>When unauthenticated {@code ABC123}.</li>
     *   <li>Then return Body size is two.</li>
     * </ul>
     * <p>
     * Method under test: {@link LoginController#getUser(Authentication)}
     */
    @Test
    @DisplayName("Test getUser(Authentication); when unauthenticated 'ABC123'; then return Body size is two")
    void testGetUser_whenUnauthenticatedAbc123_thenReturnBodySizeIsTwo() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        LoginController loginController = new LoginController(
                new LoginUtil(mock(AuditLogRepository.class), mock(UserRepository.class)));

        // Act
        ResponseEntity<Map<String, Object>> actualUser = loginController
                .getUser(OneTimeTokenAuthenticationToken.unauthenticated("ABC123"));

        // Assert
        Map<String, Object> body = actualUser.getBody();
        assertEquals(2, body.size());
        Object getResult = body.get("authorities");
        assertInstanceOf(List.class, getResult);
        HttpStatusCode statusCode = actualUser.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        assertEquals("", body.get("name"));
        assertEquals(200, actualUser.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(((List<Object>) getResult).isEmpty());
        assertTrue(actualUser.hasBody());
    }
}
