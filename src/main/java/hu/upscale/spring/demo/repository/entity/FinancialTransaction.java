package hu.upscale.spring.demo.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author László Zoltán
 */
@Data
@Entity
@Table(name = "FinancialTransaction", schema = "demo")
public final class FinancialTransaction {

    @Id
    @Column(name = "TransactionId")
    private String transactionId;
    @Column(name = "PreviousTransactionId")
    private String previousTransactionId;
    @Column(name = "Data")
    private byte[] data;

}
