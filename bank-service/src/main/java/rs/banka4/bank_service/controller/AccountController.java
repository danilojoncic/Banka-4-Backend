package rs.banka4.bank_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.banka4.bank_service.controller.docs.AccountApiDocumentation;
import rs.banka4.bank_service.domain.account.dtos.AccountDto;
import rs.banka4.bank_service.domain.account.dtos.BankAccountDto;
import rs.banka4.bank_service.domain.account.dtos.CreateAccountDto;
import rs.banka4.bank_service.domain.account.dtos.SetAccountLimitsDto;
import rs.banka4.bank_service.exceptions.authenticator.NotValidTotpException;
import rs.banka4.bank_service.service.abstraction.AccountService;
import rs.banka4.bank_service.service.abstraction.BankAccountService;
import rs.banka4.bank_service.service.abstraction.TotpService;
import rs.banka4.rafeisen.common.dto.AccountNumberDto;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController implements AccountApiDocumentation {

    private final AccountService accountService;
    private final TotpService totpService;
    private final BankAccountService bankAccountService;

    @Override
    @GetMapping("/search")
    public ResponseEntity<Page<AccountDto>> getAll(
        Authentication auth,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String accountNumber,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return accountService.getAll(
            auth,
            firstName,
            lastName,
            accountNumber,
            PageRequest.of(page, size)
        );
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable String id, Authentication auth) {
        return ResponseEntity.ok(
            accountService.getAccount(
                auth.getCredentials()
                    .toString(),
                id
            )
        );
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<AccountDto>> getAccountsForClient(Authentication auth) {
        return ResponseEntity.ok(
            accountService.getAccountsForClient(
                auth.getCredentials()
                    .toString()
            )
        );
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<Set<AccountNumberDto>> getAccountsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(accountService.getAccountsForUser(userId));
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> createAccount(
        @RequestBody @Valid CreateAccountDto createAccountDto,
        Authentication auth
    ) {
        accountService.createAccount(createAccountDto, (String) auth.getCredentials());
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @PutMapping("/set-limits/{accountNumber}")
    public ResponseEntity<Void> setAccountLimits(
        Authentication authentication,
        @NotBlank @PathVariable("accountNumber") String accountNumber,
        @RequestBody @Valid SetAccountLimitsDto dto
    ) {
        if (totpService.verifyClient(authentication, dto.otpCode())) {
            String token =
                authentication.getCredentials()
                    .toString();
            accountService.setAccountLimits(accountNumber, dto, token);
            return ResponseEntity.ok()
                .build();
        } else {
            throw new NotValidTotpException();
        }
    }

    @Override
    @GetMapping("/bank-accounts")
    public ResponseEntity<List<BankAccountDto>> getBankAccounts() {
        return ResponseEntity.ok(bankAccountService.getAllBankAccountWithCurrency());
    }
}
