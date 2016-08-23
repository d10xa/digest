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

Build:

```bash
./gradlew clean build
```
