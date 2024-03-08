package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AccountOverviewDto;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.service.AccountService;
import com.ag.xyzbank.service.validation.TokenNotValidException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import com.ag.xyzbank.service.validation.UserNotEligibleException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AccountOverviewController {
	private final AccountService accountService;

	public AccountOverviewController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("overview")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
					schema = @Schema(implementation = AccountOverviewDto.class))}),
			@ApiResponse(responseCode = "400", description = "User does not exist or under the age of 18", content = @Content),
			@ApiResponse(responseCode = "403", description = "Invalid token", content = @Content)})
	public AccountOverviewDto getOverview(@RequestParam String token) {
		try {
			Account account = accountService.getByToken(token);
			return new AccountOverviewDto(account.getIban(), account.getAccountType(), account.getBalance(), account.getCurrency());
		} catch(UserExistanceException | UserNotEligibleException userExistenceException) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userExistenceException.getMessage());
		} catch(TokenNotValidException ex) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
		}
	}
}
