# java

## Часть 1. Создание проекта и проверка работоспобности. Создание модели и контроллера.

<br>

1. Создаём через start.spring.io zip-архив с проектом. Добавляем в него Spring Web. Скачиваем, разархивируем, открываем с помощью Intellij IDEA.

<details>
    <summary>start.spring.io</summary>
    <br>
    <img width="1530" height="767" alt="image" src="https://github.com/user-attachments/assets/4d8b8851-1f0a-4cfa-b696-06c967718b5a" />
</details>

2. Возьмём зависимость Lombok версия 1.18.30 через "https://mvnrepository.com/":

<details>
    <summary>maven repository</summary>
    <br>
    <img width="1316" height="266" alt="image" src="https://github.com/user-attachments/assets/93036855-e553-44dc-9ba9-cf04a0b8cfe1" />
    <br>
    <img width="558" height="220" alt="image" src="https://github.com/user-attachments/assets/19010e4c-856a-4f7a-93b7-b1fe487cad77" />
</details>

3. Добавим зависимость в pom.xml.

<details>
    <summary>pom.xml</summary>
    <br>
    <img width="619" height="424" alt="image" src="https://github.com/user-attachments/assets/8abde529-6a54-4101-8789-c9fbbc679609" />
</details>

4. Создаём пакет model, в нём класс Student:

<details>
    <summary>code</summary>
    <br>
    
    package ru.winnca.restcrud.students.model;
    
    import lombok.*;
    
    import java.time.LocalDate;
    
    @Data
    @Builder
    public class Student {
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        @NonNull
        private String email;
        private int age;
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.model.Student</summary>
    <br>
    <img width="372" height="381" alt="image" src="https://github.com/user-attachments/assets/76f0464e-482f-47c2-b044-10c8c03e3f43" />
</details>

5. Создадим контроллер. В нём сделаем реализацию создания студентов (так делать некорректно).

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.controller;
    
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    import ru.winnca.restcrud.students.model.Student;
    
    import java.time.LocalDate;
    import java.util.List;
    
    @RestController
    @RequestMapping("/api/students")
    public class StudentController {
        @GetMapping("")
        public List<Student> findAllStudents(){
            return List.of(
                    Student.builder().firstName("Alexandr").lastName("Winnitskiy").dateOfBirth(LocalDate.of(2001, 5, 12)).email("winnca@gmail.com").age(24).build(),
                    Student.builder().firstName("Dasha").email("dasha@gmail.com").age(19).build(),
                    Student.builder().firstName("Alina").lastName("Bolmat").email("alinabolmat@gmail.com").age(24).build()
            );
        }
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.controller.StudentController</summary>
    <br>
    <img width="1621" height="592" alt="image" src="https://github.com/user-attachments/assets/4e266c06-1a56-454a-a7b1-8b02b44c83ec" />
</details>

7. Перед запуском убедитесь, что структура проекта выглядит следующим образом:

<details>
    <summary>structure</summary>
    <br>
    <img width="457" height="424" alt="image" src="https://github.com/user-attachments/assets/ef590cfe-25c6-4284-b1a4-5bd6a8ff8473" />
</details>

8. Запуск проекта через класс SpringStudentsApplication. Открываем браузер, в поисковой строке: "http://localhost:8080/api/students".

<details>
    <summary>start project</summary>
    <br>
    <img width="368" height="372" alt="image" src="https://github.com/user-attachments/assets/53dd2b70-a987-4566-a40e-0c9fa2231acf" />
</details>

<br>

<br>

## Часть 2. Создание CRUD с использованием сервиса и репозитория (помимо модели и контроллера).

<br>

9. Содержание бизнес-логики на уровне контроллера в маленьком приложении допускается, но когда масштаб больше, то надо отделять фасад, с которым будут работать пользователи или API от бизнес-логики. Для этого используют **сервисы**.

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.controller;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    import ru.winnca.restcrud.students.model.Student;
    import ru.winnca.restcrud.students.service.StudentService;
    
    import java.time.LocalDate;
    import java.util.List;
    
    @RestController
    @RequestMapping("/api/students")
    public class StudentController{
        @Autowired
        private StudentService studentService;
    
        @GetMapping("")
        public List<Student> findAllStudents(){
            return studentService.findAllStudents();
        }
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.controller.StudentController</summary>
    <br>
    <img width="513" height="582" alt="image" src="https://github.com/user-attachments/assets/5dc9cffa-d8a3-42fd-8886-eb7abff221eb" />
</details>

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.service;
    
    import org.springframework.stereotype.Service;
    import ru.winnca.restcrud.students.model.Student;
    
    import java.time.LocalDate;
    import java.util.List;
    
    @Service
    public class StudentService {
    
        public List<Student> findAllStudents(){
            return List.of(
                    Student.builder().firstName("Alexandr").lastName("Winnitskiy").dateOfBirth(LocalDate.of(2001, 5, 12)).email("winnca@gmail.com").age(24).build(),
                    Student.builder().firstName("Dasha").email("dasha@gmail.com").age(19).build(),
                    Student.builder().firstName("Alina").lastName("Bolmat").email("alinabolmat@gmail.com").age(24).build()
            );
        }
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.service.StudentService</summary>
    <br>
    <img width="1473" height="470" alt="image" src="https://github.com/user-attachments/assets/f75a5c34-2ab0-4fb2-afcf-72a6e4103ce5" />
</details>

<details>
    <summary>structure</summary>
    <br>
    <img width="224" height="193" alt="image" src="https://github.com/user-attachments/assets/44f591bd-38a4-47a2-a035-8d337d114560" />
</details>

<br>

10. Чтобы приложение было легко расширяемым, воспользуемся интерфейсами в package service.

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.service;
    
    import ru.winnca.restcrud.students.model.Student;
    
    import java.util.List;
    
    public interface StudentService {
        List<Student> findAllStudents();
    }
</details>

<details>
    <summary>ru.winnca.restcrud.service.StudentService</summary>
    <br>
    <img width="401" height="250" alt="image" src="https://github.com/user-attachments/assets/cad7b92b-5aa0-49bb-b1ec-a1468085b9ca" />
</details>

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.service.ram;

    import org.springframework.stereotype.Service;
    import ru.winnca.restcrud.students.model.Student;
    import ru.winnca.restcrud.students.service.StudentService;
    
    import java.time.LocalDate;
    import java.util.List;
    
    @Service
    public class InMemoryStudentService implements StudentService {
        @Override
        public List<Student> findAllStudents(){
            return List.of(
                    Student.builder().firstName("Alexandr").lastName("Winnitskiy").dateOfBirth(LocalDate.of(2001, 5, 12)).email("winnca@gmail.com").age(24).build(),
                    Student.builder().firstName("Dasha").email("dasha@gmail.com").age(19).build(),
                    Student.builder().firstName("Alina").lastName("Bolmat").email("alinabolmat@gmail.com").age(24).build()
            );
        }
    }
</details>

<details>
    <summary>ru.winnca.restcrud.service.ram.InMemoryStudentService</summary>
    <br>
    <img width="1471" height="497" alt="image" src="https://github.com/user-attachments/assets/3a61e1ba-c605-4b06-978b-28e7c59b07da" />
</details>

<details>
    <summary>code</summary>
    <br>
    
    package ru.winnca.restcrud.students.controller;
    
    import lombok.AllArgsConstructor;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
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
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.controller.StudentController</summary>
    <br>
    <img width="702" height="464" alt="image" src="https://github.com/user-attachments/assets/4c0e3efe-d455-4042-b2d1-23b56c3b2052" />
</details>

<details>
    <summary>structure</summary>
    <br>
    <img width="244" height="242" alt="image" src="https://github.com/user-attachments/assets/81d41491-4e46-44ea-b1c1-0ca201fa7d7d" />
</details>

<br>

11. Создаём методы CRUD (создание, обновление, чтение, удаление). Пока только в сервисе.

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.service;
    
    import ru.winnca.restcrud.students.model.Student;
    
    import java.util.List;
    
    public interface StudentService {
        List<Student> findAllStudents();
        Student saveStudent(Student student);
        Student readByEmail(String email);
        Student updateStudent(Student student);
        void deleteStudent(String email);
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.service.StudentService</summary>
    <br>
    <img width="404" height="414" alt="image" src="https://github.com/user-attachments/assets/e2a22ed2-cebb-45f6-8f13-82edd9238ae8" />
</details>

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.service.ram;
    
    import org.springframework.stereotype.Service;
    import ru.winnca.restcrud.students.model.Student;
    import ru.winnca.restcrud.students.service.StudentService;
    
    import java.time.LocalDate;
    import java.util.List;
    
    @Service
    public class InMemoryStudentService implements StudentService {
        @Override
        public List<Student> findAllStudents(){
            return List.of(
                    Student.builder().firstName("Alexandr").lastName("Winnitskiy").dateOfBirth(LocalDate.of(2001, 5, 12)).email("winnca@gmail.com").age(24).build(),
                    Student.builder().firstName("Dasha").email("dasha@gmail.com").age(19).build(),
                    Student.builder().firstName("Alina").lastName("Bolmat").email("alinabolmat@gmail.com").age(24).build()
            );
        }
    
        @Override
        public Student saveStudent(Student student) {
            return null;
        }
    
        @Override
        public Student readStudent(String email) {
            return null;
        }
    
        @Override
        public Student updateStudent(Student student) {
            return null;
        }
    
        @Override
        public void deleteStudent(String email) {
    
        }
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.service.ram.InMemoryStudentService</summary>
    <br>
    <img width="1476" height="804" alt="image" src="https://github.com/user-attachments/assets/6e4ac6b0-aca8-4d62-8eb6-7f17f212ea8b" />
</details>

<br>

12. Хранение объектов в сервисе некорректно, лучше делать этого через репозитории. Своего рода классы, обеспечивающие доступ к данным. Либо взаимодействует с базой данных (MySQL, PostgreSQL, ...) или с памятью.

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.repository;
    import org.springframework.stereotype.Repository;
    import ru.winnca.restcrud.students.model.Student;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.stream.IntStream;
    
    @Repository
    public class InMemoryStudentDAO {
        private final List<Student> STUDENTS = new ArrayList<>();
        public List<Student> findAllStudents(){
            return STUDENTS;
        }
        public Student saveStudent(Student student) {
            STUDENTS.add(student);
            return student;
        }
        public Student readStudent(String email) {
            return STUDENTS.stream().filter(student -> student.getEmail().equals(email)).findFirst().orElse(null);
        }
        public Student updateStudent(Student student) {
            var studentIndex = IntStream.range(0, STUDENTS.size()).filter(index -> STUDENTS.get(index).getEmail().equals(student.getEmail())).findFirst().orElse(-1);
            if (studentIndex > -1){
                STUDENTS.set(studentIndex, student);
                return student;
            }
            return null;
        }
        public void deleteStudent(String email) {
            var student = readStudent(email);
            if (student != null){
                STUDENTS.remove(student);
            }
        }
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.repository.InMemoryStudentDAO</summary>
    <br>
    <img width="1393" height="895" alt="image" src="https://github.com/user-attachments/assets/1374b999-b226-4f1c-812d-a9dda5a65307" />
</details>

<details>
    <summary>structure</summary>
    <br>
    <img width="242" height="283" alt="image" src="https://github.com/user-attachments/assets/ec6ee12e-7ccd-4a40-8701-21eca012d0d0" />
</details>

<br>

13. Использование репозитория в сервисе.

<details>
    <summary>code</summary>
    <br>

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
</details>

<details>
    <summary>ru.winnca.restcrud.students.service.ram.InMemoryStudentService</summary>
    <br>
    <img width="1391" height="887" alt="image" src="https://github.com/user-attachments/assets/07191f4a-2329-4782-be1e-0733160ad092" />
</details>

<br>

14. Использование сервиса в контроллере.

<details>
    <summary>code</summary>
    <br>

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
</details>

<details>
    <summary>ru.winnca.restcrud.students.controller.StudentController</summary>
    <br>
    <img width="976" height="887" alt="image" src="https://github.com/user-attachments/assets/53e8515c-5504-4dda-9b89-7ed3cc841a9f" />
</details>

<br>

15. Скачивае приложение Postman. Нажимаем на кнопку "New", выбираем "HTTP".

<details>
    <summary>Настройка Postman</summary>
    <br>
    <img width="944" height="606" alt="image" src="https://github.com/user-attachments/assets/e072ebd0-b017-432e-ac03-164cc035489b" />
</details>

16. Тестируем API. Выбираем POST запрос, выбираем "raw", вводим в формате JSON нового пользователя, нажимаем "Send".

<details>
    <summary>Настройка HTTP</summary>
    <br>
    <img width="855" height="648" alt="image" src="https://github.com/user-attachments/assets/83bffb54-c660-4190-8635-32fa40a2e220" />
</details>

<br>

17. Проверяем.

<details>
    <summary>Тестим API</summary>
    <br>
    <img width="847" height="639" alt="image" src="https://github.com/user-attachments/assets/07ef550d-d727-4d26-95ce-ac45efd03eb0" />
    <br>
    <img width="515" height="247" alt="image" src="https://github.com/user-attachments/assets/6d0b9353-5f7e-4671-a00d-05e5cf71d60f" />
</details>

<br>

18. Добавим ещё нового пользователя для наглядности, которые храняться в памяти.

<details>
    <summary>create</summary>
    <br>
    <img width="851" height="612" alt="image" src="https://github.com/user-attachments/assets/ce997041-1c91-4ff5-8fb8-0bf2f7352d27" />
    <br>
    <img width="838" height="662" alt="image" src="https://github.com/user-attachments/assets/7147ec5f-b00e-4bfd-938f-6f18980732fd" />
    <br>
    <img width="329" height="302" alt="image" src="https://github.com/user-attachments/assets/7e84de27-f114-47e2-91d6-5d69030ceeb6" />
</details>

Обратите внимание, что можем создавать нового пользователя, прописывая только некоторые поля (обязательно email, так как аннотация @NonNull):

<details>
    <summary>details</summary>
    <img width="843" height="513" alt="image" src="https://github.com/user-attachments/assets/5d7506bb-954e-4a1c-a43b-ab472847a005" />
</details>

19. Просмотрим данные одного из пользователей.

<details>
    <summary>read</summary>
    <br>
    <img width="654" height="509" alt="image" src="https://github.com/user-attachments/assets/5a8e0530-8bc7-4436-afb9-e04654f4d929" />
</details>

20. Изменим данные о пользователе.

<details>
    <summary>update</summary>
    <br>
    <img width="646" height="527" alt="image" src="https://github.com/user-attachments/assets/bdbf73cf-77d3-445d-a146-2b22a17f45ef" />
    <br>
    <img width="654" height="719" alt="image" src="https://github.com/user-attachments/assets/921dfeb4-64fc-47ea-8a4b-0a256ede3673" />
    <br>
    <img width="321" height="313" alt="image" src="https://github.com/user-attachments/assets/5be3aff4-3f03-4f15-bcbe-5e355baf7daf" />
</details>

21. Удалим пользователя.

<details>
    <summary>delete</summary>
    <br>
    <img width="648" height="397" alt="image" src="https://github.com/user-attachments/assets/51e059d6-f695-4312-9eae-a804c6c2e580" />
    <br>
    <img width="653" height="557" alt="image" src="https://github.com/user-attachments/assets/fa465273-aa05-4df6-8986-d5bd8726789d" />
</details>

<br>

<br>

## Часть 3. Вместо использование оперативной памяти, репозиторий взаимодействует с БД.

<br>

22. Вместо хранение объектов в оперативной памяти, воспользуемся БД. Будем использовать Spring Data JPA и PostgreSQL.

<details>
    <summary>pom.xml</summary>
    <br>
    <img width="486" height="107" alt="image" src="https://github.com/user-attachments/assets/f41b0c02-841a-4690-b418-954b13809118" />
    <br>
    <img width="337" height="102" alt="image" src="https://github.com/user-attachments/assets/2ae2ef3f-4397-4d74-9a95-253bef4b58fa" />
</details>

<br>

23. Перейдём в application.yaml настроим подключение к БД.

<details>
    <summary>code</summary>
    <br>

    spring:
      application:
        name: students
      jackson:
        serialization:
          indent-output: true
      datasource:
        url:
          jdbc:postgresql://localhost:5432/student_db
        username:
          postgres
        password:
          12345678
        driver-class-name: org.postgresql.Driver
      jpa:
        hibernate:
          ddl-auto: create
        database: postgresql
        database-platform: org.hibernate.dialect.PostgreSQLDialect
        show-sql: true
        properties:
          hibernate:
            format_sql: true
</details>

<details>
    <summary>resources.application.yaml</summary>
    <br>
    <img width="226" height="146" alt="image" src="https://github.com/user-attachments/assets/7aa30a4c-e722-4c52-9c09-1ad1b422b08e" />
    <br>
    <img width="526" height="514" alt="image" src="https://github.com/user-attachments/assets/6cc04f3d-491d-4124-9af2-c4eb7dd4d1dd" />
</details>

<br>

24. Изменим модель, чтобы java класс можно преобразовать в таблицу или сущность, то есть Hibernate и Spring Data Jpa могли манипулировать этими объектами. Будет использовать аннотации.

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.model;
    
    import jakarta.persistence.*;
    import lombok.*;
    
    import java.time.LocalDate;
    import java.time.Period;
    
    @Data
    //@Builder
    @Entity
    @Table(name="students")
    public class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        //@NonNull
        @Column(unique = true)
        private String email;
        @Transient
        private int age;
    
        public int getAge() {
            return Period.between(dateOfBirth, LocalDate.now()).getYears()+1;
        }
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.model.Student</summary>
    <br>
    <img width="581" height="679" alt="image" src="https://github.com/user-attachments/assets/56d763e2-171c-448c-b404-d6ca1d7eb97f" />
</details>

<br>

25. Добавим репозиторий студентов для взаимодействия с БД - JpaRepository. Добавим в него методы CRUD.

<details>
    <summary>code</summary>
    <br>

    package ru.winnca.restcrud.students.repository;
    
    import org.springframework.data.jpa.repository.JpaRepository;
    import ru.winnca.restcrud.students.model.Student;
    import java.util.Optional;
    
    public interface StudentRepository extends JpaRepository<Student, Long> {
        Optional<Student> findByEmail(String email);
    }
</details>

<details>
    <summary>ru.winnca.restcrud.students.repository.StudentRepository</summary>
    <br>
    <img width="253" height="362" alt="image" src="https://github.com/user-attachments/assets/d438fd69-646b-4341-9014-e451570e31f9" />
    <br>
    <img width="602" height="255" alt="image" src="https://github.com/user-attachments/assets/5cb952e4-254b-4000-894b-dee9c4dc5472" />
</details>

<br>

26. Создадим сервис для взаимодействия с этим репозиторием. Пометим аннотацией @Primary - главный сервис для взаимодействия, если бд прекратит свою работу, то будет работать второй сервис.

<details>
    <summary>code</summary>
    <br>

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
</details>

<details>
    <summary>ru.winnca.restcrud.students.service.db.StudentServiceImpl</summary>
    <br>
    <img width="241" height="145" alt="image" src="https://github.com/user-attachments/assets/8f908b47-ca6e-4a79-ab03-96bceb91e69f" />
    <br>
    <img width="922" height="883" alt="image" src="https://github.com/user-attachments/assets/d8fbe87b-ce89-44e2-8aa2-62e481d2c65b" />
</details>

<br>

27. Приложение готово, можно открывать pgAdmin4 (вводим пароль, создаём бд student_db) и Postman для тестирования. Запускаем приложение.

<details>
    <summary>PostgreSQL</summary>
    <br>
    <img width="269" height="189" alt="image" src="https://github.com/user-attachments/assets/acf6daed-af92-4c80-8e6c-6af996c65c77" />
</details>

<details>
    <summary>Postman</summary>
    <br>
    <img width="642" height="317" alt="image" src="https://github.com/user-attachments/assets/e213b657-1b9e-460d-a9a7-5b0b3e6fae3b" />
</details>

<br>

28. Тестим.

<details>
    <summary>create</summary>
    <br>
    <img width="637" height="547" alt="image" src="https://github.com/user-attachments/assets/30d8ed21-16b0-4eb5-8b22-17b23014dea8" />
    <br>
    <img width="666" height="519" alt="image" src="https://github.com/user-attachments/assets/ecbcab4a-98fb-4c3b-9e0a-cc68dee6c3df" />
    <br>
</details>

<details>
    <summary>read</summary>
    <br>
    <img width="637" height="732" alt="image" src="https://github.com/user-attachments/assets/a9ab64ac-4443-447a-a83f-296ddd13938c" />
    <br>
    <img width="730" height="216" alt="image" src="https://github.com/user-attachments/assets/9bd1d737-4237-4aa1-a89b-48b89ee7d49e" />
    <br>
    <img width="642" height="538" alt="image" src="https://github.com/user-attachments/assets/90e33605-a5f6-4f4d-ae99-4a5a65ee987b" />
</details>

<details>
    <summary>update</summary>
    <br>
    <img width="639" height="559" alt="image" src="https://github.com/user-attachments/assets/9cd3ab98-2314-4ad2-b076-a957eaca675d" />
    <br>
    <img width="644" height="718" alt="image" src="https://github.com/user-attachments/assets/410b258a-f6e0-419c-ad00-afd1fb3366c2" />
    <br>
    <img width="732" height="224" alt="image" src="https://github.com/user-attachments/assets/9937e2a2-b5da-459e-8cdb-f4d7fc0ded82" />
</details>

<details>
    <summary>delete</summary>
    <br>
    Подобным образом, как делали метод "read" для получения сведений о студенте в Postman, делаем для "delete".
    <br>
    <img width="737" height="192" alt="image" src="https://github.com/user-attachments/assets/f8c27f04-f22d-4515-9a6f-2d500a376f3e" />
    <br>
    <img width="649" height="581" alt="image" src="https://github.com/user-attachments/assets/abe63d3f-3520-4f63-828b-d2e363055a33" />
</details>
