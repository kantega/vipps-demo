package no.kantega.vipps;

import java.util.HashMap;

public class TransactionStatusNames {
    private HashMap<TransactionStatus, String> map;

    public TransactionStatusNames() {
        map = new HashMap<>();
        map.put(TransactionStatus.CANCEL, "CANCEL");
        map.put(TransactionStatus.RESERVED, "RESERVED");
        map.put(TransactionStatus.SALE, "SALE");
        map.put(TransactionStatus.CANCELLED, "CANCELLED");
        map.put(TransactionStatus.REJECTED, "REJECTED");
    }

    /**
     * Returns a status name based on a value.
     * @param status The value to return a name for.
     */
    public String getName(TransactionStatus status) {
        return map.get(status);
    }
}
