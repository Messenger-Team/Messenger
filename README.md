# Модуль "Messenger"

### 1. [Описание модуля](#module)
### 2. [Используемые технологии и библиотеки](#code)
### 3. [Фронтенд-реализация](#front)
### 4. [Инструкция по запуску](#instruction)
### 5. [Исправление взаимодействия бека и фронта](#debug)
### 6. [Прочее](#credits)
## Module
### В составе общего [проекта](https://github.com/team-5-tutor-project) модуль выполняет следующие функции:

- Создание чата.
- Отправка сообщений.
- Получение списка сообщений

### Модуль связан с другими модулями следующим образом:
- С [Поисковиком](https://github.com/team-5-tutor-project/team-5-searcher) Создание чата с заданными пользователями и возврат CHatId, созданного чата
- С [Аккаунтом](https://github.com/team-5-tutor-project/team-5-account) модуль взаимодействует во время запроса ссылки на чат по имеющемуся ID.

### 

### Текущая модель данных для БД:

## Code

### Backend

- Реализован http-сервер с помощью библиотеки **Spring-boot**
- Подключение к базе данных и конфигурация осуществляются на базе библиотеки **javax.persistence**
- Используется [Swagger](http://localhost:8000/swagger-ui.html) для тестирования реализуемых методов и взаимодействия с БД
![image](https://user-images.githubusercontent.com/54327287/175776924-f36e0dfb-b731-4d9b-8f3a-100cde6c039c.png)

### Frontend

- Используется Blazor
- Всё взаимодейвствие с другими модулями организуется через Redirect

## Front

### Посмотреть реализацию проекта можно по [ссылке](https://github.com/team-5-tutor-project/team-5-messenger-frontend)

### Чат:
![image](https://user-images.githubusercontent.com/54327287/175776836-42df39f3-d3f1-4b69-9a1a-8ba656a64fff.png)


