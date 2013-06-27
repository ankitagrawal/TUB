package com.hk.api.resource.accounts;

import com.hk.admin.pact.service.accounting.SupplierTransactionService;
import com.hk.api.constants.HKAPIConstants;
import com.hk.api.constants.HKAPIOperationStatus;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.constants.inventory.EnumSupplierTransactionType;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.core.SupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.Date;

@Path ("/payment")
@Component
public class PaymentInfoResource {

    @Autowired
    private SupplierTransactionService supplierTransactionService;

    @Autowired
    private SupplierDao supplierDao;

	@POST
	@Path ("/add")
	@Produces ("application/json")
	public HKAPIBaseDTO addPaymentForSupplier(@FormParam("tinNumber") String tinNumber, @FormParam("date") Date date, @FormParam("busyPaymentId") String busyPaymentId,
                                        @FormParam("amount") Double amount, @FormParam("busySupplierBalance") Double busySupplierBalance,
                                        @FormParam("narration") String narration){
        HKAPIBaseDTO baseDTO=new HKAPIBaseDTO();
        Supplier supplier = null;
        supplierDao.findByTIN(tinNumber);

        if(supplier == null){
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
            baseDTO.setMessage(HKAPIConstants.NO_SUPPLIER);
            return baseDTO;
        }
        SupplierTransaction supplierTransaction = supplierTransactionService.createSupplierTransaction(supplier, EnumSupplierTransactionType.Payment.asSupplierTransactionType(), amount, date, busyPaymentId,
                busySupplierBalance, narration);
        if(supplierTransaction == null){
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
        }
        return baseDTO;
	}

}
