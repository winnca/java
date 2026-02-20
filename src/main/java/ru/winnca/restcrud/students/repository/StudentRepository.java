package ru.winnca.restcrud.students.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.winnca.restcrud.students.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
}