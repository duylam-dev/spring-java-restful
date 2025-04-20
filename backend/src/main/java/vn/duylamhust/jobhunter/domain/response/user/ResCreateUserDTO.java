package vn.duylamhust.jobhunter.domain.response.user;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.duylamhust.jobhunter.domain.response.company.ResCompanyUserDTO;
import vn.duylamhust.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;

    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private ResCompanyUserDTO company;

}
