package com.hly.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthEventQueryVO {
    private String eventType;
    private String content;
    private LocalDateTime eventTime;
    private Integer isPublic;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
