# Event Decoration Planner

A small Spring Boot MVC application for planning personal celebration events. It follows the compact ER model supplied with the project: users, themes, events, decoration items, selected event decorations and bookings.

## Included in this MVP

- Create, view, edit and delete event drafts.
- Choose a theme and add or remove decoration items from an event.
- Create one booking request per event and track booking status separately from payment status.
- Responsive Thymeleaf pages for the overview, events, themes, decorations and bookings.
- Seeded demo customer, themes and catalogue items.
- Layered Java packages for entities, repositories, services, service implementations, controllers and configuration.
- File-backed H2 database by default; optional MySQL profile.

Authentication, provider accounts, quote comparison, notifications and payment processing are not included in this first project slice. The app uses a seeded demo customer as the owner of newly created events. A booking request remains `PENDING` until its status is changed in the bookings page, and payment remains `PENDING` because no gateway is configured.

## Requirements

- Java 17 or newer
- Maven 3.6.3 or newer

## Run with MySQL

The MySQL profile is active by default. In PowerShell, set the database credentials for the current terminal and start the app:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-password"
mvn.cmd '-Dmaven.repo.local=target/maven-repository' 'spring-boot:run'
```

Open [http://localhost:8080](http://localhost:8080). Demo themes and decoration items are inserted when their tables are empty. The default MySQL URL targets `localhost:3306/event_planner`; override it with `DB_URL` if needed.

## Run with H2 instead

Disable the MySQL profile for a local file-backed H2 database and run:

```powershell
mvn.cmd '-Dmaven.repo.local=target/maven-repository' '-Dspring-boot.run.profiles=' 'spring-boot:run'
```

H2 stores its data under `./data`; the console is at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) with JDBC URL `jdbc:h2:file:./data/eventplanner;MODE=MySQL;DATABASE_TO_LOWER=TRUE`, username `sa`, and an empty password.

## Package layout

```text
com.eventplanner
├── config          # demo data initializer
├── controller      # Spring MVC routes
├── dto             # web form objects and validation
├── entity           # JPA entities and status enums
├── exception        # not-found and request error handling
├── repository      # Spring Data JPA repositories
└── service          # service interfaces
    └── impl         # service implementations
```

Thymeleaf pages live in `src/main/resources/templates`; the stylesheet is in `src/main/resources/static/css`.
