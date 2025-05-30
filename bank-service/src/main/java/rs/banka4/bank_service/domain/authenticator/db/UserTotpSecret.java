package rs.banka4.bank_service.domain.authenticator.db;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import rs.banka4.bank_service.domain.user.client.db.Client;
import rs.banka4.bank_service.domain.user.employee.db.Employee;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_to_totp_secrets")
public class UserTotpSecret {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

    @Column(
        nullable = false,
        unique = true
    )
    private String secret;

    @OneToOne
    @JoinColumn(
        name = "client_id",
        unique = true
    )
    private Client client;

    @OneToOne
    @JoinColumn(
        name = "employee_id",
        unique = true
    )
    private Employee employee;

    @Column(nullable = false)
    private Boolean isActive;

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
        UserTotpSecret that = (UserTotpSecret) o;
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
