# Transaction Service

## Описание

Этот проект представляет собой сервис для обработки транзакций с возможностью расчета расходов в долларах США (USD) на основе текущих или последних доступных курсов валют. В частности, поддерживаются курсы KZT/USD и RUB/USD. Курсы валют загружаются ежедневно с использованием внешнего API (например, Fixer API) и сохраняются в базе данных PostgreSQL для дальнейшего использования в расчетах. Flyway для миграции баз данных.
## Стек технологий
- Java 17
- Spring Boot 3.2.0
- PostgreSQL
- JPA (Hibernate)
- Flyway (for database migrations)
- Fixer API (for currency exchange rates)
- Docker and Docker Compose (for containerization)
- @EnableScheduling (for periodic tasks)

  
## Запуск сервиса

### 1. Установка необходимых инструментов
Перед тем как запустить сервис, убедитесь, что у вас установлены следующие инструменты:

- [Docker](https://docs.docker.com/get-started/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### 2. Запуск с использованием Docker Compose

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/lesbaydias/transactions-api-converter.git
   cd transactions-api-converter
   
3. Соберите и запустите контейнеры с помощью Docker Compose:
   ```bash
   docker compose up
  
  Эта команда:
  - Сначала собирает образ вашего Java приложения.
  - Запускает сервис transaction-service и базу данных PostgreSQL

## API Endpoints
### API транзакций
Создайте транзакцию: POST http://localhost:8080/api/transactions/create

В разделе Postman "Body"

```bash
{
    "accountFrom": 1111111111,
    "accountTo": 2222222222,
    "currencyShortName": "KZT",
    "sum": 150000.00,
    "expenseCategoryType": "PRODUCT"
}
```


### ОБЯЗАТЕЛЬНО:
Если текущая дата выходная дата, то сервер должен отправить последнюю актуальную информацию о валюте. А если таковой информации не имеется то должен срабатывать Exception.


## Client API
1. Get Transactions Exceeding Limit: GET http://localhost:8080/api/limits/exceeded

Returns transactions that have exceeded the set monthly limit.

2. Set New Monthly Limit: POST http://localhost:8080/api/limits/set
```bash
{
    "limitSum": 1000.00
}
```
Sets a new monthly spending limit.

## Database Schema
The application uses PostgreSQL and includes the following tables:

- limits: Stores monthly spending limits with the date they were set.
- transactions: Stores all transactions, including a flag (limit_exceeded) to indicate whether a transaction has exceeded the monthly limit.
- currency_rate: Caches exchange rates (KZT/USD, RUB/USD) to avoid frequent API calls.

#### Flyway is used for managing database migrations.

## Docker and Deployment
### Docker
A Dockerfile is provided to containerize the application.

1. Build Docker image:
```bash
docker build -t transactionservice .
```
2. Run the Docker container:
```bash
docker run -p 8080:8080 transactionservice
```

### Docker Compose
A docker-compose.yml file is provided for running the application along with PostgreSQL.

1. Start services with Docker Compose:
```bash
docker-compose up --build
```

## Scheduling and Limit Reset
The microservice uses Spring's @EnableScheduling annotation to automatically reset the monthly limit on the 1st of each month.

## External API Integration
This service uses the Fixer API for retrieving the exchange rates of KZT/USD and RUB/USD pairs. The rates are saved in the database and used for currency conversions.


## Объяснение решений
    - Выбор монолитной структуры: Мы решили использовать монолитную архитектуру для упрощения разработки и деплоя на начальных этапах проекта. Это упрощает взаимодействие между компонентами и позволяет быстрее разрабатывать и тестировать функционал.
    
    - Добавление таблицы для курсов валют: Мы создали третью таблицу для хранения курсов валют, чтобы запрашивать и хранить дневные курсы валютных пар KZT/USD и RUB/USD. Это позволяет минимизировать обращения к внешним API и использовать локальные данные при расчете.
    
    - Планировщик задач: Использование аннотации @Scheduled для сброса месячного лимита позволяет автоматически обновлять лимиты расходов первого числа каждого месяца.
    
    - Код для получения курсов валют: Мы реализовали метод getCurrencyRate, который проверяет наличие курса на текущий день, запрашивает его с API при необходимости и использует последний доступный курс, если данные недоступны.


   
