package com.yourname.tutor.service;

import com.yourname.tutor.exception.ResourceNotFoundException;
import com.yourname.tutor.model.dto.LessonSlotDto;
import com.yourname.tutor.model.dto.TestimonialDto;
import com.yourname.tutor.model.dto.TutorPublicInfoDto;
import com.yourname.tutor.model.entity.LessonSlot;
import com.yourname.tutor.model.entity.Testimonial;
import com.yourname.tutor.model.entity.Tutor;
import com.yourname.tutor.model.entity.TutorInfo;
import com.yourname.tutor.repository.LessonSlotRepository;
import com.yourname.tutor.repository.TestimonialRepository;
import com.yourname.tutor.repository.TutorInfoRepository;
import com.yourname.tutor.repository.TutorRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicTutorService {

    private static final int DEFAULT_SLOT_RANGE_DAYS = 30;

    private final TutorRepository tutorRepository;
    private final TutorInfoRepository tutorInfoRepository;
    private final TestimonialRepository testimonialRepository;
    private final LessonSlotRepository lessonSlotRepository;

    @Transactional(readOnly = true)
    public TutorPublicInfoDto getTutorInfo(Long tutorId) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found: " + tutorId));

        TutorInfo tutorInfo = tutorInfoRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor info not found for tutor: " + tutorId));

        return mapToTutorInfoDto(tutor, tutorInfo);
    }

    @Transactional(readOnly = true)
    public List<TestimonialDto> getTutorTestimonials(Long tutorId) {
        ensureTutorExists(tutorId);
        return testimonialRepository.findAllByTutorIdOrderByCreatedAtDesc(tutorId).stream()
                .map(this::mapToTestimonialDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonSlotDto> getLessonSlots(Long tutorId, OffsetDateTime start, OffsetDateTime end) {
        ensureTutorExists(tutorId);
        OffsetDateTime effectiveStart = Optional.ofNullable(start).orElse(OffsetDateTime.now());
        OffsetDateTime effectiveEnd = Optional.ofNullable(end)
                .orElse(effectiveStart.plusDays(DEFAULT_SLOT_RANGE_DAYS));

        if (!effectiveEnd.isAfter(effectiveStart)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        return lessonSlotRepository
                .findAllByTutorIdAndStartTimeBetween(tutorId, effectiveStart, effectiveEnd)
                .stream()
                .map(this::mapToLessonSlotDto)
                .collect(Collectors.toList());
    }

    private void ensureTutorExists(Long tutorId) {
        if (!tutorRepository.existsById(tutorId)) {
            throw new ResourceNotFoundException("Tutor not found: " + tutorId);
        }
    }

    private TutorPublicInfoDto mapToTutorInfoDto(Tutor tutor, TutorInfo tutorInfo) {
        return TutorPublicInfoDto.builder()
                .tutorId(tutor.getId())
                .firstName(tutor.getFirstName())
                .lastName(tutor.getLastName())
                .email(tutor.getEmail())
                .bio(tutorInfo.getBio())
                .subjects(tutorInfo.getSubjects())
                .hourlyRate(tutorInfo.getHourlyRate())
                .experienceYears(tutorInfo.getExperienceYears())
                .build();
    }

    private TestimonialDto mapToTestimonialDto(Testimonial testimonial) {
        return TestimonialDto.builder()
                .id(testimonial.getId())
                .studentName(testimonial.getStudentName())
                .rating(testimonial.getRating())
                .comment(testimonial.getComment())
                .createdAt(testimonial.getCreatedAt())
                .build();
    }

    private LessonSlotDto mapToLessonSlotDto(LessonSlot lessonSlot) {
        return LessonSlotDto.builder()
                .id(lessonSlot.getId())
                .startTime(lessonSlot.getStartTime())
                .endTime(lessonSlot.getEndTime())
                .status(lessonSlot.getStatus())
                .build();
    }
}
