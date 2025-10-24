package com.yourname.tutor.controller;

import com.yourname.tutor.model.dto.TutorDto;
import com.yourname.tutor.service.TutorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tutors")
public class TutorController {

    private final TutorService tutorService;

    @GetMapping
    public ResponseEntity<List<TutorDto>> getTutors() {
        return ResponseEntity.ok(tutorService.getAllTutors());
    }
}
