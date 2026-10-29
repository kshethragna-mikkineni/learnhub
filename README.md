# CourseClimb

Online learning platform: React frontend, Spring MVC backend, MySQL database.

```
learnhub/
  database/schema.sql     optional manual schema (Hibernate also creates it)
  backend/                Spring Boot 3 + Spring MVC + Spring Security (JWT) + JPA
  frontend/               React 18 + Vite + React Router
```

## Run it

Requirements: JDK 17+, Maven, Node 18+, MySQL 8 running on localhost:3306.

1. Backend (port 8080)

       cd backend
       export DB_USER=root DB_PASSWORD=yourpassword
       mvn spring-boot:run

   The `learnhub` database is created on first start, and sample courses
   and quiz questions are inserted.

2. Frontend (port 5173)

       cd frontend
       npm install
       npm run dev

   Open http://localhost:5173. Vite proxies `/api` to the backend.

## What it does

| Area | Where |
| --- | --- |
| Register / sign in (BCrypt + JWT) | `AuthController`, `security/`, `AuthForm.jsx` |
| Course list, search, category filter | `CourseController`, `Courses.jsx`, `CourseCard.jsx` |
| Enrollment (one per user and course) | `EnrollmentController`, `CourseDetail.jsx` |
| Quizzes, graded on the server | `QuizController`, `QuizRunner.jsx` |
| Dashboard with progress | `GET /api/enrollments/me`, `Dashboard.jsx`, `ProgressBar.jsx` |

## API

| Method | Path | Auth |
| --- | --- | --- |
| POST | /api/auth/register, /api/auth/login | no |
| GET | /api/courses?q=&category= , /api/courses/{id} | no |
| POST | /api/courses/{id}/enroll | yes |
| GET | /api/enrollments/me | yes |
| GET | /api/courses/{id}/quiz | yes, enrolled |
| POST | /api/courses/{id}/quiz/submit | yes, enrolled |

Correct answers never leave the server until a quiz is submitted.
Progress is the best quiz score so far, so it never goes down.

## Notes

- Set `JWT_SECRET` (32+ characters) before deploying.
- Entity fields are public to keep the sample short; switch to private
  fields with getters if your team prefers.
- For production, change `ddl-auto` to `validate` and use `schema.sql`
  or a migration tool such as Flyway.
- Ideas to extend: lessons with completion tracking, instructor role for
  creating courses, refresh tokens, tests with Testcontainers.
