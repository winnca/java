package ru.winnca.restcrud.students.service.ram;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.winnca.restcrud.students.model.Student;
import ru.winnca.restcrud.students.repository.InMemoryStudentDAO;
import ru.winnca.restcrud.students.service.StudentService;
//import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryStudentService implements StudentService {

    private final InMemoryStudentDAO studentDAO;
    @Override
    public List<Student> findAllStudents(){
//        return List.of(
//                Student.builder().firstName("Alexandr").lastName("Winnitskiy").dateOfBirth(LocalDate.of(2001, 5, 12)).email("winnca@gmail.com").age(24).build(),
//                Student.builder().firstName("Dasha").email("dasha@gmail.com").age(19).build(),
//                Student.builder().firstName("Alina").lastName("Bolmat").email("alinabolmat@gmail.com").age(24).build()
//        );
        return studentDAO.findAllStudents();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentDAO.saveStudent(student);
    }

    @Override
    public Student readStudent(String email) {
        return studentDAO.readStudent(email);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentDAO.updateStudent(student);
    }

    @Override
    public void deleteStudent(String email) {
        studentDAO.deleteStudent(email);
    }
}
