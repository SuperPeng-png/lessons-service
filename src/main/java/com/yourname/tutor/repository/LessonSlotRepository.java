package com.yourname.tutor.repository;

import com.yourname.tutor.model.entity.LessonSlot;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonSlotRepository extends JpaRepository<LessonSlot, Long> {

    List<LessonSlot> findAllByTutorIdAndStartTimeBetween(Long tutorId, OffsetDateTime start, OffsetDateTime end);
}
