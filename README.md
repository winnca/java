# java



* ### [Теория](#title0)

* ### [Часть 1](#title1)

* ### [Часть 2](#title2)

* ### [Часть 3](#title3)

<br>
<br>

## <a id="title0">Теория</a>

### **1. Структура `pom.xml` (Maven Project Object Model)**

Файл `pom.xml` — это сердце Maven-проекта. Он содержит информацию о проекте и инструкции по его сборке.

* **GroupId** — уникальный идентификатор организации или группы, создавшей проект. Следует правилам именования пакетов в Java (используется **перевернутое доменное имя**, например, `ru.winnca`). Является корнем для всех модулей и библиотек, создаваемых в рамках одной организации.

* **ArtifactId** — уникальное имя самого проекта (артефакта). Соответствует названию выходного файла сборки (JAR или WAR) — например, `students`.

* **Packaging (Тип сборки):** тип сборки **JAR** (Java ARchive).
  * В Spring Boot это стандарт, так как фреймворк включает в себя встроенный сервер Tomcat. Благодаря этому приложение можно запустить как самостоятельный JAR-файл без необходимости развертывания на внешнем сервере.
  * **WAR** (Web Application ARchive) используется для веб-приложений, которые развертываются на внешних серверах, таких как Apache Tomcat.

---

### **2. Основные понятия приложения**

В данном `README.md` описывается создание **RESTful веб-сервиса**, который обрабатывает HTTP-запросы и реализует стандартные **CRUD-операции** (Create, Read, Update, Delete).

* **REST (Representational State Transfer)** — это архитектурный стиль взаимодействия компонентов приложения в сети. Приложение, следующее принципам REST, называется **RESTful**.

Наше приложение соблюдает следующие принципы REST:

* **Клиент-серверная архитектура:** Разделение интерфейса клиента (Postman) и сервера (наше Spring Boot приложение).

* **Отсутствие состояния (Stateless):** Сервер не хранит информацию о сессии клиента (нет `HttpSession`, нет `@SessionAttributes`). Каждый запрос содержит все необходимые данные для его обработки.

* **Кэширование:** Хотя мы не реализовали кэширование явно, Spring позволяет легко добавить его (например, с помощью `@Cacheable`). Ответы сервера могут помечаться как кэшируемые или не кэшируемые, что позволяет оптимизировать нагрузку.

* **Единообразие интерфейса:** Взаимодействие с ресурсами (студентами) происходит через стандартные методы HTTP:
  * `GET` — для чтения данных (`@GetMapping`).
  * `POST` — для создания новых ресурсов (`@PostMapping`).
  * `PUT` — для обновления существующих (`@PutMapping`).
  * `DELETE` — для удаления (`@DeleteMapping`).

* **Многоуровневая система:** Наше приложение разделено на слои (контроллер → сервис → репозиторий → модель), что является примером многоуровневой архитектуры.

* **Код по требованию (необязательный принцип):** В нашем проекте не используется.

**API (Application Programming Interface)** — это программный интерфейс. Набор правил и инструментов, с помощью которого клиенты могут взаимодействовать с нашим приложением, не имея доступа к его внутреннему устройству.

**REST API** — это API, построенный по принципам REST. Контроллер, помеченный аннотацией `@RestController`, предоставляет именно такой интерфейс.

**Postman** — это инструмент (клиент) для тестирования API. Он позволяет отправлять HTTP-запросы (GET, POST, PUT, DELETE) к нашему серверу и просматривать ответы.

---

### **3. Архитектура Spring Framework**

**Spring Framework** — это платформа, состоящая из нескольких взаимосвязанных модулей, сгруппированных по функциональному признаку.

#### **3.1. Data Access/Integration (Доступ к данным и интеграция)**

Модуль, который предоставляет классы, интерфейсы и аннотации для управления доступом к данным. Упрощает взаимодействие с БД и другими источниками данных. Состоит из:

* **JDBC (Java Database Connectivity)** — стандартный Java API для взаимодействия с реляционными базами данных. Spring упрощает работу с ним через модуль `spring-jdbc`, убирая шаблонный код.

* **ORM (Object-Relational Mapping)** — технология отображения Java-объектов на таблицы в реляционной базе данных. Spring интегрируется с ORM-фреймворками, такими как Hibernate, JPA.

* **OXM (Object-XML Mapping)** — отображение Java-объектов в XML и обратно. Spring поддерживает различные реализации, например, JAXB.

* **JMS (Java Message Service)** — API для асинхронного обмена сообщениями между приложениями через брокеры сообщений (ActiveMQ, RabbitMQ).

* **Transactions** — модуль для декларативного (через `@Transactional`) и программного управления транзакциями.

#### **3.2. Web (MVC/Remoting)**

Предоставляет API и утилиты для создания веб-приложений. Состоит из:

* **Servlet** — обеспечивает поддержку традиционных веб-приложений на основе Servlet API.

* **Web** — базовый веб-модуль, предоставляющий фундаментальную веб-функциональность, мультипарт-загрузку и REST-клиент (`RestTemplate`).

* **Portlet** — интегрирует приложение с API Portlets (устаревшая технология).

* **WebSocket** — обеспечивает поддержку двусторонней (полнодуплексной) связи между клиентом и сервером в режиме реального времени.

#### **3.3. AOP (Аспектно-ориентированное программирование)**

Мощный механизм модуляции **сквозных проблем (cross-cutting concerns)** — функциональностей, которые затрагивают несколько компонентов и не могут быть полностью отделены от основной логики (логирование, безопасность, управление транзакциями). AOP позволяет выделить их в отдельные **аспекты** и применять прозрачно к нескольким компонентам. Пример в нашем проекте: `@Transactional`.

#### **3.4. Aspects**

Предоставляет набор готовых аспектов, которые могут быть использованы в приложении на основе AOP. К ним относится ведение журналов, кэширование, безопасность, **управление транзакциями**. Это модули многократного использования, которые применяются к различным частям приложения для добавления функциональности без ущерба для основного кода.

#### **3.5. Instrumentation**

Обеспечивает поддержку инструментов байт-кода, позволяя разработчикам изменять поведение скомпилированного класса во время выполнения (например, для целей отладки или мониторинга).

#### **3.6. Messaging**

Модуль для создания приложений, управляемых сообщениями. Предоставляет абстрактную основу для работы с протоколами обмена сообщениями, такими как **MQTT** (Message Queuing Telemetry Transport — облегченный протокол для обмена сообщениями между устройствами).

#### **3.7. Core Container (Основной контейнер)**

Обеспечивает базовые строительные блоки Spring Framework и включает в себя следующие компоненты:

* **Beans** — предоставляет утилиты для управления компонентами и их зависимостями (основа IoC/DI).

* **Core** — предоставляет утилиты и неинвазивные способы доступа к низкоуровневым функциональным возможностям Java.

* **Context** — надстройка над модулями Core и Beans (`ApplicationContext`).

* **Expression Language (SpEL)** — язык выражений, созданный для Spring Framework. Поддерживает запросы и управление графами объектов во время выполнения, используется для гибкой настройки компонентов.

#### **3.8. Test**

Обеспечивает поддержку тестирования приложений на базе Spring. Включает утилиты для написания модульных тестов, интеграционных тестов и сквозных тестов, поддерживает интеграцию с популярными платформами, такими как JUnit.

#### **3.9. Spring Boot (надстройка над Spring Framework)**

**Spring Boot** — это проект, построенный на основе Spring Framework, который значительно упрощает и ускоряет разработку приложений.

**Основные возможности и преимущества Spring Boot:**

* **Автоконфигурация (Auto-configuration):** Spring Boot автоматически настраивает компоненты Spring на основе добавленных зависимостей. Например, если в проекте есть `spring-boot-starter-web`, Spring Boot автоматически настроит встроенный Tomcat. Это избавляет разработчика от написания большого количества XML-конфигураций или Java-конфигураций вручную.

* **Стартеры (Starters):** Набор удобных дескрипторов зависимостей (`spring-boot-starter-*`), которые собирают совместимые версии библиотек для конкретной функциональности. В нашем проекте используется `spring-boot-starter-web` и `spring-boot-starter-data-jpa`.

* **Встроенные серверы (Embedded Servers):** Позволяет запускать приложение как самостоятельный JAR-файл со встроенным Tomcat, Jetty или Undertow. Не требуется устанавливать и настраивать отдельный веб-сервер.

* **Отсутствие шаблонного кода:** Минимизирует ручную конфигурацию, позволяя разработчику сосредоточиться на бизнес-логике.

* **Микросервисная архитектура:** Spring Boot идеально подходит для создания микросервисов благодаря своей легкости, автономности и простоте развертывания.

В нашем проекте мы используем Spring Boot для быстрого создания REST API без необходимости вручную настраивать DispatcherServlet, DataSource и другие компоненты — все это Spring Boot делает за нас автоматически.

---

### **4. Жизненный цикл Spring-приложения и управление бинами**

Важно понимать разницу между жизненным циклом всего приложения и жизненным циклом отдельного бина:

* **Жизненный цикл Spring-приложения** — это процесс от запуска JVM до её остановки, включая инициализацию контейнера, обработку запросов и завершение работы.

* **Жизненный цикл Spring-бина** — это этапы, через которые проходит каждый отдельный компонент внутри контейнера: создание, внедрение зависимостей, инициализация, использование и уничтожение.

#### **4.1. Жизненный цикл Spring-приложения (общий)**

1. **Запуск JVM**
   * Выполняется команда `java -jar students.jar`
   * Происходит загрузка классов в память

2. **Инициализация Spring Boot**
   * Запускается метод `SpringApplication.run()`
   * Создается `ApplicationContext` (IoC контейнер)
   * Выполняется настройка автоконфигурации

3. **Жизненный цикл бинов (подробно см. раздел 4.2)**
   * Сканирование компонентов с аннотациями `@Component`, `@Service`, `@Repository`, `@Controller`
   * Создание и инициализация бинов
   * Внедрение зависимостей

4. **Запуск веб-сервера (Tomcat)**
   * Старт встроенного Tomcat на порту 8080
   * Приложение готово к обработке запросов

5. **Обработка HTTP-запросов (работа приложения)**
   * `GET /api/students` — получение списка студентов
   * `POST /api/students` — создание нового студента
   * `PUT /api/students` — обновление студента
   * `DELETE /api/students/{email}` — удаление студента

6. **Получение сигнала на остановку**
   * Нажатие Ctrl+C в консоли
   * Штатное завершение приложения

7. **Завершение работы бинов**
   * Вызов destroy-методов (если они указаны)
   * Освобождение занятых ресурсов

8. **Остановка Spring контейнера**
   * Закрытие `ApplicationContext`

9. **Остановка JVM**
   * Полное завершение работы приложения

#### **4.2. Жизненный цикл Spring-бина (детально на примере `StudentController`)**

Этот процесс происходит на этапе 3 общей схемы приложения и повторяется для каждого бина в контейнере.

1. **Определение бина**
   * Spring находит класс с аннотацией `@RestController`
   * Создается определение бина на основе метаданных

2. **Создание объекта бина (Instantiation)**
   * Вызывается конструктор класса: `new StudentController()`
   * В консоли появляется сообщение: "Конструктор StudentController"

3. **Внедрение зависимостей (Dependency Injection)**
   * Spring анализирует параметры конструктора
   * Находит готовый бин `StudentService` (или создает его, если он еще не создан)
   * Внедряет зависимость: `this.studentService = studentService`

4. **Вызов `@PostConstruct` (init-method)**
   * Если в классе есть метод с аннотацией `@PostConstruct`, Spring вызывает его
   * Здесь можно выполнить логику инициализации (например, проверку подключения к БД)
   * В консоли появляется сообщение: "@PostConstruct: бин инициализирован"

5. **Бин готов к использованию**
   * `StudentController` может обрабатывать входящие HTTP-запросы
   * Методы контроллера начинают работу:
     * `findAllStudents()` → `GET /api/students`
     * `saveStudent()` → `POST /api/students`
     * `readStudent()` → `GET /api/students/{email}`
     * `updateStudent()` → `PUT /api/students`
     * `deleteStudent()` → `DELETE /api/students/{email}`

6. **Вызов `@PreDestroy` (destroy-method)**
   * При остановке приложения Spring вызывает методы с аннотацией `@PreDestroy`
   * Здесь можно освободить ресурсы (закрыть соединения, файлы и т.д.)
   * В консоли появляется сообщение: "@PreDestroy: бин уничтожается"

7. **Бин уничтожен**
   * Объект становится доступен для сборщика мусора (Garbage Collector)

#### **4.3. Пример для нашего RESTful приложения**

**При запуске приложения:**

1. JVM стартует
2. Spring Boot инициализируется
3. **Начинается жизненный цикл бинов:**
   * Создается `InMemoryStudentDAO` (вызывается конструктор)
   * Внедряются зависимости (в данном случае их нет)
   * Вызывается `@PostConstruct` (если есть)
   * Бин готов
   
   * Создается `InMemoryStudentService` (вызывается конструктор)
   * Внедряется зависимость `studentDAO`
   * Вызывается `@PostConstruct` (если есть)
   * Бин готов
   
   * Создается `StudentController` (вызывается конструктор)
   * Внедряется зависимость `studentService`
   * Вызывается `@PostConstruct` (если есть)
   * Бин готов
4. Запускается Tomcat на порту 8080
5. **Приложение работает** — обрабатывает запросы

**При остановке приложения:**

1. Получен сигнал на остановку (Ctrl+C)
2. **Начинается завершение бинов:**
   * У `StudentController` вызывается `@PreDestroy` (если есть)
   * У `InMemoryStudentService` вызывается `@PreDestroy` (если есть)
   * У `InMemoryStudentDAO` вызывается `@PreDestroy` (если есть)
3. Закрывается `ApplicationContext`
4. JVM завершает работу

#### **4.4. Ключевой вывод**

**Жизненный цикл приложения — это "контейнер", внутри которого происходит множество жизненных циклов отдельных бинов.** Бины создаются при старте приложения, живут внутри него (обрабатывая запросы) и уничтожаются при его остановке.

---

### **5. Как Spring обрабатывает HTTP-запрос и отправляет ответ (на примере нашего приложения)**

Процесс обработки входящего HTTP-запроса в Spring MVC выглядит следующим образом:

1. **Клиент отправляет запрос:** Браузер или Postman отправляет HTTP-запрос (например, `GET http://localhost:8080/api/students`) на наш сервер.

2. **Запрос попадает в `DispatcherServlet` (Front Controller):** Это главный сервлет в Spring MVC. Он принимает все входящие запросы и выступает в роли диспетчера, делегируя их дальнейшую обработку другим компонентам.

3. **`DispatcherServlet` обращается к `HandlerMapping`:** `HandlerMapping` — это компонент, который хранит соответствия между URL-путями, HTTP-методами и методами в контроллерах (благодаря аннотациям `@RequestMapping` и их производным `@GetMapping`, `@PostMapping` и т.д.). Он находит нужный метод (`findAllStudents()`) в нужном контроллере (`StudentController`), который должен обработать этот запрос.

4. **Вызов метода контроллера:** `DispatcherServlet` вызывает найденный метод в контроллере. При необходимости он также подготавливает аргументы для этого метода (например, из `@RequestBody` или `@PathVariable`).

5. **Работа бизнес-логики:**
   * Контроллер (`StudentController`) делегирует выполнение задачи соответствующему сервису (`StudentService`).
   * Сервис (`StudentServiceImpl`) содержит бизнес-логику и, если нужно, обращается к репозиторию (`StudentRepository`).
   * Репозиторий (`StudentRepository`) взаимодействует с базой данных (PostgreSQL), выполняя необходимые операции (SELECT, INSERT, UPDATE, DELETE).
   * **Взаимодействие с БД:** Репозиторий выполняет запрос к базе данных и получает результат (сущности `Student`).

6. **Формирование ответа:** Результат (список студентов или один студент) проходит обратный путь:
   * **БД → Репозиторий → Сервис → Контроллер.**
   * Контроллер возвращает полученные объекты (например, `List<Student>`).

7. **`DispatcherServlet` использует `HttpMessageConverter`:** Spring автоматически конвертирует возвращаемые Java-объекты в JSON с помощью библиотеки Jackson (`HttpMessageConverter`), так как контроллер помечен `@RestController` (или метод — `@ResponseBody`).

8. **Отправка ответа клиенту:** `DispatcherServlet` отправляет сформированный HTTP-ответ (с JSON-данными и статусом 200 OK) обратно клиенту (Postman или браузеру).

---

### **6. Зависимости Spring Boot (Dependencies)**

Зависимости — это внешние библиотеки и модули, которые подключаются к проекту. Spring Boot управляет их версиями автоматически, что упрощает разработку.

#### **6.1. Spring Web**

Это стартовый пакет (starter) для создания веб-приложений. Он включает в себя все необходимые компоненты для обработки HTTP-запросов и создания REST API.

**Что входит в состав Spring Web:**

* **Spring MVC (Model-View-Controller):** Фреймворк, реализующий паттерн MVC. В нашем случае мы используем его для создания контроллеров (`@RestController`), которые обрабатывают входящие HTTP-запросы.

* **Встроенный контейнер сервлетов (Embedded Container):** По умолчанию это Apache Tomcat. Он позволяет запускать веб-приложение как самостоятельный JAR-файл без установки отдельного веб-сервера.

* **Библиотека Jackson:** Обеспечивает автоматическую **сериализацию** (преобразование Java-объектов в JSON) и **десериализацию** (преобразование JSON в Java-объекты). Это позволяет нашему API легко обмениваться данными в формате JSON.

* **Базовые модули Spring (Core, Beans, Context):** Фундамент, на котором строится весь фреймворк, обеспечивающий внедрение зависимостей (Dependency Injection) и управление жизненным циклом бинов.

* **Hibernate Validator:** Реализация спецификации Bean Validation для проверки корректности данных.

* **Логирование (Logback, SLF4J):** Библиотеки для логирования работы приложения.

#### **6.2. Lombok**

Lombok — это библиотека, которая позволяет сократить шаблонный код (boilerplate code) в Java-классах с помощью аннотаций. Вместо того чтобы вручную писать геттеры, сеттеры, конструкторы и другие стандартные методы, мы просто ставим аннотацию.

**Основные аннотации Lombok, используемые в проекте:**

* **`@Data`** — "золотая пуля". Включает в себя сразу несколько аннотаций: `@ToString`, `@EqualsAndHashCode`, `@Getter` (для всех полей), `@Setter` (для всех не final полей) и `@RequiredArgsConstructor`. Делает класс изменяемым (mutable).

* **`@Builder`** — реализует паттерн проектирования "Строитель". Позволяет создавать объекты с большим количеством полей более читаемым и гибким способом (как в примере с `Student.builder().firstName(...).build()`).

* **`@NonNull`** — аннотация для поля или параметра. Добавляет проверку на `null` в сгенерированный конструктор или метод. Если передать `null`, будет выброшено исключение `NullPointerException`.

* **`@AllArgsConstructor`** — генерирует конструктор, который принимает **все** поля класса в качестве аргументов.

  * **Важно для Spring:** В нашем проекте мы используем `@AllArgsConstructor` в контроллере (`StudentController`) и сервисе (`StudentServiceImpl`). Spring автоматически выполнит **внедрение зависимостей через конструктор (Constructor Injection)**. То есть, когда Spring будет создавать бин `StudentController`, он увидит единственный конструктор со всеми параметрами и автоматически передаст в него нужные зависимости (в нашем случае — `StudentService`). Это считается лучшей практикой, и нам **не нужно** явно писать `@Autowired` над конструктором — Spring сам его вызовет.

* **`@NoArgsConstructor`** — генерирует конструктор без аргументов. Требуется JPA для создания сущностей через рефлексию.

* **`@Getter` / `@Setter`** — генерируют геттеры и сеттеры для полей класса.

**Почему это удобно:** Lombok значительно сокращает объем кода, уменьшает риск ошибок и делает классы более чистыми и читаемыми. В сочетании с внедрением зависимостей через конструктор (который генерирует `@AllArgsConstructor`) код становится не только компактным, но и более тестируемым и соответствующим лучшим практикам Spring.

---

<br>
<br>

## <a id="title1">Часть 1. Создание проекта и проверка работоспобности. Создание модели и контроллера.</a>

<br>

1. Создаём через start.spring.io zip-архив с проектом. Добавляем в него Spring Web. Скачиваем, разархивируем, открываем с помощью Intellij IDEA.

<details>
    <summary>start.spring.io</summary>
    <br>
    <img width="1530" height="767" alt="image" src="https://github.com/user-attachments/assets/4d8b8851-1f0a-4cfa-b696-06c967718b5a" />
</details>

<br>

2. Возьмём зависимость Lombok версия 1.18.30 через "https://mvnrepository.com/".

<details>
    <summary>maven repository</summary>
    <br>
    <img width="1316" height="266" alt="image" src="https://github.com/user-attachments/assets/93036855-e553-44dc-9ba9-cf04a0b8cfe1" />
    <br>
    <img width="558" height="220" alt="image" src="https://github.com/user-attachments/assets/19010e4c-856a-4f7a-93b7-b1fe487cad77" />
</details>

<br>

3. Добавим зависимость в pom.xml.

<details>
    <summary>pom.xml</summary>
    <br>
    <img width="619" height="424" alt="image" src="https://github.com/user-attachments/assets/8abde529-6a54-4101-8789-c9fbbc679609" />
</details>

<br>

4. Создаём пакет model. В нём класс Student.

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

<br>

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

<br>

6. Перед запуском убедитесь, что структура проекта выглядит следующим образом:

<details>
    <summary>structure</summary>
    <br>
    <img width="457" height="424" alt="image" src="https://github.com/user-attachments/assets/ef590cfe-25c6-4284-b1a4-5bd6a8ff8473" />
</details>

<br>

7. Запуск проекта через класс SpringStudentsApplication. Открываем браузер, в поисковой строке: "http://localhost:8080/api/students".

<details>
    <summary>start project</summary>
    <br>
    <img width="368" height="372" alt="image" src="https://github.com/user-attachments/assets/53dd2b70-a987-4566-a40e-0c9fa2231acf" />
</details>

<br>

<br>

## <a id="title2">Часть 2. Создание CRUD с использованием сервиса и репозитория (помимо модели и контроллера).</a>

<br>

8. Содержание бизнес-логики на уровне контроллера в маленьком приложении допускается, но когда масштаб больше, то надо отделять фасад, с которым будут работать пользователи или API от бизнес-логики. Для этого используют **сервисы**.

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

9. Чтобы приложение было легко расширяемым, воспользуемся интерфейсами в package service.

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

10. Создаём методы CRUD (создание, обновление, чтение, удаление). Пока только в сервисе.

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

11. Хранение объектов в сервисе некорректно, лучше делать этого через репозитории. Своего рода классы, обеспечивающие доступ к данным. Либо взаимодействует с базой данных (MySQL, PostgreSQL, ...) или с памятью.

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

12. Использование репозитория в сервисе.

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

13. Использование сервиса в контроллере.

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

14. Скачивае приложение Postman. Нажимаем на кнопку "New", выбираем "HTTP".

<details>
    <summary>Настройка Postman</summary>
    <br>
    <img width="944" height="606" alt="image" src="https://github.com/user-attachments/assets/e072ebd0-b017-432e-ac03-164cc035489b" />
</details>

<br>

15. Тестируем API. Выбираем POST запрос, выбираем "raw", вводим в формате JSON нового пользователя, нажимаем "Send".

<details>
    <summary>Настройка HTTP</summary>
    <br>
    <img width="855" height="648" alt="image" src="https://github.com/user-attachments/assets/83bffb54-c660-4190-8635-32fa40a2e220" />
</details>

<br>

16. Проверяем.

<details>
    <summary>Тестим API</summary>
    <br>
    <img width="847" height="639" alt="image" src="https://github.com/user-attachments/assets/07ef550d-d727-4d26-95ce-ac45efd03eb0" />
    <br>
    <img width="515" height="247" alt="image" src="https://github.com/user-attachments/assets/6d0b9353-5f7e-4671-a00d-05e5cf71d60f" />
</details>

<br>

> 17. Добавим ещё нового пользователя для наглядности, которые храняться в памяти.

<details>
    <summary>create</summary>
    <br>
    <img width="851" height="612" alt="image" src="https://github.com/user-attachments/assets/ce997041-1c91-4ff5-8fb8-0bf2f7352d27" />
    <br>
    <img width="838" height="662" alt="image" src="https://github.com/user-attachments/assets/7147ec5f-b00e-4bfd-938f-6f18980732fd" />
    <br>
    <img width="329" height="302" alt="image" src="https://github.com/user-attachments/assets/7e84de27-f114-47e2-91d6-5d69030ceeb6" />
</details>

>> Обратите внимание, что можем создавать нового пользователя, прописывая только некоторые поля (обязательно email, так как аннотация @NonNull):

<details>
    <summary>details</summary>
    <img width="843" height="513" alt="image" src="https://github.com/user-attachments/assets/5d7506bb-954e-4a1c-a43b-ab472847a005" />
</details>

> 18. Просмотрим данные одного из пользователей.

<details>
    <summary>read</summary>
    <br>
    <img width="654" height="509" alt="image" src="https://github.com/user-attachments/assets/5a8e0530-8bc7-4436-afb9-e04654f4d929" />
</details>

> 19. Изменим данные о пользователе.

<details>
    <summary>update</summary>
    <br>
    <img width="646" height="527" alt="image" src="https://github.com/user-attachments/assets/bdbf73cf-77d3-445d-a146-2b22a17f45ef" />
    <br>
    <img width="654" height="719" alt="image" src="https://github.com/user-attachments/assets/921dfeb4-64fc-47ea-8a4b-0a256ede3673" />
    <br>
    <img width="321" height="313" alt="image" src="https://github.com/user-attachments/assets/5be3aff4-3f03-4f15-bcbe-5e355baf7daf" />
</details>

> 20. Удалим пользователя.

<details>
    <summary>delete</summary>
    <br>
    <img width="648" height="397" alt="image" src="https://github.com/user-attachments/assets/51e059d6-f695-4312-9eae-a804c6c2e580" />
    <br>
    <img width="653" height="557" alt="image" src="https://github.com/user-attachments/assets/fa465273-aa05-4df6-8986-d5bd8726789d" />
</details>

<br>

<br>

## <a id="title3"> Часть 3. Вместо использование оперативной памяти, репозиторий взаимодействует с БД.</a>

<br>

21. Вместо хранение объектов в оперативной памяти, воспользуемся БД. Будем использовать Spring Data JPA и PostgreSQL.

<details>
    <summary>pom.xml</summary>
    <br>
    <img width="486" height="107" alt="image" src="https://github.com/user-attachments/assets/f41b0c02-841a-4690-b418-954b13809118" />
    <br>
    <img width="337" height="102" alt="image" src="https://github.com/user-attachments/assets/2ae2ef3f-4397-4d74-9a95-253bef4b58fa" />
</details>

<br>

22. Перейдём в application.yaml настроим подключение к БД.

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

23. Изменим модель, чтобы java класс можно преобразовать в таблицу или сущность, то есть Hibernate и Spring Data Jpa могли манипулировать этими объектами. Будет использовать аннотации.

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

24. Добавим репозиторий студентов для взаимодействия с БД - JpaRepository. Добавим в него методы CRUD.

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

25. Создадим сервис для взаимодействия с этим репозиторием. Пометим аннотацией @Primary - главный сервис для взаимодействия, если бд прекратит свою работу, то будет работать второй сервис.

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

26. Приложение готово, можно открывать pgAdmin4 (вводим пароль, создаём бд student-db) и Postman для тестирования. Запускаем приложение.

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

27. Тестим.

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
