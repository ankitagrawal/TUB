package com.hk.admin.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.dto.inventory.DebitNoteDto;
import com.hk.admin.dto.inventory.DebitNoteLineItemDto;
import com.hk.admin.pact.dao.inventory.DebitNoteDao;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.inventory.EnumDebitNoteStatus;
import com.hk.constants.inventory.EnumDebitNoteType;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;

@Component
public class DebitNoteManager {
	@Autowired
	DebitNoteDao debitNoteDao;

	public DebitNoteDto generateDebitNoteDto(DebitNote debitNote) {
		DebitNoteDto debitNoteDto = new DebitNoteDto();
		List<DebitNoteLineItemDto> debitNoteLineItemDtoList = new ArrayList<DebitNoteLineItemDto>();
		Double totalTax = 0.0;
		Double totalSurcharge = 0.0;
		Double totalTaxable = 0.0;
		Double totalPayable = 0.0;

		for (DebitNoteLineItem debitNoteLineItem : debitNote.getDebitNoteLineItems()) {

			Double taxable = 0.0;
			Double tax = 0.0;
			Double surcharge = 0.0;
			Double payable = 0.0;

			Sku sku = debitNoteLineItem.getSku();

			// ProductVariant productVariant =
			// debitNoteLineItem.getProductVariant();

			DebitNoteLineItemDto debitNoteLineItemDto = new DebitNoteLineItemDto();
			debitNoteLineItemDto.setDebitNoteLineItem(debitNoteLineItem);
			if (debitNoteLineItem != null && debitNoteLineItem.getCostPrice() != null
					&& debitNoteLineItem.getQty() != null) {
				taxable = debitNoteLineItem.getCostPrice() * debitNoteLineItem.getQty();
			}
			@SuppressWarnings("unused")
			Warehouse warehouse = debitNote.getWarehouse();
			if (sku != null) {
				ProductVariant productVariant = sku.getProductVariant();

				if (debitNote.getSupplier() != null && debitNote.getSupplier().getState() != null
						&& productVariant != null && sku.getTax() != null) {
					TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(debitNote.getSupplier(), sku, taxable);
					tax = taxComponent.getTax();
					surcharge = taxComponent.getSurcharge();
				}
			} else {
				if (debitNote.getSupplier() != null && debitNote.getSupplier().getState() != null
						&& debitNote.getWarehouse()!=null) {
				TaxComponent taxComponent = TaxUtil.getSupplierTaxForExtraInventory(debitNote.getSupplier(),
						debitNote.getWarehouse().getState(), debitNoteLineItem.getTax(), taxable);
				tax = taxComponent.getTax();
				surcharge = taxComponent.getSurcharge();
				}
			}

			payable = taxable + tax + surcharge;
			debitNoteLineItemDto.setTaxable(taxable);
			debitNoteLineItemDto.setPayable(payable);
			debitNoteLineItemDto.setSurcharge(surcharge);
			debitNoteLineItemDto.setTax(tax);

			debitNoteLineItemDtoList.add(debitNoteLineItemDto);
			totalTaxable += taxable;
			totalTax += tax;
			totalSurcharge += surcharge;
			totalPayable += payable;

		}
		debitNoteDto.setDebitNoteLineItemDtoList(debitNoteLineItemDtoList);
		debitNoteDto.setTotalTaxable(totalTaxable);
		debitNoteDto.setTotalTax(totalTax);
		debitNoteDto.setTotalSurcharge(totalSurcharge);
		debitNoteDto.setTotalPayable(totalPayable);
		return debitNoteDto;

	}

	public DebitNote createDebitNoteLineItem(DebitNote debitNote, List<RtvNoteLineItem> rtvNoteLineItems,
			List<ExtraInventoryLineItem> extraInventoryLineItems) {
		List<ExtraInventoryLineItem> eiLineItems = new ArrayList<ExtraInventoryLineItem>();

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
			if (eili.getSku() != null) {
				debitNoteLineItem.setSku(eili.getSku());
			}
			else{
				debitNoteLineItem.setProductName(eili.getProductName());
			}
			if (eili.getTax() != null) {
				debitNoteLineItem.setTax(eili.getTax());
			}
			debitNoteLineItems.add(debitNoteLineItem);
		}
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

	public DebitNoteDao getDebitNoteDao() {
		return debitNoteDao;
	}

	public void setDebitNoteDao(DebitNoteDao debitNoteDao) {
		this.debitNoteDao = debitNoteDao;
	}

}
