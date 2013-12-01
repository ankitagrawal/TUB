package com.hk.domain.comparator;



import java.util.Comparator;

import com.hk.domain.accounting.AccountingInvoice;

/**
 * Created by IntelliJ IDEA.
 * User: AdminUser
 * Date: Feb 29, 2012
 * Time: 12:21:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountingInvoiceComparator implements Comparator<AccountingInvoice> {
  public int compare(AccountingInvoice o1, AccountingInvoice o2) {
    if (o1.getInvoiceDate() != null && o2.getInvoiceDate() != null) {
      return o2.getInvoiceDate().compareTo(o1.getInvoiceDate()); //Desc invoice date order
    }
    return -1;
  }
}
