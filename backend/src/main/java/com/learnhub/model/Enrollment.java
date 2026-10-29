package com.learnhub.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "enrollments", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "user_id") public User user;
    @ManyToOne(optional = false) @JoinColumn(name = "course_id") public Course course;
    public int progress;                                  // 0-100
    @Column(name = "last_score") public Integer lastScore; // last quiz result, 0-100
    @Column(name = "enrolled_at") public Instant enrolledAt = Instant.now();
}
