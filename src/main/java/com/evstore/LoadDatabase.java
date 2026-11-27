package com.evstore;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.evstore.model.Vehicle;
import com.evstore.model.Customer;
import com.evstore.model.VehicleOption;
import com.evstore.repository.CustomerRepository;
import com.evstore.repository.VehicleOptionRepository;
import com.evstore.repository.VehicleRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(VehicleRepository repository, VehicleOptionRepository optionRepository, CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
        	//sample cares
        	if (repository.count() > 0) {
                log.info("Vehicles already exist, skipping sample data");
                return;
            }
            log.info("Preloading " + repository.save(
                    new Vehicle("Tesla", "Model 3", 2025, new BigDecimal("45000"), 0)));
            log.info("Preloading " + repository.save(
                    new Vehicle("Ford", "Mustang Mach-E", 2024, new BigDecimal("52000"), 12000)));
            log.info("Preloading " + repository.save(
                    new Vehicle("Hyundai", "Ioniq 5", 2023, new BigDecimal("38000"), 25000)));
            
            //sample options
            if (optionRepository.count() == 0) {
                for (Vehicle v : repository.findAll()) {
                    optionRepository.save(new VehicleOption(v, "Colour", "White", BigDecimal.ZERO));
                    optionRepository.save(new VehicleOption(v, "Colour", "Blue", new BigDecimal("500")));
                    optionRepository.save(new VehicleOption(v, "Wheels", "19 inch", BigDecimal.ZERO));
                    optionRepository.save(new VehicleOption(v, "Wheels", "20 inch", new BigDecimal("1200")));
                }
                log.info("Preloaded vehicle options");
            }
            
            //sample account
            if (customerRepository.count() == 0) {
                customerRepository.save(new Customer("demo", "demo@example.com", passwordEncoder.encode("password"), "123 Main St"));
                customerRepository.save(new Customer("demo2", "demo2@example.com", passwordEncoder.encode("password2"), "1 Main St"));
            }
        };
    }

}   
