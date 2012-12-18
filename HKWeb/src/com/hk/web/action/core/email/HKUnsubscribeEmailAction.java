package com.hk.web.action.core.email;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 12/17/12
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
public class HKUnsubscribeEmailAction extends BaseAction {

    @Value("#{hkEnvProps['" + Keys.Env.hkEmailApiUrl + "']}")
    private String hkEmailApiUrl = null;

    @Validate(required = true)
    private String unsubscribeToken;

    public Resolution pre() {

        try{
            ClientRequest cr = new ClientRequest(hkEmailApiUrl + "?unsubscribeToken=" + unsubscribeToken );
            ClientResponse clientResponse = cr.post();
            if (clientResponse.getStatus() == ClientResponse.Status.OK.getStatusCode()){
                addRedirectAlertMessage(new SimpleMessage("You have been unsubscribed successfully."));
            }else{
                addRedirectAlertMessage(new SimpleMessage("Error unsubscribing from HealthKart. Please try again"));
            }
        }catch (Exception ex){

        }
        return new ForwardResolution("/pages/unsubscribeEmail.jsp");
    }
}
