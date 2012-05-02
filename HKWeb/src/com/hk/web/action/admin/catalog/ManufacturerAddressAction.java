package com.hk.web.action.admin.catalog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationMethod;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.dao.core.AddressDao;
import com.hk.dao.core.ManufacturerDao;
import com.hk.dao.location.LocalityMapDao;
import com.hk.domain.LocalityMap;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.user.Address;
import com.hk.util.LatLongGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Nov 5, 2011
 * Time: 6:10:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ManufacturerAddressAction extends BasePaginatedAction {
  private Manufacturer manufacturer;
  private Address address;
  private List<Address> addresses = new ArrayList<Address>();
  String name;
  String mainAddressId;
  private Integer defaultPerPage = 21;
  Page addressPage;
  String city;
  Long manufacturerId;
  List<Double> latitude=new ArrayList<Double>();
  List<Double> longitude=new ArrayList<Double>();

  @ValidateNestedProperties({
      @Validate(field = "name", required = true, on = "addAddress"),
      @Validate(field = "line1", required = true, on = "addAddress"),
      @Validate(field = "city", required = true, on = "addAddress"),
      @Validate(field = "state", required = true, on = "addAddress"),
      @Validate(field = "pin", required = true, on = "addAddress"),
      @Validate(field = "phone", required = true, on = "addAddress")
  })

  
  ManufacturerDao manufacturerDao;
  
  AddressDao addressDao;
  
  LatLongGenerator latLongGenerator;
  
  LocalityMapDao localityMapDao;

  @ValidationMethod(on="addAddress")
   public void validateAddress()
    {
      if (address == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please enter all fields"));
         } else if (address.getName() == null || address.getLine1() == null ||address.getLine2()==null || address.getCity() == null || address.getState() == null || address.getPin() == null || address.getPhone() == null) {
           getContext().getValidationErrors().add("2", new SimpleError("Please enter all the fields"));
            }
   }


  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/manufacturerAddresses.jsp");
  }

  public Resolution addAddress() {
    if (address.getId() == null) {
      address = addressDao.save(address);
      addresses = manufacturer.getAddresses();
      addresses.add(address);
      manufacturer.setAddresses(addresses);
      manufacturerDao.save(manufacturer);
    }
    else {
      address = addressDao.save(address);
    }

      latLongGenerator.createLocalityMap(address, manufacturer);

    addRedirectAlertMessage(new SimpleMessage("Changes saved."));
    return new RedirectResolution("/pages/admin/addManufacturerAddresses.jsp");
  }

  public Resolution remove() {
    address.setDeleted(true);
    addressDao.save(address);
    addresses = manufacturer.getAddresses();
    addresses.remove(address);
    manufacturer.setAddresses(addresses);
    addRedirectAlertMessage(new LocalizableMessage("/SelectAddress.action.address.deleted"));
    return new RedirectResolution(ManufacturerAddressAction.class);
  }

  public Resolution addOrEditNewAddresses() {
    return new ForwardResolution("/pages/admin/addManufacturerAddresses.jsp");
  }

  public Resolution getManufacturerAddresses() {

    if (manufacturer != null)
      manufacturerId = manufacturer.getId();

    addressPage = addressDao.getVisibleAddressesForManufacturer(manufacturerId, city, getPageNo(), getPerPage());
    if (addressPage != null)
      addresses.addAll(addressPage.getList());
    if (addresses.size() > 0) {
      for (Address address : addresses) {
        LocalityMap localityMap= localityMapDao.findByAddress(address);
        if (localityMap != null) {
          latitude.add(localityMap.getLattitude());
          longitude.add(localityMap.getLongitude());
        }
        else
        {
          latitude.add(null);
          longitude.add(null);
         }
      }
    }
    return new ForwardResolution("/pages/admin/manufacturerAddresses.jsp");

  }

  public Resolution setAsDefaultAddress() {
    if (manufacturer != null) {
      manufacturer.setMainAddressId(address.getId().intValue());
      mainAddressId = manufacturer.getMainAddressId() != null ? manufacturer.getMainAddressId().toString() : "";
      manufacturerDao.save(manufacturer);
      addRedirectAlertMessage(new SimpleMessage("Default Set."));
    }
    return new ForwardResolution(ManufacturerAddressAction.class, "getManufacturerAddresses");
  }

  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  public List<Address> getAddresses() {
    return addresses;
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("manufacturerId");
    params.add("city");
    return params;
  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public int getPageCount() {
    return addressPage == null ? 0 : addressPage.getTotalPages();
  }

  public int getResultCount() {
    return addressPage == null ? 0 : addressPage.getTotalResults();
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Long getManufacturerId() {
    return manufacturerId;
  }

  public void setManufacturerId(Long manufacturerId) {
    this.manufacturerId = manufacturerId;
  }

  public List<Double> getLatitude() {
    return latitude;
  }

  public void setLatitude(List<Double> latitude) {
    this.latitude = latitude;
  }

  public List<Double> getLongitude() {
    return longitude;
  }

  public void setLongitude(List<Double> longitude) {
    this.longitude = longitude;
  }
}
