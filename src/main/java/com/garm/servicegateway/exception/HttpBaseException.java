package com.garm.servicegateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HttpBaseException extends BaseException {

    private String httpMessage;
    private HttpHeaders headers;
    private HttpStatus status;
    private WebRequest request;

    public HttpBaseException(String message) {
        super(message);
    }
}