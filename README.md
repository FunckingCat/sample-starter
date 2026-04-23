# dab2c-platform

Платформа общих компонентов, оформленная как набор Spring Boot стартеров. Цель — дать командам переиспользуемые библиотеки с единым способом сборки, публикации и подключения, чтобы типовой код (интеграции, клиенты, сквозная функциональность) жил в одном репозитории и обновлялся централизованно.

> ⚠️ Проект на ранней стадии. Структура сборки зафиксирована, но публичное API и состав модулей пока нестабильны — возможны breaking changes без warning'а.

---

## Как устроены Spring Boot стартеры (в двух словах)

Стартер — это JAR, который при подключении к сервису автоматически регистрирует свои бины в Spring-контексте. Механика строится на трёх кирпичах:

- `*-api` — публичные интерфейсы и модели: только на них программирует потребитель.
- `*-impl` — реализация API, скрытая от потребителя (подключается к стартеру через `implementation`, не утекает в его compile classpath).
- `*-starter` — склеивает `api + impl` через авто-конфигурации. Spring Boot находит их по файлу `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` и поднимает бины при старте приложения.

Типичный авто-конфиг выглядит так: класс с `@AutoConfiguration`, биндинг настроек через `@ConfigurationProperties` + `@EnableConfigurationProperties`, включение фичи через `@ConditionalOnProperty`, и `@ConditionalOnMissingBean` на каждом `@Bean` — чтобы потребитель мог подменить любой бин своим, объявив его в своей конфигурации.

Подробнее: [Spring Boot — Developing Auto-configuration](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html).

---

## Структура репозитория

```
dab2c-platform/
├── buildSrc/                           # конвеншен-плагины Gradle
├── gradle/
│   └── libs.versions.toml              # каталог версий (единая точка правды)
├── dab2c-platform-api/                 # группирующая директория с API-модулями
│   └── <feature>-api/                  # публичные интерфейсы и модели фичи
├── dab2c-platform-impl/                # группирующая директория с реализациями
│   └── <feature>-impl/                 # внутренняя реализация, не экспонируется
├── dab2c-platform-starter/             # единый стартер с авто-конфигурациями
├── dab2c-platform-integration-tests/   # сквозные тесты поверх стартера (не публикуется)
├── build.gradle.kts                    # group и version для всех подпроектов
└── settings.gradle.kts                 # состав модулей
```

`dab2c-platform-api/` и `dab2c-platform-impl/` — **не модули**, а обычные группирующие папки без собственного `build.gradle.kts`. Gradle видит вложенные модули через `include("dab2c-platform-api:<feature>-api")` в `settings.gradle.kts`; двоеточие здесь задаёт иерархию проектов, не привязываясь к физическим папкам.

### Роли модулей

| Модуль                              | Назначение                                                         | Где живёт код                                              | Публикуется                         |
|-------------------------------------|--------------------------------------------------------------------|------------------------------------------------------------|-------------------------------------|
| `<feature>-api`                     | Публичные интерфейсы, DTO, контракты. Java.                        | `src/main/java/ru/sbrf/dab2c/platform/<feature>/api/`      | да                                  |
| `<feature>-impl`                    | Реализация API. Kotlin. Зависит от своего `-api` через `api(...)`. | `src/main/kotlin/ru/sbrf/dab2c/platform/<feature>/impl/`   | да                                  |
| `dab2c-platform-starter`            | Авто-конфигурации и `@ConfigurationProperties`. Одна пара props + config на фичу. | `src/main/kotlin/ru/sbrf/dab2c/platform/starter/<feature>/` | да, артефакт `dab2c-platform-starter` |
| `dab2c-platform-integration-tests`  | `@SpringBootTest`-проверки, что стартер корректно поднимает контекст. | `src/test/kotlin/ru/sbrf/dab2c/platform/it/`               | нет                                 |

---

## Конвеншен-плагины (`buildSrc`)

В `buildSrc/src/main/kotlin/` лежат три precompiled-плагина. Они нужны, чтобы `build.gradle.kts` каждого модуля был в 5–10 строк, а общая настройка (версия JVM, BOM, публикация) правилась в одном месте.

- **`dab2c.java-conventions`** — JVM-основа: `java-library`, Gradle toolchain JDK 21, `withSourcesJar()`, BOM `spring-boot-dependencies` (`platform(...)` на `implementation` и `testImplementation`), `spring-boot-starter-test` + `junit-platform-launcher`, `useJUnitPlatform()` и логирование passed/failed/skipped. Напрямую в модулях **не подключается** — приходит транзитивно через `kotlin-conventions`.
- **`dab2c.kotlin-conventions`** — расширяет `java-conventions` плагинами `kotlin("jvm")` и `kotlin("plugin.spring")`, выставляет `jvmTarget = 21`, `-Xjsr305=strict`, добавляет `kotlin("reflect")` в `implementation`. Это базовый плагин для **всех** модулей платформы — включая чистые Java-модули (`*-api`): Kotlin-плагин там просто не активируется исходниками, накладных расходов нет.
- **`dab2c.publish-conventions`** — `maven-publish` + `MavenPublication` из `components["java"]`, репозиторий — `mavenLocal()`. Применяется ко всему, что должно публиковаться (api, impl, starter); **не применяется** к integration-tests, благодаря чему тестовый модуль не попадает в `~/.m2`.

---

## Каталог версий (`gradle/libs.versions.toml`)

Единая точка правды для версий и координат Gradle-зависимостей. Ключевые пункты:

- `[versions]` — версии (Kotlin, Spring Boot).
- `[libraries]` — артефакты (Spring BOM `spring-boot-dependencies`, `spring-boot-autoconfigure`, `spring-boot-configuration-processor`, `spring-boot-starter-test`, Kotlin-плагины для `buildSrc`).
- Spring-артефакты объявлены **без версий**: версия приходит из BOM, подключённого в `dab2c.java-conventions` через `platform(libs.spring.boot.dependencies)`.

Новую зависимость добавляем в `[libraries]` и ссылаемся в `build.gradle.kts` как `libs.my.lib` — руками версию в модульный билд-скрипт писать не надо.

---

## Соглашения по именованию и пакетам

**Директории и модули:**
- Верхний уровень — префикс `dab2c-platform-` **обязателен**: `dab2c-platform-api/`, `dab2c-platform-impl/`, `dab2c-platform-starter/`, `dab2c-platform-integration-tests/`.
- Подмодули внутри группирующих директорий — **без префикса**: `<feature>-api`, `<feature>-impl`.

**Maven-координаты:**
- Группа: `ru.sbrf.dab2c.platform` (задана в корневом `build.gradle.kts`, наследуется всеми подпроектами).
- Artifact ID совпадает с именем директории модуля (Gradle-умолчание). Соответственно, `dab2c-platform-starter` публикуется под тем же `artifactId`.

**Java/Kotlin-пакеты:**
- `ru.sbrf.dab2c.platform.<feature>.api` — интерфейсы и DTO.
- `ru.sbrf.dab2c.platform.<feature>.impl` — реализация.
- `ru.sbrf.dab2c.platform.starter.<feature>` — `<Feature>Props` и `<Feature>AutoConfig`.

**Ключи конфигурации:**
- Префикс — `dab2c.<feature>.*`. Канонический набор: `dab2c.<feature>.enabled` (boolean, по умолчанию `true`) + специфичные параметры фичи.

**Язык реализации:**
- API-модули — **Java** (чтобы Java- и Kotlin-потребители видели чистые сигнатуры, без Kotlin-специфики в байткоде).
- Impl и starter — **Kotlin**.

---

## Как добавить новый `<feature>` (api + impl + авто-конфигурация)

Чек-лист на одну фичу:

1. **Создать модули.** `dab2c-platform-api/<feature>-api/` и `dab2c-platform-impl/<feature>-impl/`.

2. **Прописать `build.gradle.kts` в каждом:**
   ```kotlin
   // dab2c-platform-api/<feature>-api/build.gradle.kts
   plugins {
       id("dab2c.kotlin-conventions")
       id("dab2c.publish-conventions")
   }
   description = "dab2c-platform — <feature> API"
   ```
   ```kotlin
   // dab2c-platform-impl/<feature>-impl/build.gradle.kts
   plugins {
       id("dab2c.kotlin-conventions")
       id("dab2c.publish-conventions")
   }
   description = "dab2c-platform — <feature> implementation"
   dependencies {
       api(project(":dab2c-platform-api:<feature>-api"))
   }
   ```

3. **Разложить исходники** по `src/main/java/ru/sbrf/dab2c/platform/<feature>/api/` (интерфейсы, DTO) и `src/main/kotlin/ru/sbrf/dab2c/platform/<feature>/impl/` (реализация).

4. **Зарегистрировать модули** в `settings.gradle.kts`:
   ```kotlin
   include("dab2c-platform-api:<feature>-api")
   include("dab2c-platform-impl:<feature>-impl")
   ```

5. **Подключить фичу к стартеру** в `dab2c-platform-starter/`:
   - создать пакет `ru.sbrf.dab2c.platform.starter.<feature>`;
   - `<Feature>Props` — Kotlin data class с `@ConfigurationProperties(prefix = "dab2c.<feature>")` и `@DefaultValue` на параметрах конструктора (включая `enabled: Boolean = true`);
   - `<Feature>AutoConfig`:
     ```kotlin
     @AutoConfiguration
     @EnableConfigurationProperties(FeatureProps::class)
     @ConditionalOnProperty(prefix = "dab2c.<feature>", name = ["enabled"], havingValue = "true", matchIfMissing = true)
     class FeatureAutoConfig {
         @Bean
         @ConditionalOnMissingBean
         fun featureService(props: FeatureProps): FeatureService = FeatureServiceImpl(/* ... */)
     }
     ```
   - дописать FQN авто-конфига в `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` (по одному на строку);
   - в `dab2c-platform-starter/build.gradle.kts` добавить зависимости:
     ```kotlin
     api(project(":dab2c-platform-api:<feature>-api"))
     implementation(project(":dab2c-platform-impl:<feature>-impl"))
     ```

6. **Написать integration-тесты** в `dab2c-platform-integration-tests/` (`@SpringBootTest`): поведение по умолчанию, биндинг property, отключение через `dab2c.<feature>.enabled=false`, пользовательский override через `@TestConfiguration` (проверяет, что `@ConditionalOnMissingBean` работает).

7. **Прогнать сборку:**
   ```bash
   ./gradlew build
   ```

---

## Как собрать и опубликовать

**Окружение:** JDK 21 подхватится через Gradle toolchain автоматически, системный Gradle не нужен — используйте `./gradlew`.

**Полная сборка с тестами:**
```bash
./gradlew build
```

**Публикация в локальный Maven (`~/.m2/repository/ru/sbrf/dab2c/platform/`):**
```bash
./gradlew publishToMavenLocal
```

После публикации для каждого публикуемого модуля появятся `.jar`, `-sources.jar`, `.pom` и `.module`. Модуль `dab2c-platform-integration-tests` в `~/.m2` **не попадает** — это ожидаемо и служит маркером корректной настройки `publish-conventions`.

**Координаты артефактов:** `ru.sbrf.dab2c.platform:<artifactId>:<version>` (версия задана в корневом `build.gradle.kts`).

---

## Как подключить стартер в сервис-потребитель

**Gradle-зависимость:**
```kotlin
dependencies {
    implementation("ru.sbrf.dab2c.platform:dab2c-platform-starter:<version>")
}
```

**Минимальный `application.yml`:**
```yaml
dab2c:
  <feature>:
    enabled: true         # по умолчанию включено
    # специфичные для фичи параметры
```

Бины фичи (`<Feature>Service` и т.п.) становятся доступны для `@Autowired` сразу после подключения.

**Переопределение поведения:** объявите в своей конфигурации собственный `@Bean` нужного типа — авто-конфиг уступит ему место благодаря `@ConditionalOnMissingBean`.

**Отключение фичи целиком:** `dab2c.<feature>.enabled=false`.

---

## Планы (ориентиры, не жёсткий roadmap)

- Перевести property-префиксы стартера с `sample.` на `dab2c.` — для консистентности с группой и префиксом директорий.
- Настроить публикацию в корпоративный Nexus (сейчас в репозитории объявлен только `mavenLocal()`).
- Добавить генерацию `spring-configuration-metadata.json` для Kotlin-based `@ConfigurationProperties` (текущее ограничение KSP/KAPT).
- Настроить CI: lint, build на PR, publish-to-snapshot на merge в основную ветку.
