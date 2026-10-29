package com.learnhub.web;

import com.learnhub.model.Enrollment;
import com.learnhub.repo.CourseRepository;
import com.learnhub.repo.EnrollmentRepository;
import com.learnhub.repo.UserRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class EnrollmentController {
    public record EnrollmentDto(Long courseId, String title, String category, String level,
                                int durationHours, int progress, Integer lastScore, Instant enrolledAt) {
        static EnrollmentDto of(Enrollment e) {
            return new EnrollmentDto(e.course.id, e.course.title, e.course.category, e.course.level,
                    e.course.durationHours, e.progress, e.lastScore, e.enrolledAt);
        }
    }

    private final EnrollmentRepository enrollments;
    private final CourseRepository courses;
    private final UserRepository users;

    public EnrollmentController(EnrollmentRepository enrollments, CourseRepository courses, UserRepository users) {
        this.enrollments = enrollments; this.courses = courses; this.users = users;
    }

    @GetMapping("/enrollments/me")
    public List<EnrollmentDto> mine(@AuthenticationPrincipal Long userId) {
        return enrollments.findByUserIdOrderByEnrolledAtDesc(userId).stream().map(EnrollmentDto::of).toList();
    }

    @PostMapping("/courses/{courseId}/enroll")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentDto enroll(@PathVariable Long courseId, @AuthenticationPrincipal Long userId) {
        if (enrollments.findByUserIdAndCourseId(userId, courseId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already enrolled in this course");
        Enrollment e = new Enrollment();
        e.course = courses.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        e.user = users.getReferenceById(userId);
        return EnrollmentDto.of(enrollments.save(e));
    }
}
