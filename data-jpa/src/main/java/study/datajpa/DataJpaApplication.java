package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication // Boot 사용하면 하위 디렉토리에서 Spring Data Jpa 사용 가능
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	// 수정자 등록자 주입 (랜덤한 id 생성해서 auditor aware에 넣어주는 코드)
	@Bean
	public AuditorAware<String> auditorProvider() {
		// session이나 token에서 user id를 가져와도 됨
		// auditor ware에 해당 내용을 넣어준다.
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
