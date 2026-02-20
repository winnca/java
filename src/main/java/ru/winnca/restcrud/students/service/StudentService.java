package ru.winnca.restcrud.students.service;

import ru.winnca.restcrud.students.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAllStudents();
    Student saveStudent(Student student);
    Student readStudent(String email);
    Student updateStudent(Student student);
    void deleteStudent(String email);
}
