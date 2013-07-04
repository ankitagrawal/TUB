package com.hk.api.resource.accounts;

import com.hk.admin.pact.service.accounting.SupplierTransactionService;
import com.hk.api.constants.HKAPIConstants;
import com.hk.api.constants.HKAPIOperationStatus;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.dto.accounts.HKAPIPaymentInfoDTO;
import com.hk.constants.inventory.EnumSupplierTransactionType;
import com.hk.domain.accounting.BusyApiCallLog;
import com.hk.domain.accounting.SupplierTransaction;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.core.SupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

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
	public HKAPIBaseDTO addPaymentForSupplier(@RequestBody HKAPIPaymentInfoDTO hkapiPaymentInfoDTO,
                                              @Context HttpServletRequest request, @HeaderParam("apiKey") String apiKey){

        HKAPIBaseDTO baseDTO=new HKAPIBaseDTO();

        if(hkapiPaymentInfoDTO == null){
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
            baseDTO.setMessage(HKAPIConstants.NO_DATA_RECEIVED);
            return baseDTO;
        }

        BusyApiCallLog busyApiCallLog = new BusyApiCallLog();

        busyApiCallLog.setIp(request.getRemoteAddr());
        busyApiCallLog.setApiKey(apiKey);
        busyApiCallLog.setRequestUrl(request.getRequestURI());
        busyApiCallLog.setRequestData(hkapiPaymentInfoDTO.toString());
        String headerData="";
        String headerElement=null;
        Enumeration headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            headerElement = (String)headerNames.nextElement();
            if(headerElement != null){
                headerData+= headerElement+" : ";
                if (request.getHeader(headerElement) != null){
                    headerData+= request.getHeader(headerElement)+" , ";
                }
                else{
                    headerData+= "null , ";
                }
            }
        }
        busyApiCallLog.setRequestHeader(headerData);

        supplierTransactionService.logApiRequestFromBusy(busyApiCallLog);

        if(apiKey == null  || !apiKey.equals(HKAPIConstants.BUSY_API_KEY)){
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
            baseDTO.setMessage(HKAPIConstants.INVALID_API_KEY);
            return baseDTO;
        }

        Supplier supplier = supplierDao.findByTIN(hkapiPaymentInfoDTO.getTinNumber());

        if(supplier == null){
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
            baseDTO.setMessage(HKAPIConstants.NO_SUPPLIER);
            return baseDTO;
        }
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date paymentDate = null;
        try{
            paymentDate = df.parse(hkapiPaymentInfoDTO.getDate());
        }catch (ParseException e){
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
            baseDTO.setMessage(HKAPIConstants.INVALID_DATE);
            return baseDTO;
        }

        SupplierTransaction supplierTransaction = supplierTransactionService.createSupplierTransaction(supplier,
                EnumSupplierTransactionType.Payment.asSupplierTransactionType(), hkapiPaymentInfoDTO.getAmount(), paymentDate, hkapiPaymentInfoDTO.getBusyPaymentId(),
                hkapiPaymentInfoDTO.getBusySupplierBalance(), hkapiPaymentInfoDTO.getNarration());
        if(supplierTransaction == null){
            baseDTO.setStatus(HKAPIOperationStatus.ERROR);
        }
        return baseDTO;
	}

}
