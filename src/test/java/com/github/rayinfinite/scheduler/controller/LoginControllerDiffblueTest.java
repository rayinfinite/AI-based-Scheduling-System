package com.github.rayinfinite.scheduler.controller;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {LoginController.class})
@ExtendWith(SpringExtension.class)
class LoginControllerDiffblueTest {
    @Autowired
    private LoginController loginController;

    /**
     * Test {@link LoginController#login(Authentication)}.
     * <p>
     * Method under test: {@link LoginController#login(Authentication)}
     */
    @Test
    @DisplayName("Test login(Authentication)")
    void testLogin() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login");

        // Act and Assert
        ResultActions resultActions = MockMvcBuilders.standaloneSetup(loginController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(Boolean.FALSE.toString()));
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
        ResponseEntity<Map<String, Object>> actualUser = (new LoginController()).getUser(null);

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
        LoginController loginController = new LoginController();

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
