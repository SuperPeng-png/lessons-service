package com.yourname.tutor.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TutorDto {
    Long id;
    String firstName;
    String lastName;
    String email;
}
