package com.yourname.tutor.repository;

import com.yourname.tutor.model.entity.TutorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorInfoRepository extends JpaRepository<TutorInfo, Long> {
}
