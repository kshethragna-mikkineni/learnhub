package com.learnhub.web;

import com.learnhub.model.Enrollment;
import com.learnhub.model.Question;
import com.learnhub.repo.EnrollmentRepository;
import com.learnhub.repo.QuestionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/courses/{courseId}/quiz")
public class QuizController {
    /** Question as sent to the browser: no correct answer included. */
    public record QuestionDto(Long id, String text, List<String> options) {}
    public record SubmitReq(Map<Long, Integer> answers) {}
    public record Result(int score, int total, int percent, Map<Long, Integer> correct) {}

    private final QuestionRepository questions;
    private final EnrollmentRepository enrollments;

    public QuizController(QuestionRepository questions, EnrollmentRepository enrollments) {
        this.questions = questions; this.enrollments = enrollments;
    }

    @GetMapping
    public List<QuestionDto> quiz(@PathVariable Long courseId, @AuthenticationPrincipal Long userId) {
        requireEnrollment(userId, courseId);
        return questions.findByCourseId(courseId).stream()
                .map(q -> new QuestionDto(q.id, q.text, q.options)).toList();
    }

    @PostMapping("/submit")
    @Transactional
    public Result submit(@PathVariable Long courseId, @RequestBody SubmitReq req,
                         @AuthenticationPrincipal Long userId) {
        Enrollment e = requireEnrollment(userId, courseId);
        List<Question> qs = questions.findByCourseId(courseId);
        Map<Long, Integer> answers = req.answers() == null ? Map.of() : req.answers();
        Map<Long, Integer> correct = new HashMap<>();
        int score = 0;
        for (Question q : qs) {
            correct.put(q.id, q.correctIndex);
            Integer given = answers.get(q.id);
            if (given != null && given == q.correctIndex) score++;
        }
        int percent = qs.isEmpty() ? 0 : Math.round(100f * score / qs.size());
        e.lastScore = percent;
        e.progress = Math.max(e.progress, percent);   // progress never goes backwards
        enrollments.save(e);
        return new Result(score, qs.size(), percent, correct);
    }

    private Enrollment requireEnrollment(Long userId, Long courseId) {
        return enrollments.findByUserIdAndCourseId(userId, courseId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN, "Enroll in this course to take its quiz"));
    }
}
