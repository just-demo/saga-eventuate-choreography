```
docker-compose up --force-recreate
```

```
./gradlew :order-service:bootRun
./gradlew :customer-service:bootRun
./gradlew :history-service:bootRun
```

* http://localhost:8081/swagger-ui/
* http://localhost:8082/swagger-ui/
* http://localhost:8083/swagger-ui/

TODO: apply lombok!!!
TODO: cleanup dependencies!!!
TODO: remove dependency on sleuth?
TODO: remove dependency on actuator?