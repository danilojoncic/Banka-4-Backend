package rs.banka4.bank_service.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.banka4.bank_service.domain.company.db.Company;
import rs.banka4.bank_service.domain.transaction.db.Transaction;
import rs.banka4.bank_service.domain.user.client.db.Client;

public interface TransactionRepository extends
    JpaRepository<Transaction, UUID>,
    JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByTransactionNumber(String transactionNumber);

    @Query("""
        SELECT t FROM Transaction t
               WHERE t.fromAccount IN (SELECT a.accountNumber FROM Account a
                                                              WHERE a.client = :client)
        """)
    Page<Transaction> findAllByFromAccount_ClientAndIsTransferTrue(
        Client client,
        Pageable pageable
    );

    @Query(
        "SELECT COALESCE(SUM(t.from.amount), 0) FROM Transaction t WHERE t.fromAccount = :accNo AND t.paymentDateTime >= CURRENT_DATE"
    )
    BigDecimal getTotalDailyTransactions(String accNo, LocalDate date);

    @Query(
        "SELECT COALESCE(SUM(t.from.amount), 0) FROM Transaction t WHERE t.fromAccount = :accNo AND MONTH(t.paymentDateTime) = :month"
    )
    BigDecimal getTotalMonthlyTransactions(String accNo, int month);

    @Query(
        """
            SELECT t FROM Transaction t
                   WHERE t.fromAccount IN (SELECT a.accountNumber FROM Account a WHERE a.company = :company)
                         OR t.toAccount IN (SELECT a.accountNumber FROM Account a WHERE a.company = :company)
            """
    )
    Page<Transaction> findAllByCompany(@Param("company") Company company, Pageable pageable);
}
