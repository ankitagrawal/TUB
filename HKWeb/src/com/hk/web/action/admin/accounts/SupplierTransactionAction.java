package com.hk.web.action.admin.accounts;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.accounting.SupplierTransactionService;
import com.hk.admin.util.finance.busy.*;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.Date;
import java.util.List;


@Component
public class SupplierTransactionAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(SupplierTransactionAction.class);

    @Autowired
    private SupplierTransactionService supplierTransactionService;

    @Autowired
    private SupplierDao supplierDao;

    private Date startDate;
    private Date endDate;
    private Supplier supplier;
    private List<SupplierTransaction> supplierTransactionList;
    private Page supplierTransactionPage;
    private int defaultView;
    private String supplierName;
    private String tinNumber;

	@DefaultHandler
	@Secure(hasAnyPermissions = {PermissionConstants.FINANCE_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
	public Resolution pre() {
        if(supplierName != null || tinNumber != null){
            Page supplierPage = getSupplierDao().getSupplierByTinAndName(tinNumber, supplierName, null, 0, 0);
            if(supplierPage != null && supplierPage.getList() != null && supplierPage.getList().size() > 0){
                supplier = (Supplier) supplierPage.getList().get(0);
            }
        }
        supplierTransactionList = getSupplierTransactionService().getLastTransactionListForSuppliers(supplier);
		return new RedirectResolution("/pages/admin/supplierTransactions.jsp");
	}

    @Secure(hasAnyPermissions = {PermissionConstants.FINANCE_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution viewDetails() {
        if(supplier == null){
            addRedirectAlertMessage(new SimpleMessage("Please select a supplier"));
            return new RedirectResolution(SupplierTransactionAction.class);
        }

        if(defaultView == 1){
            startDate = new Date( new Date().getTime() - 1000L*60L*60L*24L*30L);
            endDate = new Date();
        }

        supplierTransactionPage = getSupplierTransactionService().getAllTransactionListForSuppliers(supplier, startDate, endDate, 0, 0);
        if(supplierTransactionPage != null){
            supplierTransactionList = supplierTransactionPage.getList();
        }
        return new RedirectResolution("/pages/admin/supplierTransactionDetails.jsp");
    }


    public SupplierTransactionService getSupplierTransactionService() {
        return supplierTransactionService;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public List<SupplierTransaction> getSupplierTransactionList() {
        return supplierTransactionList;
    }

    public void setSupplierTransactionList(List<SupplierTransaction> supplierTransactionList) {
        this.supplierTransactionList = supplierTransactionList;
    }

    public int getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(int defaultView) {
        this.defaultView = defaultView;
    }

    public SupplierDao getSupplierDao() {
        return supplierDao;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }
}