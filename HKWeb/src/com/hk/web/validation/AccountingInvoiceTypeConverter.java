package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.impl.dao.accounting.AccountingInvoiceDaoImpl;
import com.hk.domain.accounting.AccountingInvoice;

/**
 * User: rahul
 * Time: 7 Dec, 2009 12:00:23 PM
 */
@Component
public class AccountingInvoiceTypeConverter implements TypeConverter<AccountingInvoice> {
  public void setLocale(Locale locale) {
    //do nothing
  }
  
  @Autowired
  AccountingInvoiceDaoImpl accountingInvoiceDao;

  public AccountingInvoice convert(String s, Class<? extends AccountingInvoice> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return accountingInvoiceDao.get(AccountingInvoice.class, idLong);
    }
  }
  
  
}