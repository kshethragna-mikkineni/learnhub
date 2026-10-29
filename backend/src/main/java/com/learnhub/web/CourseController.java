package com.learnhub.web;

import com.learnhub.model.Course;
import com.learnhub.repo.CourseRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseRepository courses;

    public CourseController(CourseRepository courses) { this.courses = courses; }

    @GetMapping
    public List<Course> list(@RequestParam(required = false) String q,
                             @RequestParam(required = false) String category) {
        return courses.search(blankToNull(q), blankToNull(category));
    }

    @GetMapping("/{id}")
    public Course get(@PathVariable Long id) {
        return courses.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }

    private static String blankToNull(String s) { return s == null || s.isBlank() ? null : s.trim(); }
}
