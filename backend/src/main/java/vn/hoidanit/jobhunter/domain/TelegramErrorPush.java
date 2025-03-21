package vn.hoidanit.jobhunter.domain;

import lombok.Data;

@Data
public class TelegramErrorPush {
    private String intentCase;
    private String errorCode;
    private String message;
    private String searchQuery;
    private String statedTime;
    
}
