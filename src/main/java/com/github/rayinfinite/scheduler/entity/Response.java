package com.github.rayinfinite.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    private Object data;

    public Response() {
        this.code = "success";
        this.message = "success";
    }

    public Response(Object data) {
        this();
        this.data = data;
    }
}
