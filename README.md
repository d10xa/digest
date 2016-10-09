Digest job tracker
------------------

Install frontend dependencies

```bash
./gradlew npmInstall
```

Run spring-boot backend and angular frontend:

```bash
./gradlew npm_start bootRun --parallel

```

Run akka-http backend and angular frontend:
```bash
./gradlew npmInstall npm_start :backend-akka-http:run --parallel
```

Build:

```bash
./gradlew clean build
```
