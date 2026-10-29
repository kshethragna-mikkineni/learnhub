package com.learnhub.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "course_id") public Course course;
    @Column(nullable = false, length = 500) public String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @OrderColumn(name = "option_order")
    @Column(name = "option_text")
    public List<String> options;

    @Column(name = "correct_index") public int correctIndex;

    public Question() {}
    public Question(Course c, String text, int correct, String... options) {
        this.course = c; this.text = text; this.correctIndex = correct; this.options = List.of(options);
    }
}
