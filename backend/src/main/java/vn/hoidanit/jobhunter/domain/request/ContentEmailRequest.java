package vn.hoidanit.jobhunter.domain.request;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ContentEmailRequest {
    String email;
    String candidateName;
}
