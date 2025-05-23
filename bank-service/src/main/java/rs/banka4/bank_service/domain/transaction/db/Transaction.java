package rs.banka4.bank_service.domain.transaction.db;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import rs.banka4.bank_service.domain.actuaries.db.MonetaryAmount;
import rs.banka4.bank_service.domain.trading.db.ForeignBankId;
import rs.banka4.bank_service.tx.TxExecutor;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(
        nullable = false,
        unique = true
    )
    private String transactionNumber;

    @Column(
        name = "from_account",
        nullable = false
    )
    private String fromAccount;

    @Column(
        name = "to_account",
        nullable = false
    )
    private String toAccount;

    @Embedded
    private MonetaryAmount from;

    @Embedded
    private MonetaryAmount to;

    @Embedded
    private MonetaryAmount fee;

    @Column(nullable = false)
    private String recipient;

    @Column(length = 3)
    private String paymentCode;

    @Column(length = 50)
    private String referenceNumber;

    @Column(
        nullable = false,
        length = 500
    )
    private String paymentPurpose;

    @Column(nullable = false)
    private LocalDateTime paymentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Builder.Default
    private boolean isTransfer = false;

    /**
     * Set to an ID returned by a {@link TxExecutor} to have the executor automatically update this
     * transactions {@link #status}.
     */
    @Column
    private ForeignBankId executingTransaction;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass =
            o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer()
                    .getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass =
            this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                    .getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Transaction that = (Transaction) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode()
            : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Transaction{"
            + "id="
            + id
            + ", transactionNumber='"
            + transactionNumber
            + '\''
            + '}';
    }
}
