package com.garm.servicegateway.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ErrorDto {
    @JsonAlias("ErrorCode")
    private String errorCode;

    @JsonAlias("Description")
    private String description;
}