package rs.banka4.bank_service.domain.loan.db;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bank_margins")
public class BankMargin {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        unique = true
    )
    private LoanType type;

    private BigDecimal margin;


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "BankMargin{" + "id=" + id + ", type=" + type + ", margin=" + margin + '}';
    }
}
