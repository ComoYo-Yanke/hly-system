package com.hly.dto;

import com.hly.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthEventInsertDTO {
    private String eventType;
    private String content;
    private LocalDateTime eventTime;
    private Integer isPublic;
}
