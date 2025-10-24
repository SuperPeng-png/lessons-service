package com.yourname.tutor.service;

import com.yourname.tutor.model.dto.TutorDto;
import com.yourname.tutor.model.entity.Tutor;
import com.yourname.tutor.repository.TutorRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;

    @Transactional(readOnly = true)
    public List<TutorDto> getAllTutors() {
        return tutorRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TutorDto mapToDto(Tutor tutor) {
        return TutorDto.builder()
                .id(tutor.getId())
                .firstName(tutor.getFirstName())
                .lastName(tutor.getLastName())
                .email(tutor.getEmail())
                .build();
    }
}
