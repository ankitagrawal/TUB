package com.hk.web.action.core.autocomplete;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.HealthkartResponse;
import com.hk.pact.service.core.CityService;
import com.hk.domain.core.City;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.AddressService;
import com.hk.domain.core.Pincode;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.JsonResolution;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Jan 17, 2013
 * Time: 5:51:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AutoCompleteAction extends BaseAction {

  @Autowired
	CityService cityService;
	@Autowired
	PincodeService pincodeService;
	@Autowired
	AddressService addressService;
	@Autowired
	SkuService skuService;
	@Autowired
	ProductService productService;

	private String q = "";
	private String pincode;
	private Warehouse warehouse;

	@DontValidate
	@JsonHandler
	public Resolution populateCity() {
		List<String> cityList = new ArrayList<String>();
		List<City> cities = cityService.getAllCity();
		for (City city : cities) {
      String cityName = city.getName();
			if (cityName.startsWith(q.trim().toUpperCase()))
				  cityList.add(cityName);
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "done", cityList);
		return new JsonResolution(healthkartResponse);
	}


  @SuppressWarnings("unchecked")
	@JsonHandler
	public Resolution populateAddress() {
		Pincode pincode = null;
    HealthkartResponse healthkartResponse = null;
    Map data = new HashMap();
    if (getPincode().trim() != null) {
			pincode = pincodeService.getByPincode(getPincode().trim());
		}
		if (pincode == null) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Pincode !!");
		}else{
		data.put("cityName",pincode.getCity().getName());
    data.put("stateName",pincode.getState().getName());
		healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Pincode", data);
    }
		return new JsonResolution(healthkartResponse);
	}

	@SuppressWarnings("unchecked")
	@JsonHandler
	public Resolution getProductsInWarehouse() {
		HealthkartResponse healthkartResponse = null;
		Map dataMap = new HashMap();

		if (warehouse == null) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid warehouse !!");
		}else{
			List<Sku> skuList = skuService.getSKUsByProductNameAndWarehouse(q, warehouse.getId());
			List<Long> skuIdList = new ArrayList<Long>(0);

			List<String> productDetailList = new ArrayList<String>(0);
			for (Sku sku : skuList) {
				String productDetail = sku.getProductVariant().getProduct().getName() + " " + sku.getProductVariant().getOptionsCommaSeparated();
				productDetailList.add(productDetail);
				skuIdList.add(sku.getId());
			}
//			dataMap.put("skuIdList", skuIdList);
			//dataMap.put("productDetailList", productDetailList);
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Warehouse", productDetailList);
		}
		return new JsonResolution(healthkartResponse);
	}

	@DontValidate
	@JsonHandler
	public Resolution populateBrand() {
		List<String> brandList = productService.getAllBrands(q);
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "done", brandList);
		return new JsonResolution(healthkartResponse);
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
}
