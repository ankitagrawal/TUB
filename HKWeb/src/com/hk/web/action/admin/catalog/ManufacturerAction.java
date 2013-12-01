package com.hk.web.action.admin.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.catalog.Manufacturer;
import com.hk.pact.dao.core.ManufacturerDao;
import com.hk.pact.dao.location.LocalityMapDao;
import com.hk.util.LatLongGenerator;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Nov 5, 2011 Time: 6:10:42 PM To change this template use File |
 * Settings | File Templates.
 */
@Component
public class ManufacturerAction extends BasePaginatedAction {

    private final String EMAIL_PATTERN    = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private Manufacturer manufacturer;
    private Manufacturer manufacturerDb;
    String               name;
    String               mainAddressId;
    private Integer      defaultPerPage   = 20;
    List<Manufacturer>   manufacturerList = new ArrayList<Manufacturer>();
    Page                 manufacturerPage;

    @Autowired
    ManufacturerDao      manufacturerDao;
    @Autowired
    LatLongGenerator     latLongGenerator;
    @Autowired
    LocalityMapDao       localityMapDao;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    public Resolution pre() {
        manufacturerPage = manufacturerDao.findManufacturersOrderedByName(getPageNo(), getPerPage());
        manufacturerList = manufacturerPage.getList();
        return new ForwardResolution("/pages/admin/manufacturer.jsp");
    }

    @ValidationMethod(on = "saveManufacturerDetails")
    public void validateManufacturerDetails() {
        if (manufacturer == null || manufacturer.getName() == null || manufacturer.getName().trim().equals("")) {
            getContext().getValidationErrors().add("1", new SimpleError("Please Enter Name"));
        }

      if(manufacturer==null || manufacturer.getEmail()==null || manufacturer.getEmail().trim().equals("") ){
          getContext().getValidationErrors().add("2", new SimpleError("Please Enter Email Id"));
      }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if(manufacturer!=null && manufacturer.getEmail()!=null && manufacturer.getEmail().contains(",")){
        String[] emails = manufacturer.getEmail().trim().split(",");
          for(String email : emails){
            if (manufacturer != null && email != null && !pattern.matcher(email).matches()) {
            getContext().getValidationErrors().add("3", new SimpleError("Please Enter Email ID in correct format"));
           }
          }
        }
      else{
        if (manufacturer != null && manufacturer.getEmail() != null && !pattern.matcher(manufacturer.getEmail()).matches()) {
            getContext().getValidationErrors().add("3", new SimpleError("Please Enter Email ID in correct format"));
        }
      }
    }

    public Resolution createOrEditManufacturer() {
        return new ForwardResolution("/pages/admin/editManufacturerDetails.jsp");
    }

    public Resolution saveManufacturerDetails() {
        if (manufacturer.getId() == null) {
	        Manufacturer manufacturerDb = manufacturerDao.findByName(manufacturer.getName().trim());
	        if(manufacturerDb != null){
		        addRedirectAlertMessage(new SimpleMessage("Oopps !!! Manufacturer  With Same Name Already Exist"));
		        return new ForwardResolution("/pages/admin/editManufacturerDetails.jsp");
	        }
            manufacturerDao.save(manufacturer);
        }
        else {
            manufacturerDb = getBaseDao().get(Manufacturer.class, manufacturer.getId());
            manufacturerDb.setName(manufacturer.getName().trim());
            manufacturerDb.setEmail(manufacturer.getEmail().trim());
            manufacturerDb.setDescription(manufacturer.getDescription());
            manufacturerDb.setWebsite(manufacturer.getWebsite());
            manufacturerDb = (Manufacturer) getBaseDao().save(manufacturerDb);
        }
        addRedirectAlertMessage(new SimpleMessage("Manufacturer is saved!!"));
        return new ForwardResolution("/pages/admin/editManufacturerDetails.jsp");
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Set<String> getParamSet() {
        return null;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return manufacturerPage == null ? 0 : manufacturerPage.getTotalPages();
    }

    public int getResultCount() {
        return manufacturerPage == null ? 0 : manufacturerPage.getTotalResults();
    }

    public List<Manufacturer> getManufacturerList() {
        return manufacturerList;
    }

    public void setManufacturerList(List<Manufacturer> manufacturerList) {
        this.manufacturerList = manufacturerList;
    }

}
