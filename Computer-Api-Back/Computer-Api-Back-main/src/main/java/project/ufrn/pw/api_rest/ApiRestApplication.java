package project.ufrn.pw.api_rest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import project.ufrn.pw.api_rest.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
	}

	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}
}

/*
Usuario 1--N Pedido 
Pedido  N--N Produto
Usuario 1--1 Endereço 
*/