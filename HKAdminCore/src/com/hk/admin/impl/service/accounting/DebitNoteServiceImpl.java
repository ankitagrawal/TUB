package com.hk.admin.impl.service.accounting;

import com.hk.admin.dto.inventory.DebitNoteDto;
import com.hk.admin.dto.inventory.DebitNoteLineItemDto;
import com.hk.admin.pact.dao.inventory.DebitNoteDao;
import com.hk.admin.pact.service.accounting.DebitNoteService;
import com.hk.admin.pact.service.accounting.SeekInvoiceNumService;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.core.EnumTax;
import com.hk.constants.core.TaxConstants;
import com.hk.constants.inventory.EnumDebitNoteStatus;
import com.hk.constants.inventory.EnumDebitNoteType;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.helper.InvoiceNumHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DebitNoteServiceImpl implements DebitNoteService {
	@Autowired
	DebitNoteDao debitNoteDao;

    @Autowired
    SeekInvoiceNumService seekInvoiceNumService;

	public DebitNoteDto generateDebitNoteDto(DebitNote debitNote) {
		DebitNoteDto debitNoteDto = new DebitNoteDto();
		List<DebitNoteLineItemDto> debitNoteLineItemDtoList = new ArrayList<DebitNoteLineItemDto>();
		/*Double totalTax = 0.0;
		Double totalSurcharge = 0.0;
		Double totalTaxable = 0.0;
		Double totalPayable = 0.0;
		Double discount;*/

		for (DebitNoteLineItem debitNoteLineItem : debitNote.getDebitNoteLineItems()) {

			DebitNoteLineItemDto debitNoteLineItemDto = new DebitNoteLineItemDto();
			debitNoteLineItemDto.setDebitNoteLineItem(debitNoteLineItem);
			/*Double taxable = 0.0;
			Double tax = 0.0;
			Double surcharge = 0.0;
			Double payable = 0.0;

			Sku sku = debitNoteLineItem.getSku();

			if (debitNoteLineItem != null && debitNoteLineItem.getCostPrice() != null
					&& debitNoteLineItem.getQty() != null) {
				taxable = (debitNoteLineItem.getCostPrice()-debitNoteLineItem.getCostPrice() * debitNoteLineItem.getDiscountPercent() / 100 )* debitNoteLineItem.getQty();
			}
			@SuppressWarnings("unused")
			Warehouse warehouse = debitNote.getWarehouse();*/
			
			/*if (sku != null) {
				ProductVariant productVariant = sku.getProductVariant();

				if (debitNote.getSupplier() != null && debitNote.getSupplier().getState() != null
						&& productVariant != null && debitNoteLineItem.getTax() != null) {
					TaxComponent taxComponent = TaxUtil.getSupplierTaxForVariedTaxRatesWithSku(debitNote.getSupplier(),sku, debitNoteLineItem.getTax(), taxable);
					tax = taxComponent.getTax();
					surcharge = taxComponent.getSurcharge();
				}
			} else*/ 
			
				/*if(sku==null){
				if (debitNote.getSupplier() != null && debitNote.getSupplier().getState() != null
						&& debitNote.getWarehouse()!=null) {
				TaxComponent taxComponent = TaxUtil.getSupplierTaxForVariedTaxRatesWithoutSku(debitNote.getSupplier(),
						debitNote.getWarehouse().getState(), debitNoteLineItem.getTax(), taxable);
				tax = taxComponent.getTax();
				surcharge = taxComponent.getSurcharge();
				}
			}*/

			//payable = taxable + tax + surcharge;
			
			
			debitNoteLineItemDto.setTaxable(debitNoteLineItem.getTaxableAmount());
			debitNoteLineItemDto.setPayable(debitNoteLineItem.getPayableAmount());
			debitNoteLineItemDto.setSurcharge(debitNoteLineItem.getSurchargeAmount());
			debitNoteLineItemDto.setTax(debitNoteLineItem.getTaxAmount());
			debitNoteLineItemDto.setDiscountPercent(debitNoteLineItem.getDiscountPercent());
			debitNoteLineItemDtoList.add(debitNoteLineItemDto);
			/*
			totalTaxable += taxable;
			totalTax += tax;
			totalSurcharge += surcharge;
			totalPayable += payable;*/
		}
		debitNoteDto.setDebitNoteLineItemDtoList(debitNoteLineItemDtoList);
		debitNoteDto.setTotalTaxable(debitNote.getTaxableAmount());
		debitNoteDto.setTotalTax(debitNote.getTaxAmount());
		debitNoteDto.setTotalSurcharge(debitNote.getSurchargeAmount());
		debitNoteDto.setTotalPayable(debitNote.getFinalDebitAmount());
		debitNoteDto.setTotalDiscount(debitNote.getDiscount());
		debitNoteDto.setFinalDebitAmount(debitNote.getFinalDebitAmount());
		return debitNoteDto;

	}

	public DebitNote createDebitNoteLineItem(DebitNote debitNote, List<RtvNoteLineItem> rtvNoteLineItems,
			List<ExtraInventoryLineItem> extraInventoryLineItems) {
		
		Double finalDebitAmount = 0.0;
		List<ExtraInventoryLineItem> eiLineItems = new ArrayList<ExtraInventoryLineItem>();
		Double totalTaxable = 0.0D, totalTax = 0.0D, totalSurcharge = 0.0D, totalPayable = 0.0D;
		if (rtvNoteLineItems != null && rtvNoteLineItems.size() > 0) {
			for (RtvNoteLineItem lineItem : rtvNoteLineItems) {
				ExtraInventoryLineItem eiLiItems = lineItem.getExtraInventoryLineItem();
				if (eiLiItems != null) {
					eiLineItems.add(eiLiItems);
				}
			}
		}

		if (extraInventoryLineItems != null && extraInventoryLineItems.size() > 0) {
			eiLineItems.addAll(extraInventoryLineItems);
		}

		debitNote.setCreateDate(new Date());
		debitNote.setDebitNoteStatus(EnumDebitNoteStatus.Created.asDebitNoteStatus());
		debitNote.setDebitNoteType(EnumDebitNoteType.PreCheckin.asEnumDebitNoteType());
		// TODO: set invoice number and
		if (rtvNoteLineItems != null && rtvNoteLineItems.size() > 0) {
			RtvNote note = rtvNoteLineItems.get(0).getRtvNote();
			debitNote.setSupplier(note.getExtraInventory().getPurchaseOrder().getSupplier());
			debitNote.setWarehouse(note.getExtraInventory().getPurchaseOrder().getWarehouse());
		} else if (extraInventoryLineItems != null && extraInventoryLineItems.size() > 0) {
			ExtraInventoryLineItem eili = extraInventoryLineItems.get(0);
			debitNote.setSupplier(eili.getExtraInventory().getPurchaseOrder().getSupplier());
			debitNote.setWarehouse(eili.getExtraInventory().getPurchaseOrder().getWarehouse());
		}
		Set<DebitNoteLineItem> debitNoteLineItems = new HashSet<DebitNoteLineItem>();
		for (ExtraInventoryLineItem eili : eiLineItems) {
			DebitNoteLineItem debitNoteLineItem = new DebitNoteLineItem();
			debitNoteLineItem.setCostPrice(eili.getCostPrice());
			debitNoteLineItem.setMrp(eili.getMrp());
			debitNoteLineItem.setQty(eili.getReceivedQty());
			debitNoteLineItem.setTaxableAmount(eili.getTaxableAmount());
			debitNoteLineItem.setTaxAmount(eili.getTaxAmount());
			debitNoteLineItem.setSurchargeAmount(eili.getSurchargeAmount());
			debitNoteLineItem.setPayableAmount(eili.getPayableAmount());
			if (eili.getSku() != null) {
				debitNoteLineItem.setSku(eili.getSku());
			}
			else{
				debitNoteLineItem.setProductName(eili.getProductName());
			}
			if (eili.getTax() != null) {
				debitNoteLineItem.setTax(eili.getTax());
			}
			totalTaxable += eili.getTaxableAmount();
			totalTax += eili.getTaxAmount();
			totalSurcharge += eili.getSurchargeAmount();
			totalPayable += eili.getPayableAmount();
			finalDebitAmount+=eili.getPayableAmount();
			debitNoteLineItem.setDiscountPercent(0.0D);
			debitNoteLineItems.add(debitNoteLineItem);
		}
		debitNote.setDiscount(0.0D);
		debitNote.setTaxableAmount(totalTaxable);
		debitNote.setTaxAmount(totalTax);
		debitNote.setSurchargeAmount(totalSurcharge);
		debitNote.setPayableAmount(totalPayable);
		finalDebitAmount+=debitNote.getFreightForwardingCharges();
		debitNote.setFinalDebitAmount(finalDebitAmount);
		debitNote = (DebitNote) getDebitNoteDao().save(debitNote);
		Set<DebitNoteLineItem> debitNoteLineItemSet = new HashSet<DebitNoteLineItem>();
		for (DebitNoteLineItem debitNoteLineItem : debitNoteLineItems) {
			debitNoteLineItem.setDebitNote(debitNote);
			debitNoteLineItem = (DebitNoteLineItem) getDebitNoteDao().save(debitNoteLineItem);
			debitNoteLineItemSet.add(debitNoteLineItem);
		}
		debitNote.setDebitNoteLineItems(debitNoteLineItemSet);
		debitNote = (DebitNote) getDebitNoteDao().save(debitNote);
		return debitNote;

	}
	
	@Override
	public DebitNote createDebitNoteLineItemWithRVLineItems(DebitNote debitNote, List<RvLineItem> rvLineItems) {
		// TODO Auto-generated method stub
    	debitNote.setCreateDate(new Date());
    	Double finalDebitAmount = 0.0;
    	Double totalTaxable = 0.0D, totalTax = 0.0D, totalSurcharge = 0.0D, totalPayable = 0.0D;
		debitNote.setDebitNoteStatus(EnumDebitNoteStatus.Created.asDebitNoteStatus());
		debitNote.setDebitNoteType(EnumDebitNoteType.PostCheckin.asEnumDebitNoteType());
		debitNote.setFreightForwardingCharges(0.0D);
		Set<DebitNoteLineItem> debitNoteLineItems = new HashSet<DebitNoteLineItem>();
		
		for(RvLineItem lineItem: rvLineItems){
			Double taxableAmount = 0.0D;
			Double discountPercentage = 0D;
			DebitNoteLineItem debitNoteLineItem = new DebitNoteLineItem();
			debitNoteLineItem.setCostPrice(lineItem.getCostPrice());
			debitNoteLineItem.setMrp(lineItem.getMrp());
			debitNoteLineItem.setQty(lineItem.getReconciledQty());
			debitNoteLineItem.setDiscountPercent(0.0D);
			if (lineItem.getSku() != null) {
				debitNoteLineItem.setSku(lineItem.getSku());
				debitNoteLineItem.setProductName(lineItem.getSku().getProductVariant().getVariantName());
				if(debitNote.getSupplier().getState().equals(debitNote.getWarehouse().getState())){
					debitNoteLineItem.setTax(lineItem.getSku().getTax());
				}
				else{
					Tax tax = new Tax();
					tax.setId(EnumTax.CST.getId());
					tax.setName(EnumTax.CST.getName());
					tax.setType(EnumTax.CST.getType());
					tax.setValue(EnumTax.CST.getValue());
					debitNoteLineItem.setTax(tax);
				}
				taxableAmount = (lineItem.getReconciledQty() * (lineItem.getCostPrice() - lineItem.getCostPrice() * discountPercentage / 100));
				totalTaxable += taxableAmount;
				TaxComponent taxComponent = TaxUtil.getSupplierTaxForVariedTaxRatesWithoutSku(debitNote.getSupplier(),debitNote.getWarehouse().getState(), debitNoteLineItem.getTax(), taxableAmount);
				totalTax += taxComponent.getTax();
				totalSurcharge += taxComponent.getSurcharge();
				totalPayable += taxComponent.getPayable();
				finalDebitAmount+=totalPayable;
				debitNoteLineItem.setTaxableAmount(taxableAmount);
				debitNoteLineItem.setTaxAmount(taxComponent.getTax());
				debitNoteLineItem.setSurchargeAmount(taxComponent.getSurcharge());
				debitNoteLineItem.setPayableAmount(taxComponent.getPayable());
			}
			debitNoteLineItems.add(debitNoteLineItem);
		}
		debitNote.setPayableAmount(totalPayable);
		debitNote.setTaxableAmount(totalTaxable);
		debitNote.setTaxAmount(totalTax);
		debitNote.setSurchargeAmount(totalSurcharge);
		finalDebitAmount+=debitNote.getFreightForwardingCharges();
		debitNote.setFinalDebitAmount(finalDebitAmount);
		debitNote.setDiscount(0.0D);
		debitNote = (DebitNote) getDebitNoteDao().save(debitNote);
		Set<DebitNoteLineItem> debitNoteLineItemSet = new HashSet<DebitNoteLineItem>();
		for (DebitNoteLineItem debitNoteLineItem : debitNoteLineItems) {
			debitNoteLineItem.setDebitNote(debitNote);
			debitNoteLineItem = (DebitNoteLineItem) getDebitNoteDao().save(debitNoteLineItem);
			debitNoteLineItemSet.add(debitNoteLineItem);
		}
		debitNote.setDebitNoteLineItems(debitNoteLineItemSet);
		debitNote = (DebitNote) getDebitNoteDao().save(debitNote);
		return debitNote;
		
	}

    @Override
    public DebitNote save(DebitNote debitNote) {
        String invoiceNumType = InvoiceNumHelper.PREFIX_FOR_DEBIT_NOTE;
        if(debitNote.getDebitNoteStatus().getId().equals(EnumDebitNoteStatus.CLosed.getId())){
            debitNote.setDebitNoteNumber(seekInvoiceNumService.getInvoiceNum(invoiceNumType, debitNote.getWarehouse()));
        }
        return (DebitNote)getDebitNoteDao().save(debitNote);
    }

    @Override
    public DebitNote save(DebitNote debitNote, List<DebitNoteLineItem> debitNoteLineItems) {
    	Double finalDebitAmount = 0.0;
        for (DebitNoteLineItem debitNoteLineItem : debitNoteLineItems) {
            if (debitNoteLineItem.getDebitNote() == null) {
                debitNoteLineItem.setDebitNote(debitNote);
            }
            if (debitNoteLineItem.getQty() != null && debitNoteLineItem.getQty() <= 0) {
                getDebitNoteDao().delete(debitNoteLineItem);
            } else {
                getDebitNoteDao().save(debitNoteLineItem);
            }
        }
        return save(debitNote);
    }
    
    public DebitNoteDao getDebitNoteDao() {
		return debitNoteDao;
	}


}
