package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.*;
import com.hk.admin.util.helper.XslPincodeParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.courier.RegionType;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vidur Malhotra
 * Date: 8/27/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AddPincodeRegionZoneAction extends BaseAction {

    @Autowired
    CourierService courierService;
    @Autowired
    PincodeRegionZoneService pincodeRegionZoneService;
    @Autowired
    PincodeService pincodeService;

    @Autowired
    XslPincodeParser xslPincodeParser;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    CourierGroupService courierGroupService;
    @Autowired
    CourierPricingEngineService courierPricingEngineService;


    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminDownloadsPath;
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    FileBean fileBean;
    private Pincode pincode;
    private PincodeRegionZone pincodeRegionZone;
    private List<PincodeRegionZone> pincodeRegionZoneList = null;
    private List<Pincode> pincodeList;
    private List<CourierGroup> courierGroupList = new ArrayList<CourierGroup>();
    private List<RegionType> regionTypeList = new ArrayList<RegionType>();
    private List<Warehouse> warehouseList = new ArrayList<Warehouse>();

    @Autowired
    PincodeCourierService pincodeCourierService;

    private static Logger logger = LoggerFactory.getLogger(AddPincodeRegionZoneAction.class);

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }

    public void initialize() {
        courierGroupList = courierGroupService.getAllCourierGroup();
        regionTypeList = courierPricingEngineService.getRegionTypeList();
        warehouseList = warehouseService.getServiceableWarehouses();
    }

    public Resolution savePincodeRegionList() {
        for (PincodeRegionZone pincodeRegionZone : pincodeRegionZoneList) {
            if(pincodeRegionZone.getId() == null) {
                Pincode pincode1 = pincodeRegionZone.getPincode();
                CourierGroup courierGroup1 = pincodeRegionZone.getCourierGroup();
                Warehouse warehouse1 = pincodeRegionZone.getWarehouse();
                PincodeRegionZone pincodeRegionZone1 = pincodeRegionZoneService.getPincodeRegionZone(courierGroup1, pincode1, warehouse1);
                if(pincodeRegionZone1 != null) {
                    addRedirectAlertMessage(new SimpleMessage("Entry already exists for pincode "
                            + pincode1.getPincode() + " for courier group " + courierGroup1.getName() + " correponsing to "
                            + warehouse1.getIdentifier() + " . Please update that"));
                } else {
                    pincodeRegionZoneService.save(pincodeRegionZone);
                }
            } else {
                pincodeRegionZoneService.save(pincodeRegionZone);
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Pincode Region saved"));
        return new ForwardResolution(AddPincodeRegionZoneAction.class, "searchPincodeRegion")
                                .addParameter("pincodeRegionZone",pincodeRegionZone);
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_MPA_VIEW}, authActionBean = AdminPermissionAction.class)
    public Resolution searchPincodeRegion() {
        Pincode pincode = pincodeRegionZone.getPincode();
        if (pincode == null) {
            addRedirectAlertMessage(new SimpleMessage("Pincode does not exist in System"));
        } else {
            pincodeRegionZoneList = pincodeRegionZoneService.getPincodeRegionZoneList(pincodeRegionZone.getCourierGroup(), pincode, pincodeRegionZone.getWarehouse());
            if (pincodeRegionZoneList == null) {
                addRedirectAlertMessage(new SimpleMessage("Pincode Region zone does not exist for Pincode"));
            }
            initialize();
        }
        return new ForwardResolution("/pages/admin/addPincodeRegionZone.jsp");
    }


    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }

    public PincodeRegionZoneService getPincodeRegionZoneService() {
        return pincodeRegionZoneService;
    }

    public void setPincodeRegionZoneService(PincodeRegionZoneService pincodeRegionZoneService) {
        this.pincodeRegionZoneService = pincodeRegionZoneService;
    }

    public PincodeService getPincodeService() {
        return pincodeService;
    }

    public void setPincodeService(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }



    public XslPincodeParser getXslPincodeParser() {
        return xslPincodeParser;
    }

    public void setXslPincodeParser(XslPincodeParser xslPincodeParser) {
        this.xslPincodeParser = xslPincodeParser;
    }

    public String getAdminDownloadsPath() {
        return adminDownloadsPath;
    }

    public void setAdminDownloadsPath(String adminDownloadsPath) {
        this.adminDownloadsPath = adminDownloadsPath;
    }

    public String getAdminUploadsPath() {
        return adminUploadsPath;
    }

    public void setAdminUploadsPath(String adminUploadsPath) {
        this.adminUploadsPath = adminUploadsPath;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public PincodeRegionZone getPincodeRegionZone() {
        return pincodeRegionZone;
    }

    public void setPincodeRegionZone(PincodeRegionZone pincodeRegionZone) {
        this.pincodeRegionZone = pincodeRegionZone;
    }

    public List<PincodeRegionZone> getPincodeRegionZoneList() {
        return pincodeRegionZoneList;
    }

    public void setPincodeRegionZoneList(List<PincodeRegionZone> pincodeRegionZoneList) {
        this.pincodeRegionZoneList = pincodeRegionZoneList;
    }

    public PincodeCourierService getPincodeCourierService() {
        return pincodeCourierService;
    }

    public void setPincodeCourierService(PincodeCourierService pincodeCourierService) {
        this.pincodeCourierService = pincodeCourierService;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        AddPincodeRegionZoneAction.logger = logger;
    }

    public List<Pincode> getPincodeList() {
        return pincodeList;
    }

    public void setPincodeList(List<Pincode> pincodeList) {
        this.pincodeList = pincodeList;
    }

    public List<CourierGroup> getCourierGroupList() {
        return courierGroupList;
    }

    public void setCourierGroupList(List<CourierGroup> courierGroupList) {
        this.courierGroupList = courierGroupList;
    }

    public List<RegionType> getRegionTypeList() {
        return regionTypeList;
    }

    public void setRegionTypeList(List<RegionType> regionTypeList) {
        this.regionTypeList = regionTypeList;
    }

    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }
}
