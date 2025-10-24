package com.yourname.tutor.controller;

import com.yourname.tutor.model.dto.LessonSlotDto;
import com.yourname.tutor.model.dto.TestimonialDto;
import com.yourname.tutor.model.dto.TutorPublicInfoDto;
import com.yourname.tutor.service.PublicTutorService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PublicController {

    private final PublicTutorService publicTutorService;

    @GetMapping("/tutor/info")
    public ResponseEntity<TutorPublicInfoDto> getTutorInfo(@RequestParam Long tutorId) {
        log.debug("Received request for tutor info, tutorId={}", tutorId);
        TutorPublicInfoDto response = publicTutorService.getTutorInfo(tutorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tutor/testimonials")
    public ResponseEntity<List<TestimonialDto>> getTutorTestimonials(@RequestParam Long tutorId) {
        log.debug("Received request for tutor testimonials, tutorId={}", tutorId);
        List<TestimonialDto> testimonials = publicTutorService.getTutorTestimonials(tutorId);
        return ResponseEntity.ok(testimonials);
    }

    @GetMapping("/schedule/slots")
    public ResponseEntity<List<LessonSlotDto>> getScheduleSlots(
            @RequestParam Long tutorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime end) {
        log.debug("Received request for schedule slots, tutorId={}, start={}, end={}", tutorId, start, end);
        List<LessonSlotDto> slots = publicTutorService.getLessonSlots(tutorId, start, end);
        return ResponseEntity.ok(slots);
    }
}
