package com.hk.web.action.admin.shipment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Oct 13, 2011
 * Time: 5:17:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DELIVERY_QUEUE}, authActionBean = AdminPermissionAction.class)
@Component
public class UpdateAFLChhotuDeliveryStatusAction extends BaseAction{

  private Date startDate;
  private Date endDate;

  @Autowired
  DeliveryStatusUpdateManager deliveryStatusUpdateManager;
  
  @DefaultHandler
  public Resolution pre(){

    return new ForwardResolution("/pages/admin/updateAFLChhotuDeliveryStatus.jsp");
  }

  public Resolution updateAFLStatus() throws MalformedURLException, IOException,Exception{

    User loggedOnUser = null;
    if (getPrincipal() != null) {
      loggedOnUser = getUserService().getUserById(getPrincipal().getId());
    }
    int numberOfOrdersUpdated=0;
    String testIt;
    try {

      numberOfOrdersUpdated = deliveryStatusUpdateManager.updateDeliveryStatusAFL(startDate,endDate,loggedOnUser);
    } catch (Exception e) {
      //logger.error("Exception while reading excel sheet.", e);
      addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
      return new ForwardResolution("/pages/admin/updateAFLChhotuDeliveryStatus.jsp");
    }

    addRedirectAlertMessage(new SimpleMessage("Database Updated - " + numberOfOrdersUpdated +  " orders Updated."));

    return new ForwardResolution("/pages/admin/updateAFLChhotuDeliveryStatus.jsp");


    /*String awbForTest = "84121272408167223";
    URL url = new URL("http://trackntrace.aflwiz.com/aflwiztrack?shpntnum="+awbForTest);
    
    BufferedReader in = new BufferedReader(
          new InputStreamReader(
          url.openStream()));
    String inputLine;
    String test="";

    while ((inputLine = in.readLine()) != null){
        if(inputLine != null){
          test += inputLine;
        }
        System.out.println(inputLine);
    }
    in.close();
    Node xmlNode = new XmlParser().parseText(test);
    System.out.println("test this " + ((Node)(((Node)(xmlNode.children().get(1))).children().get(8))).text()  );


    addRedirectAlertMessage(new SimpleMessage("Database Updated - " + startDate + " " + endDate + " orders Updated."+test));

    return new ForwardResolution("/pages/admin/updateAFLChhotuDeliveryStatus.jsp");*/
  }
  public Resolution updateChhotuStatus() throws MalformedURLException, IOException,Exception{

     User loggedOnUser = null;
    if (getPrincipal() != null) {
      loggedOnUser =  getUserService().getUserById(getPrincipal().getId());
    }
    int numberOfOrdersUpdated=0;
    String testIt;
    try {

      numberOfOrdersUpdated = deliveryStatusUpdateManager.updateDeliveryStatusChhotu(startDate,endDate,loggedOnUser);
    } catch (Exception e) {
      //logger.error("Exception while reading excel sheet.", e);
      addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
      return new ForwardResolution("/pages/admin/updateAFLChhotuDeliveryStatus.jsp");
    }

    addRedirectAlertMessage(new SimpleMessage("Database Updated - " + numberOfOrdersUpdated +  " orders Updated."));

    return new ForwardResolution("/pages/admin/updateAFLChhotuDeliveryStatus.jsp");
  }

  public Date getStartDate() {
    return startDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

}
