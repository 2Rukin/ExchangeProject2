<h2>Описание проекта "Ценные бумаги"</h2>

- [Summary](#summary)
- [Stack](#stack)
- [MVP](#mvp)
- [Backlog](#backlog)
- [Структура проекта](#структура-проекта)
    - [Бэкенд](#бэкенд)
- [Работа на проекте](#работа-на-проекте)
    - [С чего начинать](#с-чего-начинать)
    - [О таскборде](#о-таскборде)
    - [Как выполнять задачи](#как-выполнять-задачи)
    - [Проверка задач](#проверка-задач)
    - [Требования к коду](#требования-к-коду)
    - [Созвоны по проекту](#созвоны-по-проекту)
- [Дополнительные материалы](#дополнительные-материалы)
    - [Spring Boot Dev Tools](#Spring Boot Dev Tools)
    - [Аутентификация](#аутентификация)
    - [Liquibase](#liquibase)
    - [API Gateway Руководство](#api-gateway)
    - [Docker-Compose и Keycloak Руководство](#docker-compose--keycloak)
    - [Keycloak подключение сервиса](#подключить-service-к-keycloak-в-качестве-ресурс-сервера)
    - [Запуск Quotes-API](#quotes-api)
    - [Swagger](#swagger)

### Summary

Реализуем функционал по мониторингу акций и ценных бумаг и получению актуальной информации из различных депозитариев.

Схемы проекта - [mail.Облако](https://cloud.mail.ru/public/ERqf/41U6PrWAC)

Проект рассчитан на студентов, успешно завершивших этап Pre-Project в Kata Academy.

### Stack

Проект пишется на базе `Java 17`, `Spring Boot 3.0`, `Spring Cloud `, `Maven` и архитектуре REST. Работаем с базой
данных `PostgreSQL` через `Spring Data` и `Hibernate`. Общение между сервисами `REST` и брокер сообщений `RabbitMQ` , для просмотра схемы приложения используется [drawio](https://app.diagrams.net/).

Для работы с БД используем Docker. Быстрый запуск postgres в Docker - (docker run --name postgresql-container -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres).
Для работы с брокером сообщений `RabbitQM` используем Docker. Быстрый запуск `RabbitQM` в Docker - (docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management). 

Чтобы не писать boilerplate-код, используем на проекте [Lombok](https://projectlombok.org/features/all).

**_`TODO`_** Все контроллеры и их методы нужно сразу описывать
аннотациями [Swagger](https://docs.swagger.io/swagger-core/v1.5.0/apidocs/allclasses-noframe.html).

**_`TODO - потестируем данную доску`_** Таск-борд находится на [Kaiten](https://katateamdev.kaiten.ru/space/).

Dev-stand будем поднимать и разворачивать через Docker, а настраивать CI/CD - через Gitlab.

### MVP

[MVP](https://goo.su/mUlqF) - API (полностью описанное в Swagger **_`TODO`_**), которое будет уметь показывать,
обновлять, запрашивать полную информацию о ценных бумагах и акциях. Работать с таким API можно будет через веб-интерфейс
Swagger и Postman.

### Backlog

Фичи:
<ul>
<li>Создание юзер профиля</li>
<li>Создание личного кабинета юзера, добавление аутентификации через логин/пароль и OAUTH2.0</li>
<li>Реализация функционала обратной связи с пользователем через e-mail и Telegram</li>
<li>Создание личного кабинета администратора ресурса</li>
<li>Подключение api для запроса котировок акций</li>
<li>Добавление коннектора в НРД(национальный депозитарий) для получения полной информации по ценным бумагам</li>
<li>Добавление форума для обсуждения</li>
<li>Добавление микро-сервиса по продажи подписки на сервис для пользователей</li>
<li>Добавление микро-сервиса по управлению платежами пользователей</li>
</ul>

Импрувменты:

<ul>
<li>логирование через Slf4j</li>
<li>юнит-тесты и интеграционные тесты(c использованием тест-контейнеров)</li>
<li>анализ качества кода через SonarQube</li>
<li>использование системы контроля версий БД Liquibase</li>
</ul>

## Структура проекта

### Бэкенд

Проект модульный, имеет несколько модулей - в дальнейшем каждый модуль это отдельный микро-сервис. Описание модулей:
<ul>
<li><code>config</code> Конфигурации микро-сервисов</li>
<li><code>core</code> Общие модели для всех сервисов (пока не трогаем)</li>
**<li><code>server</code> Eureka server</li>**
<li><code>user-profile</code> На время MVP все основные классы пишем в этом модуле, после запуска основного функционала будем смотреть на какие модули разделить</li>
</ul>

Слои внутри модулей:
<ul>
<li><code>config</code> конфигурационные классы, в т.ч. Spring Security, инструменты аутентификации</li>
<li><code>entity</code> сущности базы данных</li>
<li><code>dto</code> специальные сущности для передачи/получения данных в/с апи</li>
<li><code>repository</code> dao-слой приложения, реализуем в виде интерфейсов Spring Data, имплементирующих JpaRepository.</li>
<li><code>service</code> бизнес-логика приложения, реализуем в виде интерфейсов и имплементирующих их классов.</li>
<li><code>controller</code> обычные и rest-контроллеры приложения.</li>
<li><code>util</code> пакет для утилитных классов: валидаторов, шаблонов, хэндлеров, эксепшнов.</li>
</ul>

### Фронтенд

Фронтенд пишет команда фронтенда. Наша задача - дать возможность фронту обращаться к API нашего приложения

## Работа на проекте

### С чего начинать

Доступы. Если ты читаешь это, значит доступ к проекту у тебя уже есть )
<ol>
<li>загрузи проект себе в среду разработки</li>
<li>изучи весь проект - начни с pom, properties файлов и конфигурационных классов</li>
<li>добейся успешного запуска проекта. <a href="http://localhost:8888/"> Проверить</a>.</li>
</ol>
### О таскборде

Таск-борд строится по принципу Kanban - он разделён на столбцы, каждый из которых соответствует определённому этапу
работы с задачей:
<ul>
<li><code>Backlog</code> задачи на <b>новый функционал</b>, корзина функционала приложения. Здесь можете создавать карточки на таски, которые считаете необходимыми</li>
<li><code>TODO</code> задачи, требующие выполнения</li>
<li><code>In Progress</code> выполняемые в данный момент задачи, обязательно должны иметь исполнителя</li>
<li><code>Code-review </code> задачи на этапе перекрёстной проверки студентами</li>
<li><code>Ready for merge request</code> задачи на проверке у техлида</li>
<li><code>Done</code> выполненные задачи</li>
</ul>


<ul>

</ul>

### Как выполнять задачи

### GITFLOW

Описание работы с Git:
<li>master - стабильно продовая версия , изменения заливаются техлидом с новым релизом</li>
<li>develop - оптимальная рабочая версия , сборка перед релизом</li>
<li>stage - тестовая версия , доступна для обновления всем кто принимает участие в проекте</li>
<ul>
<li>в графе <code>TODO</code> на таск-борде выбери карточку с задачей и назначь её себе для исполнения</li>
<li>загрузи себе последнюю версию ветки <code>develop</code></li>
<li>создай от <code>develop</code> свою собственную ветку для выполнения взятой задачи. Свою ветку назови так, чтобы было понятно, чему посвящена задача. В начале имени ветки проставь номер задачи с Gitlab. Например, <code>313_adding_new_html_pages</code></li>
<li>выполни задачу, обязательно сделай юнит-тесты на методы и, если всё ок, залей её в репозиторий проекта</li>
<li>создай на своей ветке merge request, в теле реквеста укажи <code><i>Closes #здесь-номер-таски"</i></code>. Например, <code>Closes #313</code></li>
<li>перенеси задачу в столбец <code>code-review</code></li>
</ul>

### Проверка задач

На этапе кросс-ревью студенты проверяют задачи, выполненные друг другом. В случае, если к коду есть замечания,
проверяющий пишет замечания в мердж реквесте и оставляет комментарий в карточке. Если к коду претензий нет, проверяющий
студент ставит к карточке лайк.

**Каждая карточка (студенческая задача) должна быть проверена как минимум 2 другими студентами и одобрена ими (т.е.
собрать не менее 2 лайков).**

Только после этого карточку можно переносить в столбец `Final Review`.

Затем код проверяет техлид (ментор) и в случае обнаружения ошибок возвращает задачу на студента и переносит её в
столбец `InProgress`. Если всё ок - merge request принимается, ветка студента сливается с основной веткой проекта, а
карточка переносится в столбец `Closed`.

### Требования к коду

- сделайте себе понятные никнеймы (имя + фамилия) в Git. Не хочу гадать, кто, где и что писал.
- для каждого класса и (желательно) методов пишите комментарии в формате <b>Javadoc</b>:
    - над классом: что это за класс, зачем нужен. Описывайте поля.
    - над методом: что делает, какие параметры принимает (и что это такое), что возвращает.
- свободно создавайте собственные вспомогательные классы в пакете Util - типа утилиток для страховки от null и типа
  того.
- в REST-контроллерах пользоваться аннотациями Swagger - причём как сами контроллеры в целом, так и их отдельные методы `**TODO**`.
- пишите Commit message как можно более подробно!

Если в процессе разработки вы пришли к пониманию того, что требуется создать какую-то ещё сущность - создавайте карточку
в `Backlog`, согласуйте ее с тимлидом и вперёд

### Требования к логированию работы контроллеров:

1. В каждый метод необходимо добавить логирование с описанием произведенной операции на уровне info.

2. Если объект не найден, вывести сообщение уровня warning ("not found" или "does not exist") с описанием произведенной
   операции.

### Созвоны по проекту

Созвоны проходят по вторникам и четвергам в оговорённое время. Регламент:

- длительность до 15 минут
- формат: доклады по 3 пунктам:
    - что сделано с прошлого созвона
    - какие были/есть трудности
    - что будешь делать до следующего созвона
- техлид (ментор) на созвонах код не ревьюит

Любые другие рабочие созвоны команда проводит без ограничений, т.е. в любое время без участия техлида. Договаривайтесь
сами :)

## Дополнительные материалы

### Spring Boot Dev Tools

Благодаря данной зависимости, разработчик получает возможность ускорить разработку проекта на Spring Boot в IDEA IDE и
сделать этот процесс более приятным и продуктивным. А не вручную перегружать сборку каждый раз, когда надо проверить
код. IDEA будет делать это за него.

#### Как подключить

+ [mkyong.com](https://mkyong.com/spring-boot/intellij-idea-spring-boot-template-reload-is-not-working/)
+ [metakoder.com](https://www.metakoder.com/blog/spring-boot-devtools-on-intellij/)
+ [YouTube Video](https://youtu.be/XYTET4vSn6k)

#### Полезные ссылки:

+ [baeldung.com](https://www.baeldung.com/spring-boot-devtools)
+ [habr.com](https://habr.com/ru/post/479382/)

### Аутентификация

**`TODO`**

### Liquibase
<h3>Добавление Liquibase для создания таблицы модели </h3>
- Добавление зависимости(зависимость уже добавлена).
- Создание в resources папки changelog и добавление файла db.changelog-master.yaml(сделано).
- в db.changelog-master.yaml добавляем путь к скрипту который будет отвечать за создание таблицы.
- создаем сам скрипт, название должно соответствовать тому что он делает(create-tariff-table.yaml смотрим пример).
- запускаем, таблица вашей модели создана

### API Gateway

В bootstrap.yml gateway-service прописываем следующие данные для своего сервиса:
1. Открываем новый раздел routes.
2. Прописываем id: (название вашего сервиса)
3. Прописываем uri: lb://(название вашего сервиса)
4. В predicates прописываем адреса к которым будет обращаться gateway - Path=/адрес

### Docker-Compose и Keycloak

1. В Maven сделать clean и install по всем сервисам.
2. Запустить все сервисы в docker-compose.yaml.
3. Наслаждаться моментом. 
4. Keycloak запускается на порту 8890, заходить в keycloak через "Administration Console" под логином admin с паролем admin.
5. В Users создаем пользователя с любым логином и паролем, обязательные поля при создании пользователя в keycloak: username, email, first и last name, иначе выпадет null в контроллере api/user/home.
6. Переходим в созданного юзера, во вкладку credentials: создаем пароль, далее вкладка Role: ставим роли ADMIN и USER.
7. Переходим по любой ссылке из контролеров, нас всё равно обяжут авторизоваться, авторизуемся под созданным юзером, далее нас перекинет на http://localhost:8080/profile/api/user/home, мы должны получить json с нашим пользователем.
8. Подключитесь к БД запущенной из контейнера: PASSWORD=postgres, USER=postgres, POSTGRES_DB=postgres, port=5432, table - users и проверьте, попал ли пользователь в БД после авторизации
9. Можно создать ещё пару пользователей и проверить их всех по ссылке http://localhost:8080/profile/api/user (пользователь должен иметь роль ADMIN). 
10. Будет полезно посмотреть урок по Keycloak: https://www.youtube.com/playlist?list=PL8X2nqRlWfaZbGSfSCnNyQ7g5VW3irLjX

  <h6> P.S. Лучше всего проверять свои контроллеры через POSTMAN или Swagger. </h6>


### Подключить service к Keycloak в качестве ресурс сервера

1. Добавляем зависимость <code> <dependency\>
   <groupId\>org.springframework.boot<\/groupId>
   <artifactId\>spring-boot-starter-oauth2-resource-server<\/artifactId>
   <\/dependency> </code>
2. в фале пропирти вашего сервиса укаываем           jwk-set-uri: http://localhost:8890/realms/project-realm/protocol/openid-connect/certs
3. Создаем классы RealmRoleConverter и ResourceServerConfig в пакете config по аналогии с user-profile
4. С помощью аннотации <code> @RolesAllowed({"ADMIN"})</code> ограничиваем доступ к своим контроллерам только по определенной роли

### Quotes API
1. Сначала нужно получить и вставить свой токен Тинькофф в config в quotes-api-local и quotes-api-develop. Вот [ссылка](https://www.tinkoff.ru/business/help/solutions/open-api/about/activation/), где можно узнать информацию. Без этого токена сервис не будет работать.
2. Запускаем в Докере postgres, redis и rabbit. Также не забываем про KeyCloack и его БД.
3. В config сервисе в файле quotes-api-local (если запускаем локально) выбираем через какой сервис будем получить акции: Tinkoff или Bcs. 
4. Запускаем server -> config -> gateway -> quotes. Каждый две минут список всех акций будет обновляться (в Тинькофф - из-за ограничений только 150 акций, в БКС обновляется 500 цен акций - лимит можно убрать в BcsStockService).
5. При каждом запросе quotes отправляет сообщение в RabbitMQ с информацией об активности юзера на сервисе. Чтобы её можно получить по адресу - http://localhost:8080/profile/api/user/getInfo
6. Работает Spring Cloud Bus - любое изменение в файле quotes-api-local будет отражаться на сервисе сразу без перезапуска после определённого дилея. Можно менять как будем получать акции. Чтобы зафорсить изменения можно перейти по адресу - http://localhost:8888/actuator/bus-refresh
7. По пути "/directStock" можно получить акции напрямую через АПИ, а не через БД (не нужно ждать обновления стоимости каждые 2 минуты). GET запрос - для одной акции, POST запрос - для списка акций.


### Swagger
  - Описание наших REST контроллеров происходит благодаря swagger, ссылка для получения первичной информации о [swagger](https://struchkov.dev/blog/api-swagger/) + [официальная документация](https://swagger.io/docs/).
  - Чтоб добавить swagger в свой микросервис, если он отсутствует, тебе понадобиться две зависимости: "springdoc-openapi-starter-webmvc-ui" и "swagger-annotations"
  - Чтобы попасть на сгенерированную swagger'ом страницу, нужно сначала пройти авторизацию(описано выше) и перейти по ссылке - localhost:8080/nameYourService/swagger-ui/index.html, nameYourService - название вашего сервиса, его можно посмотреть в gateway-service/src/main/resources/bootstrap.yml
  - После открывается страница, на которой представлены будут все REST контроллеры данного микросервиса. Раскрывайте вкладки и читайте описание контроллера, также там присутствуют схемы DTO классов 
  - Тестирование контроллеров через swagger, требует отключение защиты csrf в gateway, на данный момент она отключена