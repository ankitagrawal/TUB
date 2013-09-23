package com.hk.web.action.admin.inventory.checkin;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.constants.core.Keys;
import com.hk.domain.inventory.Bin;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.admin.inventory.InventoryCheckinAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by IntelliJ IDEA. User: Seema Date: May 18, 2012 Time: 9:40:09 AM To
 * change this template use File | Settings | File Templates.
 */
public class InventoryBinAllocationAction extends BaseAction {
	private static Logger logger = Logger.getLogger(InventoryCheckinAction.class);
	@Autowired
	SkuGroupService skuGroupService;
	@Autowired
	BinDao binDao;
	@Autowired
	UserService userService;
	@Autowired
	SkuItemDao skuItemDao;
	@Autowired
	BaseDao baseDao;

	@Validate(required = true, on = "saveBin")
	private String barcode;
	@Validate(required = true, on = "saveBin")
	private String firstLocation;
	@Validate(required = true, on = "saveBin")
	private String finalLocation;
	private Warehouse warehouse;
	private FileBean fileBean;
	
	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	@DefaultHandler
	public Resolution pre() {
		if (getUserService().getWarehouseForLoggedInUser() != null) {
			warehouse = userService.getWarehouseForLoggedInUser();
			return new ForwardResolution("/pages/admin/inventoryBinAllocation.jsp");
		}
		else{
			return new RedirectResolution(AdminHomeAction.class);
		}
	}

	public Resolution saveBinForIndividualItem() {
		HealthkartResponse healthkartResponse = null;
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		if (firstLocation != null && finalLocation != null && barcode != null) {
			boolean validated = validate(firstLocation, finalLocation, barcode, warehouse);
			if (validated) {
				SkuItem skuItem = skuItemDao.getSkuItemByBarcode(barcode);
				Bin bin = binDao.findByBarCodeAndWarehouse(firstLocation, warehouse);
				skuItem.setBin(bin);
				skuItem = (SkuItem) baseDao.save(skuItem);
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Successfully saved item with its bin", dataMap);
			} else {
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Input seems to be wrong. Please Try Again", dataMap);
			}
		} else {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Input. Please Try Again", dataMap);
		}
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	private boolean validate(String firstLocation, String finalLocation, String barcode, Warehouse warehouse){
		boolean returnVal = true;
		if(!firstLocation.equals(finalLocation)){
			returnVal = false;
		}
		try{
			SkuItem skuItem = skuItemDao.getSkuItemByBarcode(barcode);
			if(skuItem==null){
				returnVal = false;
			}
		}
		catch(Exception e){
			returnVal = false;
		}
		Bin bin = binDao.findByBarCodeAndWarehouse(firstLocation, warehouse);
		if(bin == null){
			returnVal = false;
		}
		return returnVal;
  }

	public Resolution uploadBinAllocationFile(){
		if (getUserService().getWarehouseForLoggedInUser() != null) {
			warehouse = userService.getWarehouseForLoggedInUser();
			return new ForwardResolution("/pages/admin/uploadBinAllocation.jsp");
		}
		else{
			return new RedirectResolution(AdminHomeAction.class);
		}
	}
	
	public Resolution parseBinAllocationFile(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String binAllocationTextPath = adminUploadsPath + "/binAllocation/"+ warehouse.getIdentifier() + "-" +simpleDateFormat.format(new Date()) + ".txt";
		File binAllocationText = new File(binAllocationTextPath);
		binAllocationText.getParentFile().mkdirs();
		try {
			fileBean.save(binAllocationText);
			List<String> stringsToProcess = readbinAllocationText(binAllocationText);
			Map<String, Set<String>> binSkuItemBarcodeMap = processStringList(stringsToProcess);
			if(binSkuItemBarcodeMap==null){
				addRedirectAlertMessage(new SimpleMessage("Input file seems to in wrong format!! Please check once again or consult tech."));
				return new RedirectResolution(InventoryBinAllocationAction.class).addParameter("uploadBinAllocationFile");
			}
			if(binSkuItemBarcodeMap!=null && binSkuItemBarcodeMap.size()==0){
				addRedirectAlertMessage(new SimpleMessage("Input file seems to in wrong format!! Please check once again"));
			}
			if(binSkuItemBarcodeMap!=null && binSkuItemBarcodeMap.size()>0){
				boolean savedAllocation = saveAllBinSkuItemBarcodeMap(binSkuItemBarcodeMap);
				if(!savedAllocation){
				addRedirectAlertMessage(new SimpleMessage("Could not save the bin allocations!!"));
				return new RedirectResolution(InventoryBinAllocationAction.class).addParameter("uploadBinAllocationFile");
				}
				addRedirectAlertMessage(new SimpleMessage("Successfully saved all the Bin Allocations."));
			}
		}
		catch(Exception e){
			logger.debug("Error while reading the text file.");
		}
		return new RedirectResolution(InventoryBinAllocationAction.class).addParameter("uploadBinAllocationFile");
	}
	
	private List<String> readbinAllocationText(File binAllocationText){
		String myLine = null;
		List<String> stringList = new ArrayList<String>();
		try{
			FileReader input = new FileReader(binAllocationText);
			BufferedReader bufRead = new BufferedReader(input);
			while ( (myLine = bufRead.readLine()) != null)
			{ 
				if(myLine!=null && !myLine.isEmpty()){
				stringList.add(myLine);
				}
			}
		}catch(IOException e){
			logger.debug(e.getMessage());
		}
		return stringList;
	}
	
	private Map<String, Set<String>> processStringList(List<String> stringsToProcess){
		Map<String, Set<String>> binSkuItemBarcodeMap = new HashMap<String, Set<String>>();
		if (stringsToProcess != null && stringsToProcess.size() > 0) {
			Set<String> skuItemBarcodes = null;
			String currBin = "";
			String newBin = "";
			int i = stringsToProcess.size()-1;
			int j = 0;
			try {
				if (!stringsToProcess.get(0).startsWith("A")) {
					throw new invalidInputException();
				}
				while(j<=i){
					String string = stringsToProcess.get(j);
					if (string.startsWith("A")) {
						currBin = newBin;
						newBin = string;
						if (currBin.startsWith("A") && newBin.startsWith("A") && !currBin.equals(newBin)) {
							throw new invalidInputException();
						}
						if (currBin.equals(newBin)) {
							if(skuItemBarcodes!=null && skuItemBarcodes.size()>0){
							binSkuItemBarcodeMap.put(currBin, skuItemBarcodes);
							newBin = "";
							}
						} else {
							skuItemBarcodes = new HashSet<String>();
						}
					}
					if (!string.startsWith("A")) {
						skuItemBarcodes.add(string);
					}
					if(i==j){
						if(!string.startsWith("A") || !(currBin.equals(string)&&newBin.isEmpty())){
							throw new invalidInputException();
						}
					}
					++j;
				}
				return binSkuItemBarcodeMap;
			} catch (invalidInputException ie) {
				binSkuItemBarcodeMap = null;
				System.err.println(ie.message);
			}
		}
		return binSkuItemBarcodeMap;
	}
	
	private boolean saveAllBinSkuItemBarcodeMap(Map<String, Set<String>> binSkuItemBarcodeMap){
		boolean retrunVal = true;
		if(binSkuItemBarcodeMap!=null && binSkuItemBarcodeMap.size()>0){
			Set<Entry<String, Set<String>>> entrySet = binSkuItemBarcodeMap.entrySet();
			try{
				for (Entry<String, Set<String>> entry : entrySet) {
					String binBarcode = entry.getKey();
					Set<String> skuItemBarcodes = entry.getValue();
					for(String skuItemBarcode : skuItemBarcodes){
						Bin bin = binDao.findByBarCodeAndWarehouse(binBarcode, warehouse);
						SkuItem skuItem = skuItemDao.getSkuItemByBarcode(skuItemBarcode);
						skuItem.setBin(bin);
						skuItem = (SkuItem) baseDao.save(skuItem);
					}
				}
			}
			catch(Exception e){
				retrunVal = false;
				logger.debug("error saving bin allocations to the database");
			}
		}
		else{
			retrunVal = false;
		}
		return true;
	}
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getFirstLocation() {
		return firstLocation;
	}

	public void setFirstLocation(String firstLocation) {
		this.firstLocation = firstLocation;
	}

	public String getFinalLocation() {
		return finalLocation;
	}

	public void setFinalLocation(String finalLocation) {
		this.finalLocation = finalLocation;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}
}

class invalidInputException extends Exception{
	private static final long serialVersionUID = 1L;
	String message="";
	public invalidInputException() {
		this.message = "invalid";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}