package com.hk.web.action.core.email;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 13/01/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class HKUnsubscribeEmailAction extends BaseAction {
    @Autowired
    UserService userService;

    @Value("#{hkEnvProps['" + Keys.Env.mailBoltUrl + "']}")
    private String mailBoltUrl;

    private static Logger logger = LoggerFactory.getLogger(HKUnsubscribeEmailAction.class);


    public Resolution pre() {
        String unsubscribeToken = getContext().getRequest().getParameterValues("unsubscribeToken")[0];
        boolean success = userService.unsubscribeUser(unsubscribeToken);
        if (!success) {
            addRedirectAlertMessage(new SimpleMessage("Invalid email."));
        } else {

            String postUrl = mailBoltUrl + "HKDataStore/unsubscribes/" + unsubscribeToken;

            ClientRequest request = new ClientRequest(postUrl);
            request.setHttpMethod("POST");
            ClientResponse<String> response = null;
            try {
                response = request.post();
                int status = response.getStatus();
                logger.info("Calling Post API " + postUrl);
                if (status == 200) {
                    logger.debug("Post API returned correct status");
                }
            } catch (Exception e) {
                logger.error("Something bad happened " + postUrl);
            }

            addRedirectAlertMessage(new SimpleMessage("We have received your request. We will unsubscribe you shortly."));
        }
        if (isHybridRelease()) {
            return new ForwardResolution("/pages/unsubscribeEmailBeta.jsp");

        }
        return new ForwardResolution("/pages/unsubscribeEmail.jsp");

    }

}
