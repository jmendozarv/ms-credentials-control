package encora.challenger.api.mscredentialscontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class MsCredentialsControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCredentialsControlApplication.class, args);
	}

}
