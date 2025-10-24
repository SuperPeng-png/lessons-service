package com.yourname.tutor.repository;

import com.yourname.tutor.model.entity.Testimonial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {

    List<Testimonial> findAllByTutorIdOrderByCreatedAtDesc(Long tutorId);
}
