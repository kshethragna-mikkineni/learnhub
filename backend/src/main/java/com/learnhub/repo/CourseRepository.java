package com.learnhub.repo;

import com.learnhub.model.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
        select c from Course c
        where (:q is null or lower(c.title) like lower(concat('%', :q, '%')))
          and (:category is null or c.category = :category)
        order by c.title
        """)
    List<Course> search(@Param("q") String q, @Param("category") String category);
}
