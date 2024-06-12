package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;

}
