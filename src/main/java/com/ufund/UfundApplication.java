package com.ufund;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.model.Helper;
import com.ufund.model.User;
import com.ufund.persistence.FundingBasketDAO;
import com.ufund.persistence.FundingBasketFileDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@SpringBootApplication
public class UfundApplication {

	public static void main(String[] args) {

		SpringApplication.run(UfundApplication.class, args);
	}
}
