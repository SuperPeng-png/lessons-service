package com.yourname.tutor.model.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TutorPublicInfoDto {
    Long tutorId;
    String firstName;
    String lastName;
    String email;
    String bio;
    String subjects;
    BigDecimal hourlyRate;
    Integer experienceYears;
}
