package vn.hoidanit.jobhunter.domain.response.user;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.response.company.ResCompanyUserDTO;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;

    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
    private ResCompanyUserDTO company;

}
