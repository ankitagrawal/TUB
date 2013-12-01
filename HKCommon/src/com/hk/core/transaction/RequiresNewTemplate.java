package com.hk.core.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;

@Component
public class RequiresNewTemplate {

    public Object executeInNewTransaction(TransactionCallback transactionCallback) {
        return transactionCallback.doInTransaction(null);
    }
}
