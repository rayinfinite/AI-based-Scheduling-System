package com.github.rayinfinite.scheduler.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {
    
    @Test
    void testDefaultConstructor() {
        Response response = new Response();
        assertEquals("success", response.getCode());
        assertEquals("success", response.getMessage());
        assertNull(response.getData());
    }
    
    @Test
    void testDataConstructor() {
        String data = "test data";
        Response response = new Response(data);
        assertEquals("success", response.getCode());
        assertEquals("success", response.getMessage());
        assertEquals(data, response.getData());
    }
    
    @Test
    void testAllArgsConstructor() {
        String data = "test data";
        Response response = new Response("code1", "message1", data);
        assertEquals("code1", response.getCode());
        assertEquals("message1", response.getMessage());
        assertEquals(data, response.getData());
    }
    
    @Test
    void testBuilder() {
        Response response = Response.builder()
                .code("code1")
                .message("message1")
                .data("test data")
                .build();
                
        assertEquals("code1", response.getCode());
        assertEquals("message1", response.getMessage());
        assertEquals("test data", response.getData());
    }
} 