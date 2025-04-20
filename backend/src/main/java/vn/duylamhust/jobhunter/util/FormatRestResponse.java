package vn.duylamhust.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.duylamhust.jobhunter.domain.response.RestResponse;
import vn.duylamhust.jobhunter.util.anotation.ApiMessage;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        var rs = new RestResponse<>();
        rs.setStatusCode(status);
        if (status >= 400 || body instanceof String || body instanceof Resource) {
            return body;
        } else {
            rs.setData(body);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            rs.setMessage(message == null ? "Call api success" : message.value());

        }

        return rs;
    }

}
