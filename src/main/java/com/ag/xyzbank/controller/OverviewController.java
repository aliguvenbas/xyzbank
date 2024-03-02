package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.OverviewDto;
import com.ag.xyzbank.model.ValidationResponse;
import com.ag.xyzbank.service.OverviewCalculator;
import com.ag.xyzbank.service.validation.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
public class OverviewController {

	private final OverviewCalculator overviewCalculator;
	private final ValidationService validationService;

	public OverviewController(OverviewCalculator overviewCalculator, ValidationService validationService) {
		this.overviewCalculator = overviewCalculator;
		this.validationService = validationService;
	}

	@GetMapping("overview")
	public OverviewDto getOverview(@RequestBody AuthCredentialsDto authCredentialsDt){

		String username = "test-user";

		ValidationResponse validation = validationService.validateUserForOverview(username);

		if(validation.isValid()) {
			OverviewDto overviewDto = overviewCalculator.calculate(username);
			return overviewDto;
		} else {
			//TODO put more details
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, validation.getMessage());
		}
	}

}
