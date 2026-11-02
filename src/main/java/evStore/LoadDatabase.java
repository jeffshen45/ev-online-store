package evStore;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import evStore.model.Vehicle;
import evStore.repository.VehicleRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(VehicleRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(
                    new Vehicle("Tesla", "Model 3", 2025, new BigDecimal("45000"), 0)));
            log.info("Preloading " + repository.save(
                    new Vehicle("Ford", "Mustang Mach-E", 2024, new BigDecimal("52000"), 12000)));
            log.info("Preloading " + repository.save(
                    new Vehicle("Hyundai", "Ioniq 5", 2023, new BigDecimal("38000"), 25000)));
        };
    }

}   
