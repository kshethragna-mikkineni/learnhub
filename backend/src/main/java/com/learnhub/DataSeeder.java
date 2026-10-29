package com.learnhub;

import com.learnhub.model.Course;
import com.learnhub.model.Question;
import com.learnhub.repo.CourseRepository;
import com.learnhub.repo.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Inserts sample courses and quiz questions the first time the app starts on an empty database. */
@Component
public class DataSeeder implements CommandLineRunner {
    private final CourseRepository courses;
    private final QuestionRepository questions;

    public DataSeeder(CourseRepository courses, QuestionRepository questions) {
        this.courses = courses; this.questions = questions;
    }

    @Override
    public void run(String... args) {
        if (courses.count() > 0) return;

        Course spring = courses.save(new Course("Spring MVC in Practice",
                "Build REST controllers, validate input and secure endpoints with Spring.",
                "Programming", "Intermediate", "Meera Iyer", 12));
        questions.save(new Question(spring, "Which annotation marks a class as a REST controller?", 1,
                "@Service", "@RestController", "@Entity", "@Component"));
        questions.save(new Question(spring, "Which annotation binds a JSON request body to a Java object?", 2,
                "@PathVariable", "@RequestParam", "@RequestBody", "@ModelAttribute"));
        questions.save(new Question(spring, "What does the DispatcherServlet do?", 0,
                "Routes incoming requests to handlers", "Creates database tables", "Hashes passwords", "Compiles JSP files"));

        Course react = courses.save(new Course("React Components from Scratch",
                "Props, state, effects and composing small reusable components.",
                "Programming", "Beginner", "Arjun Rao", 8));
        questions.save(new Question(react, "Which hook stores local component state?", 0,
                "useState", "useMemo", "useRef", "useContext"));
        questions.save(new Question(react, "How do you pass data from parent to child?", 3,
                "Context only", "Global variables", "Refs", "Props"));
        questions.save(new Question(react, "When does useEffect with [] run?", 1,
                "On every render", "After the first render", "Before render", "Never"));

        Course sql = courses.save(new Course("SQL for Analysts",
                "Joins, grouping and window functions on real MySQL datasets.",
                "Data", "Beginner", "Sana Qureshi", 10));
        questions.save(new Question(sql, "Which clause filters rows after GROUP BY?", 2,
                "WHERE", "ORDER BY", "HAVING", "LIMIT"));
        questions.save(new Question(sql, "Which join keeps all rows from the left table?", 1,
                "INNER JOIN", "LEFT JOIN", "CROSS JOIN", "SELF JOIN"));

        courses.save(new Course("Interface Design Basics",
                "Layout, type and colour decisions for clear, usable screens.",
                "Design", "Beginner", "Leela Nair", 6));
    }
}
