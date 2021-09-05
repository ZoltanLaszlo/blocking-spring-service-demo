package hu.upscale.spring.demo.repository.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author László Zoltán
 */
@Data
@Entity
@Table(name = "ArchiveFinancialTransaction", schema = "demo")
public final class ArchiveFinancialTransaction {

    @EmbeddedId
    private ArchiveFinancialTransactionId archiveFinancialTransactionId;
    @Column(name = "TransactionNumber")
    private int transactionNumber;
    @Column(name = "CompressedData")
    private byte[] compressedData;
    @Column(name = "Signature")
    private byte[] signature;

    @Data
    @Embeddable
    public static final class ArchiveFinancialTransactionId implements Serializable {

        private static final long serialVersionUID = 1L;

        @Column(name = "AccountStatementId")
        private String accountStatementId;
        @Column(name = "TransactionId", unique = true)
        private String transactionId;

    }

}
