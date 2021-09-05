package hu.upscale.spring.demo.service;

import hu.upscale.spring.demo.repository.ArchiveFinancialTransactionRepository;
import hu.upscale.spring.demo.repository.FinancialTransactionRepository;
import hu.upscale.spring.demo.repository.entity.ArchiveFinancialTransaction;
import hu.upscale.spring.demo.repository.entity.ArchiveFinancialTransaction.ArchiveFinancialTransactionId;
import hu.upscale.spring.demo.repository.entity.FinancialTransaction;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author László Zoltán
 */
@Service
@AllArgsConstructor
public class AccountStatementArchiverService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final ArchiveFinancialTransactionRepository archiveFinancialTransactionRepository;
    private final ZipService zipService;
    private final RsaSignatureService rsaSignatureService;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<String> archiveFinancialTransaction(UUID accountStatementId, String transactionId, int transactionNumber) {
        Optional<FinancialTransaction> financialTransaction = financialTransactionRepository.findById(transactionId);
        if (financialTransaction.isPresent()) {
            ArchiveFinancialTransaction archiveFinancialTransaction = new ArchiveFinancialTransaction();

            ArchiveFinancialTransactionId archiveFinancialTransactionId = new ArchiveFinancialTransactionId();
            archiveFinancialTransactionId.setAccountStatementId(accountStatementId.toString());
            archiveFinancialTransactionId.setTransactionId(transactionId);

            archiveFinancialTransaction.setArchiveFinancialTransactionId(archiveFinancialTransactionId);
            archiveFinancialTransaction.setTransactionNumber(transactionNumber);

            byte[] rawData = financialTransaction.get().getData();
            byte[] compressedData = zipService.compress(rawData);
            byte[] signature = rsaSignatureService.signData(rawData);

            archiveFinancialTransaction.setCompressedData(compressedData);
            archiveFinancialTransaction.setSignature(signature);

            financialTransactionRepository.deleteById(archiveFinancialTransaction.getArchiveFinancialTransactionId().getTransactionId());
            archiveFinancialTransactionRepository.save(archiveFinancialTransaction);
        }

        return financialTransaction.map(FinancialTransaction::getPreviousTransactionId);
    }
}
