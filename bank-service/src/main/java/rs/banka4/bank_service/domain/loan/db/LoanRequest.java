package rs.banka4.bank_service.domain.loan.db;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import rs.banka4.bank_service.domain.account.db.Account;
import rs.banka4.rafeisen.common.currency.CurrencyCode;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "loan_requests")
public class LoanRequest {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        columnDefinition = "currency"
    )
    private CurrencyCode currency;

    private String employmentStatus;

    private Integer employmentPeriod;

    private Integer repaymentPeriod;

    private String purposeOfLoan;

    @ManyToOne
    private Account account;

    @OneToOne
    private Loan loan;

    private BigDecimal monthlyIncome;

    @Enumerated(EnumType.STRING)
    private LoanType type;

    @Enumerated(EnumType.STRING)
    private Loan.InterestType interestType;

    private String contactPhone;

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
        LoanRequest that = (LoanRequest) o;
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
}
