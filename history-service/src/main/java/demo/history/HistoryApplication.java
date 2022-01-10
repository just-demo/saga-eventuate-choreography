package demo.history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;

import io.eventuate.tram.spring.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;

@Import(TramNoopDuplicateMessageDetectorConfiguration.class)
@EnableMongoRepositories
@EnableRetry(proxyTargetClass = true)
@SpringBootApplication
public class HistoryApplication {

  public static void main(String[] args) {
    SpringApplication.run(HistoryApplication.class, args);
  }
}
