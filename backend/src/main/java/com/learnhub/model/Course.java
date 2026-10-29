package com.learnhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public String title;
    @Column(length = 2000) public String description;
    public String category;
    public String level;
    public String instructor;
    @Column(name = "duration_hours") public int durationHours;

    public Course() {}
    public Course(String title, String description, String category, String level, String instructor, int hours) {
        this.title = title; this.description = description; this.category = category;
        this.level = level; this.instructor = instructor; this.durationHours = hours;
    }
}
