# Transaction Service

## Описание

Этот проект представляет собой сервис для обработки транзакций с возможностью расчета расходов в USD на основе текущих или последних доступных курсов валют KZT/USD и RUB/USD. Курсы валют загружаются ежедневно с использованием API (например, Fixer API) и сохраняются в базе данных PostgreSQL.

## Стек технологий
- Java 17
- Spring Boot 3.2.0
- PostgreSQL
- Docker
- Docker Compose
  
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
   docker-compose up --build
  
  Эта команда:
  - Сначала собирает образ вашего Java приложения.
  - Запускает сервис transaction-service и базу данных PostgreSQL


## Объяснение решений
    - Выбор монолитной структуры: Мы решили использовать монолитную архитектуру для упрощения разработки и деплоя на начальных этапах проекта. Это упрощает взаимодействие между компонентами и позволяет быстрее разрабатывать и тестировать функционал.
    
    - Добавление таблицы для курсов валют: Мы создали третью таблицу для хранения курсов валют, чтобы запрашивать и хранить дневные курсы валютных пар KZT/USD и RUB/USD. Это позволяет минимизировать обращения к внешним API и использовать локальные данные при расчете.
    
    - Планировщик задач: Использование аннотации @Scheduled для сброса месячного лимита позволяет автоматически обновлять лимиты расходов первого числа каждого месяца.
    
    - Код для получения курсов валют: Мы реализовали метод getCurrencyRate, который проверяет наличие курса на текущий день, запрашивает его с API при необходимости и использует последний доступный курс, если данные недоступны.


   
