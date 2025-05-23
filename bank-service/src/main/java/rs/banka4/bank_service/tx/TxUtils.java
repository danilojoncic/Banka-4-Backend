package rs.banka4.bank_service.tx;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import rs.banka4.bank_service.tx.data.DoubleEntryTransaction;
import rs.banka4.bank_service.tx.data.Posting;
import rs.banka4.bank_service.tx.data.TxAccount;
import rs.banka4.bank_service.tx.data.TxAsset;

/** Various utilities for working with transactions. */
public class TxUtils {
    public static boolean isTxBalanced(DoubleEntryTransaction tx) {
        final var txBalance = new HashMap<TxAsset, BigDecimal>();
        tx.postings()
            .forEach(p -> txBalance.merge(p.asset(), p.amount(), BigDecimal::add));
        return txBalance.values()
            .stream()
            .allMatch(x -> x.compareTo(BigDecimal.ZERO) == 0);
    }

    public static Set<Long> collectDestinations(final DoubleEntryTransaction tx) {
        return tx.postings()
            .stream()
            .map(Posting::account)
            .map(TxAccount::routingNumber)
            .collect(Collectors.toSet());
    }
}
