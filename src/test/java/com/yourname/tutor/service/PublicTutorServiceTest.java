package com.yourname.tutor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.yourname.tutor.exception.ResourceNotFoundException;
import com.yourname.tutor.model.dto.LessonSlotDto;
import com.yourname.tutor.model.dto.TestimonialDto;
import com.yourname.tutor.model.dto.TutorPublicInfoDto;
import com.yourname.tutor.model.entity.LessonSlot;
import com.yourname.tutor.model.entity.LessonSlotStatus;
import com.yourname.tutor.model.entity.Testimonial;
import com.yourname.tutor.model.entity.Tutor;
import com.yourname.tutor.model.entity.TutorInfo;
import com.yourname.tutor.repository.LessonSlotRepository;
import com.yourname.tutor.repository.TestimonialRepository;
import com.yourname.tutor.repository.TutorInfoRepository;
import com.yourname.tutor.repository.TutorRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PublicTutorServiceTest {

    private static final Long TUTOR_ID = 5L;

    @Mock
    private TutorRepository tutorRepository;
    @Mock
    private TutorInfoRepository tutorInfoRepository;
    @Mock
    private TestimonialRepository testimonialRepository;
    @Mock
    private LessonSlotRepository lessonSlotRepository;

    @InjectMocks
    private PublicTutorService publicTutorService;

    private Tutor tutor;

    @BeforeEach
    void setUp() {
        tutor = Tutor.builder()
                .id(TUTOR_ID)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .build();
    }

    @Test
    void getTutorInfo_returnsTutorInfoDto() {
        TutorInfo tutorInfo = TutorInfo.builder()
                .id(TUTOR_ID)
                .tutor(tutor)
                .bio("Experienced math tutor")
                .subjects("Math, Physics")
                .hourlyRate(BigDecimal.valueOf(40))
                .experienceYears(7)
                .build();

        when(tutorRepository.findById(TUTOR_ID)).thenReturn(Optional.of(tutor));
        when(tutorInfoRepository.findById(TUTOR_ID)).thenReturn(Optional.of(tutorInfo));

        TutorPublicInfoDto result = publicTutorService.getTutorInfo(TUTOR_ID);

        assertThat(result).isNotNull();
        assertThat(result.getTutorId()).isEqualTo(TUTOR_ID);
        assertThat(result.getBio()).isEqualTo("Experienced math tutor");
        assertThat(result.getSubjects()).contains("Math");
    }

    @Test
    void getTutorInfo_missingTutorThrows() {
        when(tutorRepository.findById(TUTOR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> publicTutorService.getTutorInfo(TUTOR_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Tutor not found");
    }

    @Test
    void getTutorTestimonials_returnsTestimonials() {
        when(tutorRepository.existsById(TUTOR_ID)).thenReturn(true);
        Testimonial testimonial = Testimonial.builder()
                .id(10L)
                .studentName("Student A")
                .rating(5)
                .comment("Great lessons")
                .build();
        when(testimonialRepository.findAllByTutorIdOrderByCreatedAtDesc(TUTOR_ID))
                .thenReturn(List.of(testimonial));

        List<TestimonialDto> testimonials = publicTutorService.getTutorTestimonials(TUTOR_ID);

        assertThat(testimonials).hasSize(1);
        assertThat(testimonials.getFirst().getStudentName()).isEqualTo("Student A");
    }

    @Test
    void getLessonSlots_returnsSlotsWithinRange() {
        when(tutorRepository.existsById(TUTOR_ID)).thenReturn(true);
        OffsetDateTime start = OffsetDateTime.now();
        OffsetDateTime end = start.plusDays(2);
        LessonSlot slot = LessonSlot.builder()
                .id(11L)
                .tutor(tutor)
                .startTime(start.plusHours(1))
                .endTime(start.plusHours(2))
                .status(LessonSlotStatus.AVAILABLE)
                .build();
        when(lessonSlotRepository.findAllByTutorIdAndStartTimeBetween(TUTOR_ID, start, end))
                .thenReturn(List.of(slot));

        List<LessonSlotDto> slots = publicTutorService.getLessonSlots(TUTOR_ID, start, end);

        assertThat(slots).hasSize(1);
        assertThat(slots.getFirst().getStatus()).isEqualTo(LessonSlotStatus.AVAILABLE);
    }

    @Test
    void getLessonSlots_invalidRangeThrowsIllegalArgument() {
        when(tutorRepository.existsById(TUTOR_ID)).thenReturn(true);
        OffsetDateTime start = OffsetDateTime.now();
        OffsetDateTime end = start.minusDays(1);

        assertThatThrownBy(() -> publicTutorService.getLessonSlots(TUTOR_ID, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("End time must be after start time");
    }

    @Test
    void getLessonSlots_defaultDatesUsesNow() {
        when(tutorRepository.existsById(TUTOR_ID)).thenReturn(true);
        when(lessonSlotRepository.findAllByTutorIdAndStartTimeBetween(any(), any(), any()))
                .thenReturn(List.of());

        List<LessonSlotDto> slots = publicTutorService.getLessonSlots(TUTOR_ID, null, null);

        assertThat(slots).isEmpty();
    }
}
