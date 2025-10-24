package com.yourname.tutor.model.dto;

import com.yourname.tutor.model.entity.LessonSlotStatus;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LessonSlotDto {
    Long id;
    OffsetDateTime startTime;
    OffsetDateTime endTime;
    LessonSlotStatus status;
}
