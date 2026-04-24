# Содержание

## Теория:

* ### [О проекте](#title11)

* ### [Spring Security](#title12)

## Практика:

* ### [Часть 1: создание каркаса приложения](#title21)

* ### [Часть 2: настройка логина и пароля](#title22)

* ### [Часть 3: права доступа к ресурсу](#title23)

* ### [Часть 4: создаём своих пользователей](#title24)

* ### [Часть 5: добавление аутентификации](#title25)

<br>
<br>

---

## <a id="title11">О проекте</a>

Данный проект — это **пошаговое учебное руководство по внедрению Spring Security** в веб-приложение на Spring Boot.

<details>
    <summary>Цель проекта</summary>
    <br>
   Научиться на практике:
    
   - подключать и настраивать Spring Security;
    
   - управлять аутентификацией и авторизацией пользователей;
    
   - разграничивать доступ к эндпоинтам в зависимости от ролей;
    
   - создавать и хранить пользователей в базе данных;
    
   - хешировать пароли с помощью BCrypt;
    
   - использовать кастомные `UserDetailsService` и `UserDetails`.
</details>

<details>
    <summary>Стек технологий</summary>

   * **Java 17+** = язык разработки.
   
   * **Spring Boot** = основа приложения.
   
   * **Spring Security** = безопасность, аутентификация, авторизация.
   
   * **Spring Data JPA / Hibernate** = работа с базой данных.
   
   * **PostgreSQL** = СУБД для хранения пользователей.
   
   * **Maven** = сборка проекта и управление зависимостями.
   
   * **Lombok** = сокращение шаблонного кода.
   
   * **JavaFaker** = генерация тестовых данных.
</details>

<details>
    <summary>Структура</summary>
    <br>
   Проект разбит на 5 практических частей:

   1. **Создание каркаса приложения** — модель, сервис, контроллер, генерация 100 тестовых приложений.

   2. **Настройка логина и пароля** — через `application.yaml` и через `SecurityConfig`.

   3. **Права доступа к ресурсам** — `SecurityFilterChain`, `@PreAuthorize`, роли пользователей.

   4. **Создание своих пользователей** — подключение БД, JPA, репозитории, кастомные `UserDetailsService` и `UserDetails`.

   5. **Добавление аутентификации** — `AuthenticationProvider`, хеширование паролей, тестирование через Postman.
</details>

<details>
    <summary>Что сможете делать после изучения</summary>

   - Настраивать безопасность в любом Spring-приложении.

   - Разграничивать доступ для разных ролей (USER, ADMIN).

   - Хранить пользователей в реальной базе данных (не в памяти).

   - Понимать разницу между аутентификацией и авторизацией.

   - Использовать BCrypt для безопасного хранения паролей.
</details>

<details>
    <summary>Замечание (важно)</summary>
    <br>
   Проект носит исключительно учебный характер. В реальном приложении:
    
   - нельзя отключать CSRF при использовании браузерных форм;

   - нужно выносить секреты (пароль БД и т.д.) в переменные окружения;

   - рекомендуется добавлять валидацию входных данных;
   
   - логику загрузки тестовых данных (JavaFaker) не следует использовать в production.
</details>

<br>
<br>

---

## <a id="title12">Spring Security</a>

**Spring Security** — это фреймворк для обеспечения безопасности Spring-приложений.

<details>
    <summary>2 главные задачи</summary>

   * **Аутентификация** — проверка, что пользователь — это тот, за кого себя выдаёт (обычно по логину/паролю).

   * **Авторизация** — проверка, имеет ли пользователь право делать то, что пытается сделать (доступ к эндпоинтам, данным и т.д.).
</details>

<details>
    <summary>Ключевые компоненты</summary>

   * `SecurityFilterChain` = цепочка фильтров, через которую проходит каждый запрос = `securityFilterChain()` в `SecurityConfig`.

   * `UserDetailsService` = "сервис поиска пользователей" — находит пользователя по логину = `MyUserDetailsService`.

   * `UserDetails` = "Обёртка пользователя" — хранит логин, пароль, роли, статусы = `BCryptPasswordEncoder`.

   * `PasswordEncoder` = "Кодировщик паролей" — хеширует пароли (необратимо) = `BCryptPasswordEncoder`.

   * `AuthenticationProvider` = "Механизм проверки" — использует `UserDetailsService` + `PasswordEncoder` = `DaoAuthenticationProvider`.
</details>

<details>
    <summary>Важно понимать</summary>

   - **Хеширование ≠ Шифрование** — хеш нельзя расшифровать обратно в пароль.

   - **BCrypt** специально сделан медленным — это усложняет подбор паролей злоумышленниками.

   - **CSRF** можно отключать только в stateless API (без сессий и кук).
</details>

<br>
<br>

---

## <a id="title21">Практика-часть-1: создание каркаса приложения</a>

1. Открываем браузер и вводим: `start.spring.io` в адресной строке:

<details>
    <summary>start.spring.io</summary>

   * Выбираем проект `Maven`, язык `Java` и версию, стабильную версию `Spring Boot`, конфигурационный файл `YAML`, тип сборки `JAR`.

   * Вводим метаданные проекта.

   * Добавляем зависимости `Spring Web`, `Spring Security`, `Lombok`.
</details>

2. Создадим модель. В нём будут лежать некие приложения со следующими полями: `id`, `name`, `author`, `version`. Добавим аннотации `@Data`, `@Builder`.

<details>
    <summary>code</summary>

    package ru.winnca.spring_security.models;
    
    import lombok.Builder;
    import lombok.Data;
    
    @Data
    @Builder
    public class Application {
        private int id;
        private String name;
        private String author;
        private String version;
    }
</details>

3. Добавим зависимость `javafaker` в `pom.xml`: https://mvnrepository.com/artifact/com.github.javafaker/javafaker/1.0.2. Позволяет генерировать случайные строки и числа.

<details>
    <summary>dependency</summary>

    <!-- Source: https://mvnrepository.com/artifact/com.github.javafaker/javafaker -->
    <dependency>
        <groupId>com.github.javafaker</groupId>
        <artifactId>javafaker</artifactId>
        <version>1.0.2</version>
        <scope>compile</scope>
    </dependency>
</details>

<br>

4. Реализуем логику приложения. Для этого создадим app-сервис. В этом сервисе реализуем:

<details>
    <summary>logic app</summary>

   * Метод, который добавить 100 случайных приложений в коллекцию. Для этого метода добавим аннотацию `@PostConstruct` = гарантирует вызов метода 1 раз после инициализации всех компонентов.

   * Метод, который вернёт все приложения.

   * Метод, который вернёт приложение по идентификатору.
</details>

<details>
    <summary>code</summary>

    package ru.winnca.spring_security.services;
    
    import com.github.javafaker.Faker;
    import jakarta.annotation.PostConstruct;
    import org.springframework.stereotype.Service;
    import ru.winnca.spring_security.models.Application;
    
    import java.util.List;
    import java.util.stream.IntStream;
    
    @Service
    public class AppService {
        private List<Application> applications;
    
        @PostConstruct
        public void loadAppInDB(){
            Faker faker = new Faker();
            applications = IntStream.rangeClosed(1, 100).mapToObj(i -> Application.builder().id(i).name(faker.app().name()).author(faker.app().author()).version(faker.app().version()).build()).toList();
        }
    
        public List<Application> allApplications(){
            return applications;
        }
    
        public Application applicationById(int id){
            return applications.stream().filter(app -> app.getId() == id).findFirst().orElse(null);
        }
    }
</details>

<details>
    <summary>Замечание</summary>
    <br>
    
   * подобная реализация методов должна находиться в репозитории, но пока сконцетрируемся на реализации `Spring Security`.
</details>

5. Создаём контрольные точки:

<details>
    <summary>endpoints</summary>

   * 1 контрольная точка будет возвращать строку.

   * 2 контрольная точка будет возвращать из сервиса все приложения.

   * 3 контрольная точка возвращать приложение по идентификатору.
</details>

<details>
    <summary>code</summary>
    
    package ru.winnca.spring_security.controllers;
    
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    import ru.winnca.spring_security.models.Application;
    import ru.winnca.spring_security.services.AppService;
    
    import java.util.List;
    
    @RestController
    @RequestMapping("api/v1/app")
    public class AppController {
        private AppService service;
    
        @GetMapping("/welcome")
        public String welcome(){
            return "Welcome to the unprotected page";
        }
    
        @GetMapping("/all-apps")
        public List<Application> allApplications(){
            return service.allApplications();
        }
    
        @GetMapping("/app/{id}")
        public Application applicationById(@PathVariable int id){
            return service.applicationById(id);
        }
    }
</details>

<br>

6. Протестируем приложение, запускаем.

<details>
    <summary>test</summary>

   * Если проект не запускается из-за lombok, то добавьте версию `<version>1.18.36</version>` в зависимость и плагин.

   * В терминале всё в порядке, кроме строчки: `Using generated security password: d9287221-e8fe-41bf-9aec-7c401083af9c`. Пароль для входа.

   * Логин по умолчанию `Spring Security`: `user`, пароль указан выше. нажимаем `sign in`.
</details>

<details>
    <summary>terminal</summary>
    <br>
    <img width="1546" height="380" alt="image" src="https://github.com/user-attachments/assets/15fc03fb-715a-44e7-9cf7-eac1160d7c6b" />
    <br>
    <img width="1642" height="197" alt="image" src="https://github.com/user-attachments/assets/5b709e81-57a5-44a1-b74c-82ec5eb7c208" />
</details>

<details>
    <summary>website</summary>
    <br>
    <img width="353" height="229" alt="image" src="https://github.com/user-attachments/assets/61f96e3c-884e-4e9d-a2f1-b0bf749f1a0d" />
    <br>
    <img width="402" height="138" alt="image" src="https://github.com/user-attachments/assets/89cf23ae-4e6f-4497-8821-f76338880413" />
    <br>
    <img width="1897" height="544" alt="image" src="https://github.com/user-attachments/assets/e0256995-204a-48ea-be6e-602d58128af8" />
    <br>
    <img width="498" height="132" alt="image" src="https://github.com/user-attachments/assets/61749133-5365-43f9-bf8a-e310ff7f4b0a" />
</details>

<br>
<br>

---

## <a id="title22">Практика-часть-2: настройка логина и пароля.</a>

<br>

## Настройка логина и пароля через application.yaml.

7. В `application.yaml` прописываем имя пользователя и пароль (любой).

<details>
    <summary>login and password</summary>
    <br>

    spring:
        security:
            user:
                name: winnca
                password: winnca
</details>

<br>

8. Протестируем:

<details>
    <summary>sign in</summary>
    <br>
    <img width="1718" height="279" alt="image" src="https://github.com/user-attachments/assets/16bf4459-8846-423c-b34e-a6cdb08bcb25" />
    <br>
    <img width="341" height="220" alt="image" src="https://github.com/user-attachments/assets/1b3c04a3-1fab-40b1-8957-73b99d5ece7f" />
    <br>
    <img width="422" height="152" alt="image" src="https://github.com/user-attachments/assets/c16d7014-6cbc-41ab-879d-3aad55e91b54" />
    <br>
    <img width="1904" height="557" alt="image" src="https://github.com/user-attachments/assets/93e0f1c6-1301-4103-95e9-b585f9d4cac9" />
    <br>
    <img width="531" height="122" alt="image" src="https://github.com/user-attachments/assets/50d4c302-78da-47ef-b991-456b5fdd1fb4" />
</details>

<details>
    <summary>Вывод</summary>

   * Нет случайного сгенерированного пароля, вошли под своим логином и пароль.

   * Такой подход допустим только в своих тестах и создаёт только 1 аккаунт, на деле в реальных приложениях и тестах не делают.

   * Правильно делать через `SecurityConfig`.
</details>

<br>

## Настройка логина и пароля через SecurityConfig

9. Создаём пакет `config` и класс `SecurityConfig`. 

<details>
    <summary>config.SecurityConfig</summary>

   * На класс добавим аннотации: `@Configuration` и `@EnableWebSecurity` = используются для настройки `Spring Security`.

   * `@Configuration` является конфигурационным бином.

   * `@EnableWebSecurity` применение глобальной web безопасности.
</details>

<details>
    <summary>code-class</summary>
    
    package ru.winnca.spring_security.config;
    
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
    
    }
</details>

<details>
    <summary>2 methods</summary>
    <br>
    
   **2 метода:** 1 - создаёт пользователя и сохранит в приложении, 2 - возвращает объект, который является кодировщиком паролей и выполняет хэширование алгоритмом `BCrypt`.
</details>

<details>
    <summary>хэширование</summary>
    <br>
    
   **Хеширование** — это необратимое преобразование пароля в строку фиксированной длины (хеш).

   **Как это работает:**
   
   1. При регистрации: пароль → хешируется → в БД сохраняется хеш.

   2. При входе: введённый пароль → хешируется снова → сравнивается с хешем из БД.
   
   3. Если хеши совпадают → пароль верный.

   **Зачем это нужно:**

   - При взломе БД злоумышленник получит хеши, а не реальные пароли.
   
   - Подобрать исходный пароль по хешу очень сложно и долго.
   
   - Алгоритм **BCrypt** специально сделан медленным — это усложняет перебор.

   **Важно:** Пароль при входе не "передаётся в зашифрованном виде". `Spring Security` просто хеширует его и сравнивает с хешем из БД. Если пароль в БД хранится в открытом виде, а не хешированным — аутентификация не сработает, даже если пароли одинаковые.
</details>

<details>
    <summary>1 method</summary>
    <br>
    
   **1 метод:** используется интерфейс `UserDetailsService` = позволяет предоставить сведения о пользователе в контексте безопасности (имя, пароль, статус учётной записи, роли пользователя, ...).

  * принимает метод `PasswordEncoder` = интерфейс для одностороннего преобразования пароля. Для хранения пароля необходимо сравнивать с паролем предоставленным пользователем.

  * `UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).build();` = по такой схеме можно создавать сколько угодно пользователей. Пароль хэшируется.

  * Если пароль не хэшировать, тогда в бд будет обычным (пароль, приходящий от пользователя во время входа в систему, при сравнении с паролем из бд будут не совпадать, даже если они одинаковы в обычном виде) = (потому что пароль во время входа в систему передаётся в зашифрованном виде, а хранящийся в бд нет).
  
  * `InMemoryUserDetailsMananger` = класс для хранения и управления всеми пользователями. Под правильным логином и паролем сможем войти в систему. 
</details>

<details>
    <summary>2 method</summary>
    <br>

   **2 метод:** уже говорили. Дополним, что оба метода должны быть помечены аннотацией `@Bean`, чтобы находиться в контексте приложения.
</details>

<details>
    <summary>code-method</summary>
    
    package ru.winnca.spring_security.config;
    
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder encoder){
            UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).build();
            UserDetails user = User.builder().username("user").password("user").build();
            UserDetails alex = User.builder().username("alex").password(encoder.encode("alex")).build();
            
            return new InMemoryUserDetailsManager(admin, user, alex);
        }
    
        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
    }
</details>

<br>

10. Удалим данные из `apllication.yaml` по созданию пользователя. Перезапустим приложение. Протестируем.

<details>
  <summary>architecture</summary>
  <br>
  <img width="274" height="243" alt="image" src="https://github.com/user-attachments/assets/7a088d2d-4000-410f-9a01-80103717a579" />
</details>

<details>
  <summary>sign in alex</summary>
  <br>
  <img width="354" height="221" alt="image" src="https://github.com/user-attachments/assets/7cbf5dd9-24b1-4eea-920d-63dab4930c4e" />
  <br>
  <img width="341" height="121" alt="image" src="https://github.com/user-attachments/assets/cb66031a-fcd7-4d05-a0ff-198d34a90398" />
  <br>
  <img width="1897" height="582" alt="image" src="https://github.com/user-attachments/assets/f6560201-c028-44a2-b2ad-4ed95b99c42f" />
  <br>
  <img width="602" height="151" alt="image" src="https://github.com/user-attachments/assets/885bc7ab-07d5-4859-bdf5-abd1b1aa4674" />
</details>

<details>
  <summary>sign in admin</summary>
  <br>
  <img width="337" height="221" alt="image" src="https://github.com/user-attachments/assets/1d7817d6-64f0-4460-a9ca-31f144933a68" />
  <br>
  <img width="404" height="111" alt="image" src="https://github.com/user-attachments/assets/fab1e592-1a0e-4228-8b41-e5b4dd226d9d" />
  <br>
  <img width="1892" height="562" alt="image" src="https://github.com/user-attachments/assets/a2f0b7e6-dc66-453e-bd46-c51fc3fb122a" />
  <br>
  <img width="473" height="145" alt="image" src="https://github.com/user-attachments/assets/f65cf006-8e24-434c-b880-416caefd5577" />
</details>

<details>
  <summary>sign in user</summary>
  <br>
  <img width="351" height="225" alt="image" src="https://github.com/user-attachments/assets/60048a45-ace3-41e9-b907-9936251a2fdd" />
  <br>
  <img width="343" height="282" alt="image" src="https://github.com/user-attachments/assets/095fe82b-a767-4279-b72b-0a2ba3616314" />
  <br>
  
    Исправим на: UserDetails user = User.builder().username("user").password(encoder.encode("user")).build();

  <br>
  <img width="337" height="283" alt="image" src="https://github.com/user-attachments/assets/c2dfc84a-bb51-4d71-aa04-f975ebef97bd" />
  <br>
  <img width="472" height="138" alt="image" src="https://github.com/user-attachments/assets/d082b774-cf97-4764-b96f-9a5badd089c1" />
  <br>
  <img width="1891" height="566" alt="image" src="https://github.com/user-attachments/assets/8ff8131d-4ebb-40b0-bdc1-57492c404303" />
</details>

<details>
    <summary>Замечание</summary>

   * Но, что если хотим сделать доступ в контрольную точку всем пользователям (зарегистрированным и нет).
</details>

<br>
<br>

---

## <a id="title23">Практика-часть-3: права доступа к ресурсу</a>

<br>

## Настройка SpringFilterChain

11. Создадим 3-ий метод в конфигурационном классе.

<details>
    <summary>SecurityFilterChain</summary>
    <br>
    
   * `SecurityFilterChain` = интерфейс для создания фильтра. В параметрах принимает `HttpSecurity` = позволяет конфигугрировать аутентификацию и авторизацию запросов. Пометим аннотации `@Bean`.
</details>

<details>
  <summary>3 method</summary>
  <br>
  
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/welcome").permitAll().
                        requestMatchers("/api/v1/**").authenticated()).
                formLogin(AbstractAuthenticationFilterConfigurer::permitAll).build();
    }
</details>

<br>

<details>
    <summary>Что такое CSRF (Cross-Site Request Forgery)</summary>
    <br>

   * Тип атаки, при котором злоумышленник заставляет пользователя невольно выполнить действие на сайте, где тот уже авторизован.

   **Как происходит атака:**

   1. Вы входите в интернет-банк (`bank.com`) — браузер сохраняет куку.

   2. Не выходя из банка, вы переходите на вредоносный сайт (`ev.com`).

   3. Сайт `ev.com` отправляет скрытый запрос на `bank.com/transfer?to=hacker&amount=1000`.

   4. Браузер автоматически прикрепляет к запросу куку от `bank.com`.

   5. Сервер банка думает, что запрос от вас, и переводит деньги.

   **Как защищает Spring Security (CSRF-токен):**

   - Сервер генерирует уникальный случайный токен для каждой формы.

   - Токен встраивается в страницу (например, в скрытое поле).

   - При отправке формы токен отправляется обратно.

   - Сервер проверяет токен. Если его нет или он неверный → запрос отклоняется.
</details>

<details>
    <summary>Почему в нашем проекте мы отключаем CSRF</summary>

   - `.csrf(AbstractHttpConfigurer::disable)`.

   - Мы используем `stateless` `REST API` (без сессий и кук).

   - В такой архитектуре `CSRF`-атаки не актуальны.

   - **Предупреждение:** В приложениях с формами входа (``Thymeleaf`) отключать `CSRF` нельзя!

   Реализация метода:

   * В нём отключаем `CSRF` защиту. Допустимо, где нет кук/сессий/браузеров. Нельзя при использовании приложений с формами (например, интернет-банк) и сессиями для аутентификации.

   * Впускает всех по адресу: `"/api/v1/welcome"`.

   * По остальным адресам впускает только авторизированных пользователей.

   * Все желающие могут авторизироваться: `.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)`:

     * включает стандартную страницу логина `Spring Security`.

     * `permitAll()` - разрешает ВСЕМ доступ к странице логина.
</details>

<br>

12. Перезапустим приложение и протестируем.

<details>
  <summary>sign in</summary>
  <br>
  <img width="295" height="47" alt="image" src="https://github.com/user-attachments/assets/a416f4dc-95f5-480b-b18f-9b6a5b58b351" />
  <br>
  <img width="376" height="109" alt="image" src="https://github.com/user-attachments/assets/cf32be1c-1e73-4631-84bb-4b6d7e291288" />
  <br>
  <img width="223" height="41" alt="image" src="https://github.com/user-attachments/assets/4342c65f-a3d0-45f7-bcbb-f2ba3b52484e" />
  <br>
  <img width="1048" height="328" alt="image" src="https://github.com/user-attachments/assets/184b2792-426d-4e23-8895-d565c8b2bda1" />
  <br>
  <img width="327" height="221" alt="image" src="https://github.com/user-attachments/assets/843267ba-02ea-4988-8abf-e2402a704a8b" />
  <br>
  <img width="1899" height="556" alt="image" src="https://github.com/user-attachments/assets/d08d4497-7921-4e35-af0e-9cc6fdfebd2f" />
</details>

<details>
    <summary>Замечание</summary>
    <br>

   * Что если надо дать доступ к конкретным контрольным точкам людям с определёнными правами.
</details>

<br>

## Доступ к endpoints пользователям с определёнными правами.

<details>
    <summary>Пояснение по @PreAuthorize</summary>

   * `@PreAuthorize` проверяет права доступа **перед** выполнением метода.

   * `hasAuthority('ROLE_ADMIN')` = есть ли у пользователя конкретное право =  `@PreAuthorize("hasAuthority('ROLE_ADMIN')")`.

   * `hasRole('ADMIN')` = то же самое, но префикс `ROLE_` добавляется автоматически = `@PreAuthorize("hasRole('ADMIN')")`.
</details>

<details>
    <summary>В коде</summary>

   - `hasAuthority('ROLE_ADMIN')` и `hasAuthority('ROLE_USER')` — проверяют конкретные права.

   - `hasAuthority('ROLE_ADMIN')` — доступ только у `ADMIN`.
</details>

<br>

13. Добавим роли в классе `SecurityConfig`:

<details>
  <summary>1 method</summary>
  
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").build();
        UserDetails user = User.builder().username("user").password(encoder.encode("user")).roles("USER").build();
        UserDetails alex = User.builder().username("alex").password(encoder.encode("alex")).roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(admin, user, alex);
    }
</details>

14. Чтобы авторизация обрабатывала роли на уровне метода, добавим на класс `SecurityConfig` аннотацию `@EnableMethodSecurity`:

<details>
  <summary>class SecurityConfig</summary>

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
</details>

15. Добавим на контрольные точки проверки прав доступа:

<details>
  <summary>controllers.AppController</summary>
    
    package ru.winnca.spring_security.controllers;
    
    import lombok.AllArgsConstructor;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    import ru.winnca.spring_security.models.Application;
    import ru.winnca.spring_security.services.AppService;
    
    import java.util.List;
    
    @RestController
    @AllArgsConstructor
    @RequestMapping("api/v1")
    public class AppController {
        private AppService service;
    
        @GetMapping("/welcome")
        public String welcome(){
            return "Welcome to the unprotected page";
        }
    
        @GetMapping("/all-apps")
        @PreAuthorize("hasAuthority('ROLE_USER')")
        public List<Application> allApplications(){
            return service.allApplications();
        }
    
        @GetMapping("/app/{id}")
        @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        public Application applicationById(@PathVariable int id){
            return service.applicationById(id);
        }
    }
</details>

<br>

16. Протестируем. Зайдя под `user` будет доступно только просмотр всех приложений. Зайдя под `admin` просмотр приложения по идентификатору. Зайдя под `alex` будут доступны оба способа.

<details>
  <summary>sign in user</summary>
  <br>
  <img width="250" height="29" alt="image" src="https://github.com/user-attachments/assets/23c817ee-41ef-43b9-bb3c-72114dd4a8e4" /> 
  <br>
  <img width="340" height="155" alt="image" src="https://github.com/user-attachments/assets/a6d7520c-d634-4f0c-835c-29bffe5b97f4" />
  <br>
  <img width="1906" height="558" alt="image" src="https://github.com/user-attachments/assets/6f96ee49-b518-44f0-a7d1-05b4bc0c00e4" />
  <br>
  <img width="608" height="482" alt="image" src="https://github.com/user-attachments/assets/af16ce8b-11e9-4d96-82fc-e0e14e5ce8fb" />
</details>

<details>
  <summary>sign in admin</summary>
  <br>
  <img width="242" height="37" alt="image" src="https://github.com/user-attachments/assets/61cba721-094f-43c2-bb6c-bd7f0a88697e" />
  <br>
  <img width="329" height="229" alt="image" src="https://github.com/user-attachments/assets/73b1d5ad-9db0-4eea-b16e-b0b19b35ede5" />
  <br>
  <img width="522" height="141" alt="image" src="https://github.com/user-attachments/assets/fa47291b-75c5-4658-b60d-69a9bbbe830f" />
  <br>
  <img width="594" height="481" alt="image" src="https://github.com/user-attachments/assets/730f6974-a3f3-496b-8d95-ba0f04a86975" />
</details>

<details>
  <summary>sign in alex</summary>
  <br>
  <img width="246" height="37" alt="image" src="https://github.com/user-attachments/assets/9f05f9ce-7b37-4f3e-b5ab-ed3e13afb2da" />
  <br>
  <img width="326" height="227" alt="image" src="https://github.com/user-attachments/assets/cc329cfe-1bd7-4172-bc67-9ebed969bb6d" />
  <br>
  <img width="1912" height="563" alt="image" src="https://github.com/user-attachments/assets/93801c60-192a-43f5-9dbf-875dd35056af" />
  <br>
  <img width="535" height="139" alt="image" src="https://github.com/user-attachments/assets/712f1dcd-3f36-43b2-99fd-c4e0690e97e4" />
</details>

<br>
<br>

---

## <a id="title24">Практика-часть-4: создаём своих пользователей</a>

#### Создание своих пользователей через отдельный контроллер, а не напрямую через  `UserDetailsService`.

17. Подключим `Spring Data Jpa`:

<details>
    <summary>Spring Data JPA и Hibernate</summary>

   * **JPA (Java Persistence API)** — это спецификация (набор интерфейсов и правил). Она описывает, как Java-объекты должны отображаться на таблицы базы данных. Сама JPA не содержит реализации — это только стандарт.

   * **Hibernate** — это самая популярная реализация спецификации JPA (ORM-фреймворк). Он выполняет низкоуровневую работу:

     - Преобразует Java-классы (с аннотацией `@Entity`) в таблицы базы данных.

     - Преобразует вызовы методов (`repository.save(user)`) в SQL-запросы (`INSERT INTO users...`).

     - Преобразует результаты SQL-запросов обратно в Java-объекты.

   **Зачем это нужно:** Вы пишете код на Java, а Hibernate сам переводит его в SQL. Это ускоряет разработку и упрощает смену базы данных.
</details>

<details>
  <summary>dependency</summary>
  
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</details>

18. Подключим драйвер для взаимодействия с бд:

<details>
  <summary>dependency</summary>
  
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</details>

<br>

19. Создадим в `models` класс `MyUser`:

<details>
    <summary>annotations</summary>

   * `@Entity` = указывает, что класс является JPA-сущностью и будет отображён на таблицу в БД.

   * `@Table(name="users")` = задаёт имя таблицы в базе данных (по умолчанию использовалось бы имя класса).

   * `@Id` = обозначает первичный ключ.

   * `@GeneratedValue(strategy = GenerationType.IDENTITY)` = указывает, что значение `ID` генерируется базой данных (автоинкремент).

   * `@Column(unique = true)` = устанавливает уникальное ограничение на колонку `username` в БД.
</details>

<details>
  <summary>models.MyUser</summary>

    package ru.winnca.spring_security.models;
    
    import jakarta.persistence.*;
    import lombok.Data;
    
    @Data
    @Entity
    @Table(name="users")
    public class MyUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Column(unique = true)
        private String username;
        private String password;
        private String roles;
    }
</details>

20. Открываем `application.yaml`. Прописываем подключение к бд.

<details>
    <summary>connect details</summary>

   * `url` — строка подключения к базе данных:
  
     * `jdbc:postgresql://` — протокол = JDBC для PostgreSQL.

     * `localhost` — хост = где запущена БД.

     * `5432` — порт = стандартный для PostgreSQL.

     * `spring_security_db` — имя базы данных.

   * `driver-class-name` — указывает полное имя класса `JDBC-драйвера`. `Spring Boot` может определить его автоматически по `URL`, но явное указание повышает надёжность.

   * `username` — имя пользователя для подключения к `PostgreSQL`.

   * `password` — пароль пользователя `PostgreSQL`.

   * `ddl-auto: update` — политика управления схемой базы данных = обновляет схему без удаления данных (добавляет новые колонки/таблицы).
</details>

<details>
  <summary>application.yaml</summary>

    spring:
        datasource:
            driver-class-name: org.postgresql.Driver
            url: jdbc:postgresql://localhost:5432/spring_security_db
            username: postgres
            password: 12345678
        jpa:
          hibernate:
            ddl-auto: update
</details>

<details>
    <summary>Замечание</summary>
    <br>
    
   * Oбязательно создайте бд spring_security_db в PostgreSQL.
</details>

<br>

21. Создаем репозиторий, который будет взаимодействовать с бд (интерфейс):

<details>
    <summary>JpaRepository</summary>
    <br>

   * Наследуемся от `JpaRepository` — интерфейс из `Spring Data JPA`, который предоставляет готовый набор методов для выполнения `CRUD-операций` и работы с базой данных.

   * В совокупности дают большую часть реализации наших `CRUD-операций`, кроме чтения по имени пользователя.

   * Используем `Optional`, чтобы явно указать, что "этот метод может не вернуть пользователя".
</details>

<details>
  <summary>repository.UserRepository</summary>
    
    package ru.winnca.spring_security.repository;
    
    import org.springframework.data.jpa.repository.JpaRepository;
    import ru.winnca.spring_security.models.MyUser;
    
    import java.util.Optional;
    
    public interface UserRepository extends JpaRepository<MyUser, Long> {
          Optional<MyUser> readByUsername(String username);
    }
</details>

22. Создание класса `MyUserDeatilsService`, который имплементирует интерфейс `UserDeatilsService`.

<details>
  <summary>services.MyUserDetailsService</summary>

    package ru.winnca.spring_security.services;
    
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    
    public class MyUserDetailsService implements UserDetailsService {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
      }
    }
</details>

<details>
    <summary>Замечание</summary>
    <br>
    
   * Видим, что возвращает данный метод UserDetails, подходящего класса нет, поэтому надо сделать собственную реализацию этого интерфейса.
</details>

<br>

23. Создание класса `MyUserDetails`, который имплементирует интерфейс `UserDetails`.

<details>
  <summary>config.MyUserDetails</summary>
    
    package ru.winnca.spring_security.config;
    
    import org.jspecify.annotations.Nullable;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    
    import java.util.Collection;
    
    public class MyUserDetails implements UserDetails {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }
    
        @Override
        public @Nullable String getPassword() {
            return null;
        }
    
        @Override
        public String getUsername() {
            return null;
        }
    
        @Override
        public boolean isAccountNonExpired() {
            return UserDetails.super.isAccountNonExpired();
        }
    
        @Override
        public boolean isAccountNonLocked() {
            return UserDetails.super.isAccountNonLocked();
        }
    
        @Override
        public boolean isCredentialsNonExpired() {
            return UserDetails.super.isCredentialsNonExpired();
        }
    
        @Override
        public boolean isEnabled() {
            return UserDetails.super.isEnabled();
        }
    }
</details>

<br>
<br>

24. Реализация методов класса `MyUserDetails`:

<details>
    <summary>Конструктор</summary>
    <br>
    
   * Чтобы получить все эти данные для переопределённых методов, самый простой способ это в конструкторе принимать пользователя.
</details>

<details>
  <summary>code</summary>
  
    private MyUser user;

    public MyUserDetails(MyUser user){
        this.user=user;
    }
</details>
<br>
<details>
    <summary>Роли</summary>
    <br>
    
   * Роли хранятся в строковом экземпляре и должны возвращаться в какой-то коллекции, поэтому используем Arrays.stream. Тип данных, который должен храниться в коллекции обязан наследоваться от `GrantedAuthority` и тогда, к примеру, возьмём `SimpleGrantedAuthority` = сериализуется и у него есть поле роль, создаём объект этого класса.

</details>

<details>
  <summary>getAuthorities()</summary>

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRoles().split(", ")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
</details>

<details>
    <summary>Пояснение</summary>
    <br>

   * Благодаря этой записи: сплитим строку в роли на отдельные кусочки, преобразуем строковое значение в нужный класс, собираем все роли в лист, return полномочия пользователя.
</details>
<br>
<details>
    <summary>геттеры</summary>
    <br>

   * Пароль и логин возьмём у пользователя через геттер.
</details>

<details>
  <summary>getPassword() && getUsername()</summary>
  
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername();
    }
</details>
<br>
<details>
    <summary>Срок действия учётной записи</summary>
    <br>

   * `isAccountNotExpired()` = указывает истёк ли срок действия учётной записи пользователя (то есть истёкшая не может быть аутентифицирована): `true` = действительна, `false` = нет.
</details>

<details>
  <summary>isAccountNonExpired()</summary>

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
</details>
<br>
<details>
    <summary>Заблочена ли учётная запись</summary>

   * `isAccountNotLocked()` = указывает заблочена ли учётная запись: `true` = незаблокирована, `false` = ...
</details>

<details>
  <summary>isAccountNotLocked()</summary>
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
</details>
<br>
<details>
    <summary>Срок действия паоля</summary>
    <br>

   * `isCredentialNotExpired()` = указывает истёк ли срок действия пароля (просроченные аутентификации не подлежат): `true` = действительна, `false` = нет.
</details>

<details>
  <summary>isCredentialNotExpired()</summary>
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
</details>
<br>
<details>
    <summary>Пользователь доступен</summary>
    <br>

   * `isEnabled()` = включён ли пользователь или нет: `true` = да, `false` = нет.
</details>

<details>
  <summary>isEnabled()</summary>

    @Override
    public boolean isEnabled() {
        return true;
    }
</details>
<br>
<details>
  <summary>config.MyUserDetails</summary>
    
    package ru.winnca.spring_security.config;
    
    import org.jspecify.annotations.Nullable;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import ru.winnca.spring_security.models.MyUser;
    
    import java.util.Arrays;
    import java.util.Collection;
    import java.util.stream.Collectors;
    
    public class MyUserDetails implements UserDetails {
    
        private MyUser user;
    
        public MyUserDetails(MyUser user){
            this.user=user;
        }
    
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Arrays.stream(user.getRoles().split(", ")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
    
        @Override
        public @Nullable String getPassword() {
            return user.getPassword();
        }
    
        @Override
        public String getUsername() {
            return user.getUsername();
        }
    
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
    
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
    
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
    
        @Override
        public boolean isEnabled() {
            return true;
        }
    }
</details>

<br>
<br>

25. Реализуем метод `loadUserByUsername()` из класса `MyUserDetailsService`:

<details>
  <summary>services.MyUserDetailsService</summary>

    package ru.winnca.spring_security.services;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    import ru.winnca.spring_security.config.MyUserDetails;
    import ru.winnca.spring_security.models.MyUser;
    import ru.winnca.spring_security.repository.UserRepository;
    
    import java.util.Optional;
    
    @Service
    public class MyUserDetailsService implements UserDetailsService {
    
        @Autowired
        private UserRepository repository;
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<MyUser> user = repository.readByUsername(username);
            return user.map(MyUserDetails::new).orElseThrow(()->new UsernameNotFoundException(username + "not found"));
        }
    }
</details>

<br>

26. Теперь вместо создания вручную через `@Builder` в классе `SecurityConfig` возвращает объект `MyUserDetailsService()`:

<details>
  <summary>config.SecurityConfig</summary>

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder encoder){
        //        UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").build();
        //        UserDetails user = User.builder().username("user").password(encoder.encode("user")).roles("USER").build();
        //        UserDetails alex = User.builder().username("alex").password(encoder.encode("alex")).roles("USER", "ADMIN").build();
        //        return new InMemoryUserDetailsManager(admin, user, alex);
            return new MyUserDetailsService();
        }
</details>

<br>

27. Создаём контрольную точку для создания пользователей.

<details>
  <summary>controllers.AppController</summary>

    @PostMapping("/new-user")
    public String addUser(@RequestBody MyUser user){
        service.addUser(user);
        return "user saved";
    }
</details>

<br>

28. Для сервиса добавим аннотацию `@AllArgsConstructor`, чтобы внедрила репозиторий. Здесь же добавим метод по добавлению пользователя.

<details>
  <summary>services.AppService</summary>

        public void addUser(MyUser user){
            repository.save(user);
        }
</details>

<br>

29. Сделаем так, чтобы пользователя могли создать все:

<details>
  <summary>config.SecurityConfig</summary>

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            return http.csrf(AbstractHttpConfigurer::disable).
                    authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/welcome", "/api/v1/new-user").permitAll().
                            requestMatchers("/api/v1/**").authenticated()).
                    formLogin(AbstractAuthenticationFilterConfigurer::permitAll).build();
        }
</details>

<br>
<br>

---

## <a id="title25">Практика-часть-5: добавление аутентификации</a>

<details>
    <summary>Разница между идентификацией, аутентификацией и авторизацией</summary>

   * **Идентификация** = пользователь вводит логин (называет себя) = "Кто вы?".

   * **Аутентификация** = система проверяет, правильный ли пароль введён для этого логина = "Пароль верен?".

   * **Авторизация** = система проверяет, есть ли у пользователя права на действие = "Что вам разрешено?".
</details>

<details>
    <summary>Процесс входа в систему</summary>

   1. **Идентификация:** пользователь вводит логин `"admin"`.

   2. **Аутентификация:** система проверяет пароль для `"admin"` — правильный?

   3. **Авторизация:** система проверяет, есть ли у `"admin"` права доступа к той или иной странице.
</details>

<details>
    <summary>AuthenticationProvider</summary>

   * **`AuthenticationProvider`** — это компонент, который **выполняет аутентификацию** (проверяет логин и пароль).

   * `DaoAuthenticationProvider` — стандартная реализация, которая:

     - использует `UserDetailsService` (чтобы найти пользователя по логину);

     - использует `PasswordEncoder` (чтобы проверить пароль).

   > Без `AuthenticationProvider` вы не сможете войти в систему под пользователями, созданными через вашу БД.
</details>

<br>

30. В `SecurityConfig` создаём ещё один метод: `authenticationProvider()`:

<details>
    <summary>authenticationProvider()</summary>

   * Создадим экземпляр `DAO` этого провайдера, то есть создадим экземпляр класса `DaoAuthenticationProvider` = реализация провайдера, которая реализует `UserDetailsService` и `PasswordEncoder` для аутентификации имени пользователя и пароля

   * Из метода `userDetailsService()` можем удалить параметры, нет в них необходимости.
</details>

<details>
  <summary>config.SecurityConfig</summary>
    
    @Bean
    public UserDetailsService userDetailsService(){
    //        UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").build();
    //        UserDetails user = User.builder().username("user").password(encoder.encode("user")).roles("USER").build();
    //        UserDetails alex = User.builder().username("alex").password(encoder.encode("alex")).roles("USER", "ADMIN").build();
    //        return new InMemoryUserDetailsManager(admin, user, alex);
        return new MyUserDetailsService();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
</details>

<br>

31. Прежде, чем пользователя сохранять. Нужно захэшировать пароль:

<details>
  <summary>services.AppService</summary>

    public void addUser(MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }
</details>

<br>

32. Протестируем приложение с помощью Postman (не забудьте открыть pgAdmin4 и создать бд).

<details>
  <summary>Postman: sign up</summary>
  <img width="644" height="370" alt="image" src="https://github.com/user-attachments/assets/75809e93-6cad-4536-9227-5bdec554286c" />
  <br>
  <img width="652" height="389" alt="image" src="https://github.com/user-attachments/assets/cef35c36-3ab3-43ef-9cbc-df9d9f08ce6f" />
  <br>
  <img width="656" height="400" alt="image" src="https://github.com/user-attachments/assets/3e43bccb-d0cf-40d0-8d17-296e024edf55" />
</details>

<details>
  <summary>PostgreSQL</summary>
  <br>
  <img width="886" height="254" alt="image" src="https://github.com/user-attachments/assets/19b12942-f9b9-41c7-81aa-e504854a884f" />
</details>

<details>
  <summary>sign in user</summary>
  <br>
  <img width="270" height="37" alt="image" src="https://github.com/user-attachments/assets/666349db-a4e8-45dc-994d-d4cb7d35b766" />
  <br>
  <img width="338" height="217" alt="image" src="https://github.com/user-attachments/assets/eee6e5c1-d0eb-451f-9b99-5457a881a591" />
  <br>
  <img width="1906" height="471" alt="image" src="https://github.com/user-attachments/assets/67d96532-d057-447b-82e3-cc9e6657ffbf" />
  <br>
  <img width="603" height="491" alt="image" src="https://github.com/user-attachments/assets/65f81bb8-65f4-40d0-b4cd-1d0fab38cd3c" />
</details>

<details>
  <summary>sign in admin</summary>
  <br>
  <img width="259" height="28" alt="image" src="https://github.com/user-attachments/assets/3e366097-db6e-4430-ad0f-eb0225ec99bb" />
  <br>
  <img width="336" height="224" alt="image" src="https://github.com/user-attachments/assets/c847fe8a-d061-4851-952f-5c6e302aa08f" />
  <br>
  <img width="619" height="494" alt="image" src="https://github.com/user-attachments/assets/3c27b541-9b6e-448e-b89f-e97887db34e0" />
  <br>
  <img width="482" height="138" alt="image" src="https://github.com/user-attachments/assets/1482033b-e23f-480f-b9b1-dee1ea317c5b" />
</details>

<details>
  <summary>sign in alex</summary>
  <img width="259" height="28" alt="image" src="https://github.com/user-attachments/assets/1a4cb75f-6f21-49bb-bd89-cd4e65eb4c10" />
  <br>
  <img width="343" height="218" alt="image" src="https://github.com/user-attachments/assets/9e38a4a8-a441-41a7-8db0-ec788ddcee8f" />
  <br>
  <img width="1891" height="561" alt="image" src="https://github.com/user-attachments/assets/3051b2b4-bc84-42fe-a816-4b3649ec32d0" />
  <br>
  <img width="469" height="135" alt="image" src="https://github.com/user-attachments/assets/563d2982-0579-47f3-b8ea-912752f9edab" />
</details>

