package com.hk.web.action.pages;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.sale.ClearanceSaleProduct;
import com.hk.impl.dao.sale.ClearanceSaleProductDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@UrlBinding ("/clearance-sale")
public class ClearanceSaleAction extends BaseAction {

  @Autowired
  ClearanceSaleProductDao clearanceSaleProductDao;

  private List<ClearanceSaleProduct> clearanceSaleProductList;

	public Resolution pre() {
    clearanceSaleProductList = clearanceSaleProductDao.getProducts();
		return new ForwardResolution("/pages/campaign/clearance/sale.jsp");
	}

  public List<ClearanceSaleProduct> getClearanceSaleProductList() {
    return clearanceSaleProductList;
  }
}