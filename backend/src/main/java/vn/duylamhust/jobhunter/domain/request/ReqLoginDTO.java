package vn.duylamhust.jobhunter.domain.request;

import jakarta.validation.constraints.NotBlank;

public class ReqLoginDTO {
    @NotBlank(message = "username must be not null")
    private String username;
    @NotBlank(message = "password must be not null")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
