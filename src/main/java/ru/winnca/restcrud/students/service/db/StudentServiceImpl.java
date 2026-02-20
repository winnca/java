package ru.winnca.restcrud.students.service.db;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.winnca.restcrud.students.model.Student;
import ru.winnca.restcrud.students.repository.StudentRepository;
import ru.winnca.restcrud.students.service.StudentService;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student readStudent(String email) {
        return studentRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Student updateStudent(Student student) {
        if (student.getId() != null && studentRepository.existsById(student.getId())){
            return studentRepository.save(student);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteStudent(String email) {
        Student student = readStudent(email);
        if (student != null){
            studentRepository.deleteById(student.getId());
        }
    }
}