package school.sptech.KentoCafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KentoCafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KentoCafeApplication.class, args);
	}

}
