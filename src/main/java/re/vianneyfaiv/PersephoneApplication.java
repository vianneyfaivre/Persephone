package re.vianneyfaiv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class PersephoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersephoneApplication.class, args);
	}
}
