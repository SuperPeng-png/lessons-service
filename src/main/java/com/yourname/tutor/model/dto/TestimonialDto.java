package com.yourname.tutor.model.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestimonialDto {
    Long id;
    String studentName;
    Integer rating;
    String comment;
    OffsetDateTime createdAt;
}
