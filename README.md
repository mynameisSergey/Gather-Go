# Gather-Go

![](https://github.com/mynameisSergey/Gather-Go/blob/main/img/use-case.png)

#### Инновационное приложение для активных пользователей, позволяющее эффективно планировать досуг и находить компанию для участия в интересных событиях.

#### Используемые технологии и инструменты:

* Java 11, Spring Boot, Spring Data JPA, Hibernate, Docker.

#### Основные функции:

* Афиша событий: Пользователи могут предлагать мероприятия и собирать компанию для их посещения.
* Поиск и фильтрация: Позволяет искать и фильтровать события по просмотрам и датам.
* Краткая информация: При просмотре списка событий пользователи видят краткие сведения, а детальная информация доступна
  по отдельному запросу.
* Категории событий: Мероприятия делятся на категории, доступные для просмотра и фильтрации.
* Статистика: Сервис фиксирует запросы на события для анализа их популярности.
* Пользовательские возможности: Авторизованные пользователи могут добавлять, редактировать мероприятия и подавать заявки
  на участие.
* Комментарии: Возможность оставлять комментарии к событиям для обмена мнениями и впечатлениями.
* Административные функции: Управление категориями, мероприятиями и пользователями через API.

#### Спецификация API

Для обоих сервисов имеются подробные спецификации API:

* спецификация основного
  сервиса: [API основного сервиса](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json)
* спецификация сервиса
  статистики: [API сервиса статистика](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json)

#### Gather-Go — это не просто афиша, а полноценный сервис для общения и организации досуга, который упрощает планирование  мероприятий и помогает находить единомышленников.