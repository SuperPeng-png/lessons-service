package com.yourname.tutor.exception;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiErrorResponse {
    OffsetDateTime timestamp;
    int status;
    String error;
    String message;
    String path;
}
