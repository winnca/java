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

### Цель проекта

Научиться на практике:

- подключать и настраивать Spring Security;

- управлять аутентификацией и авторизацией пользователей;

- разграничивать доступ к эндпоинтам в зависимости от ролей;

- создавать и хранить пользователей в базе данных;

- хешировать пароли с помощью BCrypt;

- использовать кастомные `UserDetailsService` и `UserDetails`.

### Стек технологий

| Технология | Назначение |
|------------|------------|
| **Java 17+** | язык разработки |
| **Spring Boot** | основа приложения |
| **Spring Security** | безопасность, аутентификация, авторизация |
| **Spring Data JPA / Hibernate** | работа с базой данных |
| **PostgreSQL** | СУБД для хранения пользователей |
| **Maven** | сборка проекта и управление зависимостями |
| **Lombok** | сокращение шаблонного кода |
| **JavaFaker** | генерация тестовых данных |

### Структура обучения

Проект разбит на 5 практических частей:

1. **Создание каркаса приложения** — модель, сервис, контроллер, генерация 100 тестовых приложений.

2. **Настройка логина и пароля** — через `application.yaml` и через `SecurityConfig`.

3. **Права доступа к ресурсам** — `SecurityFilterChain`, `@PreAuthorize`, роли пользователей.

4. **Создание своих пользователей** — подключение БД, JPA, репозитории, кастомные `UserDetailsService` и `UserDetails`.

5. **Добавление аутентификации** — `AuthenticationProvider`, хеширование паролей, тестирование через Postman.

### Что вы сможете делать после изучения

- Настраивать безопасность в любом Spring-приложении "с нуля".

- Разграничивать доступ для разных ролей (USER, ADMIN).

- Хранить пользователей в реальной базе данных (не в памяти).

- Понимать разницу между аутентификацией и авторизацией.

- Использовать BCrypt для безопасного хранения паролей.

### Важно

> Проект носит **исключительно учебный характер**. В реальном приложении:
> - нельзя отключать CSRF при использовании браузерных форм;
> - нужно выносить секреты (пароль БД и т.д.) в переменные окружения;
> - рекомендуется добавлять валидацию входных данных;
> - логику загрузки тестовых данных (JavaFaker) не следует использовать в production.

<br>
<br>

---

## <a id="title12">Spring Security</a>

**Spring Security** — это фреймворк для обеспечения безопасности Spring-приложений.

#### Две главные задачи:

1. **Аутентификация** — проверка, что пользователь — это тот, за кого себя выдаёт (обычно по логину/паролю).
2. **Авторизация** — проверка, имеет ли пользователь право делать то, что пытается сделать (доступ к эндпоинтам, данным и т.д.).

#### Ключевые компоненты (простым языком):

| Компонент | Что делает | Пример из вашего кода |
|-----------|-----------|----------------------|
| `SecurityFilterChain` | Цепочка фильтров, через которую проходит каждый запрос | `securityFilterChain()` в `SecurityConfig` |
| `UserDetailsService` | "Сервис поиска пользователей" — находит пользователя по логину | `MyUserDetailsService` |
| `UserDetails` | "Обёртка пользователя" — хранит логин, пароль, роли, статусы | `MyUserDetails` |
| `PasswordEncoder` | "Кодировщик паролей" — хеширует пароли (необратимо) | `BCryptPasswordEncoder` |
| `AuthenticationProvider` | "Механизм проверки" — использует `UserDetailsService` + `PasswordEncoder` | `DaoAuthenticationProvider` |

#### Важно понимать:

- **Хеширование ≠ Шифрование** — хеш нельзя расшифровать обратно в пароль.

- **BCrypt** специально сделан медленным — это усложняет подбор паролей злоумышленниками.

- **CSRF** можно отключать только в stateless API (без сессий и кук).

<br>
<br>

---

## <a id="title21">Практика-часть-1: создание каркаса приложения</a>

1. Открываем браузер и вводим: `start.spring.io` в адресной строке:

* выбираем проект `Maven`, язык `Java` и версию, стабильную версию `Spring Boot`, конфигурационный файл `YAML`, тип сборки `JAR`.

* вводим метаданные проекта.

* добавляем зависимости `Spring Web`, `Spring Security`, `Lombok`.

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

3. Добавим зависимость `javafaker` в `pom.xml`: https://mvnrepository.com/artifact/com.github.javafaker/javafaker/1.0.2.

* позволяет генерировать случайные строки и числа.

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

4. Реализуем логику приложения. Для этого создадим app-сервис. В этом сервисе реализуем:

* метод, который добавить 100 случайных приложений в коллекцию. Для этого метода добавим аннотацию `@PostConstruct` = гарантирует вызов метода 1 раз после инициализации всех компонентов.

* метод, который вернёт все приложения.

* метод, который вернёт приложение по идентификатору.

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

Замечание: подобная реализация методов должна находиться в репозитории, но пока сконцетрируемся на реализации `Spring Security`.

5. Создаём контрольные точки:

* 1 контрольная точка будет возвращать строку.

* 2 контрольная точка будет возвращать из сервиса все приложения.

* 3 контрольная точка возвращать приложение по идентификатору.

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

6. Протестируем приложение, запускаем.

* Если проект не запускается из-за lombok, то добавьте версию `<version>1.18.36</version>` в зависимость и плагин.

* в терминале всё в порядке, кроме строчки: `Using generated security password: d9287221-e8fe-41bf-9aec-7c401083af9c`. Пароль для входа.

* логин по умолчанию `Spring Security`: `user`, пароль указан выше. нажимаем `sign in`.
<details>
    <summary>terminal</summary>
    <br>
    [![img.png](img.png)](https://github.com/winnca/java/blob/spring_security/README/img.png)
    <br>
    ![img_1.png](img_1.png)
</details>

<details>
    <summary>website</summary>
    <br>
    ![img_2.png](img_2.png)
    <br>
    ![img_3.png](img_3.png)
    <br>
    ![img_4.png](img_4.png)
    <br>
    ![img_5.png](img_5.png)
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

8. Протестируем:

<details>
    <summary>sign in</summary>
    <br>
    ![img_6.png](img_6.png)
    <br>
    ![img_7.png](img_7.png)
    <br>
    ![img_8.png](img_8.png)
    <br>
    ![img_9.png](img_9.png)
    <br>
    ![img_10.png](img_10.png)
</details>

Вывод:

* нет случайного сгенерированного пароля, вошли под своим логином и пароль.

* такой подход допустим только в своих тестах и создаёт только 1 аккаунт, на деле в реальных приложениях и тестах не делают.

* правильно делать через `SecurityConfig`.

<br>

## Настройка логина и пароля через SecurityConfig

9. Создаём пакет `config` и класс `SecurityConfig`. 

* на класс добавим аннотации: `@Configuration` и `@EnableWebSecurity` = используются для настройки `Spring Security`.

* `@Configuration` является конфигурационным бином.

* `@EnableWebSecurity` применение глобальной web безопасности.

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

9.1. 2 метода: 1 - создаёт пользователя и сохранит в приложении, 2 - возвращает объект, который является кодировщиком паролей и выполняет хэширование алгоритмом `BCrypt`.

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

9.2. 1 метод: используется интерфейс `UserDetailsService` = позволяет предоставить сведения о пользователе в контексте безопасности (имя, пароль, статус учётной записи, роли пользователя, ...).

  * принимает метод `PasswordEncoder` = интерфейс для одностороннего преобразования пароля. Для хранения пароля необходимо сравнивать с паролем предоставленным пользователем.

  * `UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).build();` = по такой схеме можно создавать сколько угодно пользователей. Пароль хэшируется.

  * Если пароль не хэшировать, тогда в бд будет обычным (пароль, приходящий от пользователя во время входа в систему, при сравнении с паролем из бд будут не совпадать, даже если они одинаковы в обычном виде) = (потому что пароль во время входа в систему передаётся в зашифрованном виде, а хранящийся в бд нет).
  
  * `InMemoryUserDetailsMananger` = класс для хранения и управления всеми пользователями. Под правильным логином и паролем сможем войти в систему. 

9.3. 2 метод: уже говорили. Дополним, что оба метода должны быть помечены аннотацией `@Bean`, чтобы находиться в контексте приложения.

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

10. Удалим данные из `apllication.yaml` по созданию пользователя. Перезапустим приложение. Протестируем.

<details>
  <summary>architecture</summary>
  <br>
  ![img_11.png](img_11.png)
</details>

<details>
  <summary>sign in alex</summary>
  <br>
  ![img_12.png](img_12.png)
  <br>
  ![img_13.png](img_13.png)
  <br>
  ![img_14.png](img_14.png)
  <br>
  ![img_15.png](img_15.png)
</details>

<details>
  <summary>sign in admin</summary>
  <br>
  ![img_16.png](img_16.png)
  <br>
  ![img_17.png](img_17.png)
  <br>
  ![img_18.png](img_18.png)
  <br>
  ![img_19.png](img_19.png)
</details>

<details>
  <summary>sign in user</summary>
  <br>
  ![img_20.png](img_20.png)
  <br>
  ![img_21.png](img_21.png)
  <br>
  Исправим на: UserDetails user = User.builder().username("user").password(encoder.encode("user")).build();
  <br>
  ![img_22.png](img_22.png)
  <br>
  ![img_23.png](img_23.png)
  <br>
  ![img_24.png](img_24.png)
</details>

### Замечание:

* Но, что если хотим сделать доступ в контрольную точку всем пользователям (зарегистрированным и нет).

<br>
<br>

---

## <a id="title23">Практика-часть-3: права доступа к ресурсу</a>

<br>

## Настройка SpringFilterChain

11. Создадим 3-ий метод в конфигурационном классе.

* `SecurityFilterChain` = интерфейс для создания фильтра. В параметрах принимает `HttpSecurity` = позволяет конфигугрировать аутентификацию и авторизацию запросов. Пометим аннотации `@Bean`.

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

### Что такое CSRF (Cross-Site Request Forgery)?

Это тип атаки, при котором злоумышленник заставляет пользователя невольно выполнить действие на сайте, где тот уже авторизован.

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

**Почему в нашем проекте мы отключаем CSRF (`.csrf(AbstractHttpConfigurer::disable)`)?**

- Мы используем `stateless` `REST API` (без сессий и кук).

- В такой архитектуре `CSRF`-атаки не актуальны.

- **Предупреждение:** В приложениях с формами входа (``Thymeleaf`) отключать `CSRF` нельзя!

Реализация метода:

a. В нём отключаем `CSRF` защиту. Допустимо, где нет кук/сессий/браузеров. Нельзя при использовании приложений с формами (например, интернет-банк) и сессиями для аутентификации.

b. Впускает всех по адресу: `"/api/v1/welcome"`.

c. По остальным адресам впускает только авторизированных пользователей.

d. Все желающие могут авторизироваться: `.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)`:

   * включает стандартную страницу логина `Spring Security`.

   * `permitAll()` - разрешает ВСЕМ доступ к странице логина.

12. Перезапустим приложение и протестируем.

<details>
  <summary>sign in</summary>
  <br>
  ![img_25.png](img_25.png)
  <br>
  ![img_26.png](img_26.png)
  <br>
  ![img_27.png](img_27.png)
  <br>
  ![img_28.png](img_28.png)
  <br>
  ![img_29.png](img_29.png)
  <br>
  ![img_30.png](img_30.png)
</details>

### Замечание:

* Что если надо дать доступ к конкретным контрольным точкам людям с определёнными правами.

<br>

## Доступ к endpoints пользователям с определёнными правами.

### Пояснение про `@PreAuthorize`

Аннотация `@PreAuthorize` проверяет права доступа **перед** выполнением метода.

| Синтаксис | Что проверяет | Пример |
|-----------|---------------|--------|
| `hasAuthority('ROLE_ADMIN')` | Есть ли у пользователя конкретное право | `@PreAuthorize("hasAuthority('ROLE_ADMIN')")` |
| `hasRole('ADMIN')` | То же самое, но префикс `ROLE_` добавляется автоматически | `@PreAuthorize("hasRole('ADMIN')")` |

**В вашем коде:**

- `hasAuthority('ROLE_ADMIN')` и `hasAuthority('ROLE_USER')` — проверяют конкретные права.

- `hasAuthority('ROLE_ADMIN')` — доступ только у ADMIN.

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
  <summary></summary>
    
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

16. Протестируем. Зайдя под `user` будет доступно только просмотр всех приложений. Зайдя под `admin` просмотр приложения по идентификатору. Зайдя под `alex` будут доступны оба способа.

<details>
  <summary>sign in user</summary>
  <br>
  ![img_31.png](img_31.png)
  <br>
  ![img_32.png](img_32.png)
  <br>
  ![img_33.png](img_33.png)
  <br>
  ![img_34.png](img_34.png)
</details>

<details>
  <summary>sign in admin</summary>
  <br>
  ![img_35.png](img_35.png)
  <br>
  ![img_36.png](img_36.png)
  <br>
  ![img_37.png](img_37.png)
  <br>
  ![img_38.png](img_38.png)
</details>

<details>
  <summary>sign in alex</summary>
  <br>
  ![img_39.png](img_39.png)
  <br>
  ![img_40.png](img_40.png)
  <br>
  ![img_41.png](img_41.png)
  <br>
  ![img_42.png](img_42.png)
</details>

<br>
<br>

---

## <a id="title24">Практика-часть-4: создаём своих пользователей</a>

#### Создание своих пользователей через отдельный контроллер, а не напрямую через  `UserDetailsService`.

17. Подключим `Spring Data Jpa`:

### Spring Data JPA и Hibernate

**JPA (Java Persistence API)** — это спецификация (набор интерфейсов и правил). Она описывает, как Java-объекты должны отображаться на таблицы базы данных. Сама JPA не содержит реализации — это только стандарт.

**Hibernate** — это самая популярная реализация спецификации JPA (ORM-фреймворк). Он выполняет низкоуровневую работу:

- Преобразует Java-классы (с аннотацией `@Entity`) в таблицы базы данных.

- Преобразует вызовы методов (`repository.save(user)`) в SQL-запросы (`INSERT INTO users...`).

- Преобразует результаты SQL-запросов обратно в Java-объекты.

**Зачем это нужно:** Вы пишете код на Java, а Hibernate сам переводит его в SQL. Это ускоряет разработку и упрощает смену базы данных.

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

19. Создадим в `models` класс `MyUser`:

* `@Entity` = указывает, что класс является JPA-сущностью и будет отображён на таблицу в БД.

* `@Table(name="users")` = задаёт имя таблицы в базе данных (по умолчанию использовалось бы имя класса).

* `@Id` = обозначает первичный ключ.

* `@GeneratedValue(strategy = GenerationType.IDENTITY)` = указывает, что значение `ID` генерируется базой данных (автоинкремент).

* `@Column(unique = true)` = устанавливает уникальное ограничение на колонку `username` в БД.

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

* `url` — строка подключения к базе данных:
  
  * `jdbc:postgresql://` — протокол = JDBC для PostgreSQL.

  * `localhost` — хост = где запущена БД.

  * `5432` — порт = стандартный для PostgreSQL.

  * `spring_security_db` — имя базы данных.

* `driver-class-name` — указывает полное имя класса `JDBC-драйвера`. `Spring Boot` может определить его автоматически по `URL`, но явное указание повышает надёжность.

* `username` — имя пользователя для подключения к `PostgreSQL`.

* `password` — пароль пользователя `PostgreSQL`.

* `ddl-auto: update` — политика управления схемой базы данных = обновляет схему без удаления данных (добавляет новые колонки/таблицы).

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

#### Замечание: обязательно создайте бд spring_security_db в PostgreSQL.

21. Создаем репозиторий, который будет взаимодействовать с бд (интерфейс):

* Наследуемся от `JpaRepository` — интерфейс из `Spring Data JPA`, который предоставляет готовый набор методов для выполнения `CRUD-операций` и работы с базой данных.

* В совокупности дают большую часть реализации наших `CRUD-операций`, кроме чтения по имени пользователя.

* Используем `Optional`, чтобы явно указать, что "этот метод может не вернуть пользователя".

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

#### Замечание: видим, что возвращает данный метод UserDetails, подходящего класса нет, поэтому надо сделать собственную реализацию этого интерфейса.

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

24. Реализация методов класса `MyUserDetails`:

24.1. Чтобы получить все эти данные для переопределённых методов, самый простой способ это в конструкторе принимать пользователя.

<details>
  <summary>code</summary>
  
    private MyUser user;

    public MyUserDetails(MyUser user){
        this.user=user;
    }
</details>

24.2. Роли хранятся в строковом экземпляре и должны возвращаться в какой-то коллекции, поэтому используем Arrays.stream. Тип данных, который должен храниться в коллекции обязан наследоваться от `GrantedAuthority` и тогда, к примеру, возьмём `SimpleGrantedAuthority` = сериализуется и у него есть поле роль, создаём объект этого класса.

<details>
  <summary>getAuthorities()</summary>

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRoles().split(", ")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
</details>

Благодаря этой записи:
* сплитим строку в роли на отдельные кусочки
* преобразуем строковое значение в нужный класс 
* собираем все роли в лист
* return полномочия пользователя

24.3. Пароль и логин возьмём у пользователя через геттер.

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

24.4. `isAccountNotExpired()` = указывает истёк ли срок действия учётной записи пользователя (то есть истёкшая не может быть аутентифицирована): `true` = действительна, `false` = нет.

<details>
  <summary>isAccountNonExpired()</summary>

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
</details>

24.5. `isAccountNotLocked()` = указывает заблочена ли учётная запись: `true` = незаблокирована, `false` = ...

<details>
  <summary>isAccountNotLocked()</summary>
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
</details>

24.6. `isCredentialNotExpired()` = указывает истёк ли срок действия пароля (просроченные аутентификации не подлежат): `true` = действительна, `false` = нет.

<details>
  <summary>isCredentialNotExpired()</summary>
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
</details>

24.7. `isEnabled()` = включён ли пользователь или нет: `true` = да, `false` = нет.

<details>
  <summary>isEnabled()</summary>

    @Override
    public boolean isEnabled() {
        return true;
    }
</details>

24.8. Полный код класса:

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

26. Создаём контрольную точку для создания пользователей.

<details>
  <summary>controllers.AppController</summary>

    @PostMapping("/new-user")
    public String addUser(@RequestBody MyUser user){
        service.addUser(user);
        return "user saved";
    }
</details>

27. Для сервиса добавим аннотацию `@AllArgsConstructor`, чтобы внедрила репозиторий. Здесь же добавим метод по добавлению пользователя.

<details>
  <summary>services.AppService</summary>

        public void addUser(MyUser user){
            repository.save(user);
        }
</details>

28. Сделаем так, чтобы пользователя могли создать все:

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

### Разница между идентификацией, аутентификацией и авторизацией

| Термин | Что делает | Вопрос |
|--------|------------|--------|
| **Идентификация** | Пользователь вводит логин (называет себя) | "Кто вы?" |
| **Аутентификация** | Система проверяет, правильный ли пароль введён для этого логина | "Пароль верен?" |
| **Авторизация** | Система проверяет, есть ли у пользователя права на действие | "Что вам разрешено?" |

### Процесс входа в систему (по шагам):

1. **Идентификация:** пользователь вводит логин `"admin"`

2. **Аутентификация:** система проверяет пароль для `"admin"` — правильный?

3. **Авторизация:** система проверяет, есть ли у `"admin"` права доступа к той или иной странице

### Про `AuthenticationProvider`

**`AuthenticationProvider`** — это компонент, который **выполняет аутентификацию** (проверяет логин и пароль).

`DaoAuthenticationProvider` — стандартная реализация, которая:

- использует `UserDetailsService` (чтобы найти пользователя по логину);

- использует `PasswordEncoder` (чтобы проверить пароль).

Без `AuthenticationProvider` вы не сможете войти в систему под пользователями, созданными через вашу БД.

29. В `SecurityConfig` создаём ещё один метод: `authenticationProvider()`:

* Создадим экземпляр `DAO` этого провайдера, то есть создадим экземпляр класса `DaoAuthenticationProvider` = реализация провайдера, которая реализует `UserDetailsService` и `PasswordEncoder` для аутентификации имени пользователя и пароля

* Из метода `userDetailsService()` можем удалить параметры, нет в них необходимости.

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

30. Прежде, чем пользователя сохранять. Нужно захэшировать пароль:

<details>
  <summary>services.AppService</summary>

    public void addUser(MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }
</details>

31. Протестируем приложение с помощью Postman (не забудьте открыть pgAdmin4 и создать бд).

<details>
  <summary>Postman: sign up</summary>
  ![img_43.png](img_43.png)
  <br>
  ![img_44.png](img_44.png)
  <br>
  ![img_45.png](img_45.png)
</details>

<details>
  <summary>PostgreSQL</summary>
  <br>
  ![img_46.png](img_46.png)
</details>

<details>
  <summary>sign in user</summary>
  <br>
  ![img_47.png](img_47.png)
  <br>
  ![img_48.png](img_48.png)
  <br>
  ![img_49.png](img_49.png)
  <br>
  ![img_50.png](img_50.png)
</details>

<details>
  <summary>sign in admin</summary>
  <br>
  ![img_51.png](img_51.png)
  <br>
  ![img_52.png](img_52.png)
  <br>
  ![img_53.png](img_53.png)
  <br>
  ![img_54.png](img_54.png)
</details>

<details>
  <summary>sign in alex</summary>
  ![img_51.png](img_51.png)
  <br>
  ![img_55.png](img_55.png)
  <br>
  ![img_56.png](img_56.png)
  <br>
  ![img_57.png](img_57.png)
</details>
