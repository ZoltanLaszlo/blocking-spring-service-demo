package hu.upscale.spring.demo.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author László Zoltán
 */
@Service
@RequiredArgsConstructor
public class AccountStatementGeneratorService {

    private static final int LAST_TRANSACTION_NUMBER_ON_ACCOUNT_STATEMENT = 1;

    private final AccountStatementArchiverService accountStatementArchiverService;

    public UUID generateAccountStatement(UUID lastTransactionId) {
        UUID accountStatementId = UUID.randomUUID();

        int transactionNumberCounter = LAST_TRANSACTION_NUMBER_ON_ACCOUNT_STATEMENT;
        Optional<String> transactionId = Optional.of(lastTransactionId.toString());
        while (transactionId.isPresent()) {
            transactionId = accountStatementArchiverService.archiveFinancialTransaction(accountStatementId, transactionId.get(), transactionNumberCounter++);
        }

        return accountStatementId;
    }
}
