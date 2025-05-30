package vn.duylamhust.jobhunter.domain.response.job;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.duylamhust.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class ResJobDTO {
    private long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private Instant startDate;
    private Instant endDate;
    private boolean active;

    private Instant updatedAt;
    private String updatedBy;
    private Instant createdAt;
    private String createdBy;

    private List<String> skills;
}
