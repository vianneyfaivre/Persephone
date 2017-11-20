package re.vianneyfaiv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude= {
		DataSourceAutoConfiguration.class // excluded to avoid Batch processing to store metadata info
})
@PropertySource("persephone.properties")
public class PersephoneApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(PersephoneApplication.class);

		springApplication.addListeners(new ApplicationPidFileWriter());

		springApplication.run(args);
	}
}
