package com.ibsadjobah.bulksms.bulksms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {

    private int code;
    private String message;
    private Map<String, ?> data;
    private Map<String, ?> errors;



}
