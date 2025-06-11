
# AutoParts - Java & MySQL Microservice Application

AutoParts е Java-базиран микросървис за управление на автомобилни части, използващ MySQL база данни. Проектът е конфигуриран с Docker и Docker Compose за лесно изграждане, стартиране и разработка.

---

##  Структура на проекта

```
AutoParts/
│
├── Dockerfile               # Multi-stage Dockerfile за билд и рантайм
├── docker-compose.yml       # Конфигурация за стартиране на app и db
├── pom.xml                  # Maven конфигурация
├── README.md                # Документация
├── src/                     # Java сорс код
│   └── main/
│       └── java/
│           └── org/example/
│               ├── DBService.java
│               ├── Part.java
│               ├── Manufacturer.java
│               └── Car.java
```

---

##  Изграждане и стартиране на контейнерите

###  Изисквания

- Docker
- Docker Compose (ако ползвате `docker-compose.yml`)

###  Стартиране с Docker Compose

```bash
docker-compose up --build
```

Това ще:
- Изгради и стартира `autoparts_app` (Java приложението)
- Стартира `autoparts_mysql` (MySQL база с име `Autoparts`)

---

##  Компоненти

###  1. Java Приложение (`autoparts_app`)

- **Изграждане:** Използва multi-stage Dockerfile.
- **Основен клас:** `DBService.java`
- **Функции:**
  - Добавяне, търсене, редактиране и изтриване на части.
  - Извличане на всички части.
  - Работа с производители и поддържани автомобили.

###  2. MySQL база данни (`autoparts_mysql`)

- Версия: `mysql:8`
- Създава база `Autoparts` с root потребител.
- Данните се запазват в Docker volume `db-data`.

---

##  Комуникация между услугите

Docker Compose дефинира две услуги:

- `db` (MySQL): стартира на порт `3306`.
- `app` (Java): стартира на порт `8080`.

Приложението достъпва базата чрез следните environment променливи:

```env
DB_HOST=db
DB_PORT=3306
DB_NAME=Autoparts
DB_USER=root
DB_PASSWORD=rootpass
```

Комуникацията между контейнерите се осъществява по вътрешната Docker мрежа, създадена автоматично от Compose.
---

## Основни методи в класа `DBService`

Класът `DBService` управлява достъпа до базата данни и включва следните основни методи:

- **`addPart(Part part)`** – Добавя нова част в базата данни чрез SQL INSERT.
- **`updatePart(Part part)`** – Актуализира съществуваща част по нейния уникален код.
- **`deletePart(String code)`** – Изтрива част по предоставения код.
- **`searchPartsByName(String keyword)`** – Извършва търсене на части по име с помощта на SQL LIKE.
- **`getAll()`** – Извлича всички записи от таблицата `Part` и ги връща като списък от обекти.

---

