package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AccountOverviewDto;
import com.ag.xyzbank.model.ValidationResponse;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.service.OverviewCalculator;
import com.ag.xyzbank.service.TokenService;
import com.ag.xyzbank.service.validation.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/api")
public class OverviewController {

	private final OverviewCalculator overviewCalculator;
	private final ValidationService validationService;
	private final TokenService tokenService;


	public OverviewController(OverviewCalculator overviewCalculator, ValidationService validationService, TokenService tokenService) {
		this.overviewCalculator = overviewCalculator;
		this.validationService = validationService;
		this.tokenService = tokenService;
	}

	@GetMapping("overview")
	public AccountOverviewDto getOverview(@RequestParam String token) {
		String username = tokenService.checkTokenStatus(token);

		if(username != null && !username.isEmpty()) {
			ValidationResponse validation = validationService.validateUserForOverview(username);

			if(validation.isValid()) {
				Account account = overviewCalculator.calculate(username);
				return new AccountOverviewDto(account.getIban(), account.getAccountType(), account.getBalance(), account.getCurrency());
			} else {
				//TODO put more details
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, validation.getMessage());
			}
		} else {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST);
		}
	}
}
