package ru.winnca.restcrud.students.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.winnca.restcrud.students.model.Student;
import ru.winnca.restcrud.students.service.StudentService;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentController{

    private final StudentService studentService;
    @GetMapping("")
    public List<Student> findAllStudents(){
        return studentService.findAllStudents();
    }

    @PostMapping("/save_student")
    public Student saveStudent(@RequestBody Student student){
        return studentService.saveStudent(student);
    }

    @GetMapping("/read_student/{email}")
    public Student readStudent(@PathVariable("email") String email){
        return studentService.readStudent(email);
    }

    @PutMapping("/update_student")
    public Student updateStudent(@RequestBody Student student){
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/delete-student/{email}")
    public void deleteStudent(@PathVariable("email") String email){
        studentService.deleteStudent(email);
    }
}
