package com.hk.web.action.admin.webServices;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.core.search.UsersSearchCriteria;
import com.hk.dto.user.UserDTO;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserSearchService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/11/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMailBoltServicesAction extends BaseAction {
    @Autowired
    UserDao userDao;
    @Autowired
    UserSearchService userSearchService;

    // variables passed as params to services
    private String zones;
    private String pvs;
    private String prodnames;
    private String production;
    private String cities;
    private String states;
    private String categories;
    private String verified;
    private String equality;
    private String userOrderCount;
    private String emails;
    private String storeIds;

    // result showing variables
    private int result;
    private Long time;
    private String resolutionPageString = "/pages/admin/webServices/mailServices.jsp";


    private Integer convertUserOrderCount(String userOrderCount) {
        Integer uoc = null;
        try {
            uoc = Integer.parseInt(userOrderCount);
        } catch (Exception e) {
            uoc = null;
        }
        return uoc;
    }

    private Boolean convertVerified(String verified) {
        Boolean ver = null;
        try {
            ver = "true".equalsIgnoreCase(verified) ? true : "false".equalsIgnoreCase(verified) ? false : null;
        } catch (Exception e) {
            this.verified = null;
        }
        return ver;
    }

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution(resolutionPageString);
    }

    public Resolution testServices() {
        Long startTime = System.currentTimeMillis();
        UsersSearchCriteria criteria = new UsersSearchCriteria();
        if (prodnames != null) {
            List<String> ids = Arrays.asList(prodnames.split(","));
            criteria.setProductIds(ids);
        }
        if (categories != null) {
            List<String> ids = Arrays.asList(categories.split(","));
            criteria.setCategories(ids);
        }
        if (userOrderCount != null) {
            criteria.setUserOrderCount(convertUserOrderCount(userOrderCount));
        }
        if (equality != null) {
            criteria.setEquality(equality);
        }
        if (verified != null) {
            criteria.setVerified(convertVerified(verified));
        }
        if (pvs != null) {
            List<String> pvids = Arrays.asList(pvs.split(","));
            criteria.setProductVariantIds(pvids);
        }
        if (emails != null) {
            List<String> em = Arrays.asList(emails.split(","));
            criteria.setEmails(em);
        }
        if (cities != null) {
            List<String> city = Arrays.asList(cities.split(","));
            criteria.setCities(city);
        }
        if (states != null) {
            List<String> state = Arrays.asList(states.split(","));
            criteria.setStates(state);
        }
        if (zones != null) {
            List<String> zone = Arrays.asList(zones.split(","));
            List<Long> zonel = new ArrayList<Long>();
            for (String s : zone) {
                try {
                    Long l = Long.parseLong(s);
                    zonel.add(l);
                } catch (Exception e) {
                }
            }
            criteria.setZones(zonel);
        }
        if (storeIds != null) {
            List<String> st = Arrays.asList(storeIds.split(","));
            List<Long> storel = new ArrayList<Long>();
            for (String s : st) {
                try {
                    Long l = Long.parseLong(s);
                    storel.add(l);
                } catch (Exception e) {
                }
            }
            criteria.setStoreIds(storel);
        }


        if (production != null && "true".equalsIgnoreCase(production)) {
            UsersSearchCriteria.setDebugMode("false");
        } else {
            UsersSearchCriteria.setDebugMode("true");
        }
        List<UserDTO> ems = null;
        try {
            ems = userSearchService.searchUserInfo(criteria);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long endTime = System.currentTimeMillis();

        result = ems.size();
        time = endTime - startTime;

        // write output to file
        StringBuffer summarySB = new StringBuffer();
        String summary = "result size: " + result + " time taken in millis: " + (endTime - startTime);
        summarySB.append("Summary: ").append(summary).append("\n");

        boolean debug = !(production != null && "true".equalsIgnoreCase(production));
        StringBuffer resultSB = new StringBuffer();
        resultSB.append("\n").append(debug ?
                "login, email, name, subscriptionMask, unsubscribeToken" :
                "email, login, name, subscriptionMask, unsubscribeToken");

        for (UserDTO o : ems) {
            resultSB.append("\n");

            if (debug) {
                resultSB.append(o.login).append(",").append(o.email).append(",").append(o.name).append(",").append(o.subscribedMask).append(",").append(o.unsubscribeToken);
            } else {
                resultSB.append(o.email).append(",").append(o.login).append(",").append(o.name).append(",").append(o.subscribedMask).append(",").append(o.unsubscribeToken);
            }
        }

        return new StreamingResolution("text/csv", summarySB.append("\n\n").toString() + resultSB.toString()).setFilename("queryResult.csv");
    }

    public String getZones() {
        return zones;
    }

    public void setZones(String zones) {
        this.zones = zones;
    }

    public String getPvs() {
        return pvs;
    }

    public void setPvs(String pvs) {
        this.pvs = pvs;
    }

    public String getProdnames() {
        return prodnames;
    }

    public void setProdnames(String prodnames) {
        this.prodnames = prodnames;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getEquality() {
        return equality;
    }

    public void setEquality(String equality) {
        this.equality = equality;
    }

    public String getUserOrderCount() {
        return userOrderCount;
    }

    public void setUserOrderCount(String userOrderCount) {
        this.userOrderCount = userOrderCount;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(String storeIds) {
        this.storeIds = storeIds;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }
}
