package vn.hoidanit.jobhunter.util.error.anotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface ApiMessage {
    String value();
}
