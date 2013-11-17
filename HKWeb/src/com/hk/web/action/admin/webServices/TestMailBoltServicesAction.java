package com.hk.web.action.admin.webServices;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserSearchService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    private String params;
    private String minimum;
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

    ////////////////////////////////////////REMOVE////////////////////////////////////////////////////////////////////
    private Integer setUserOrderCount2(String userOrderCount) {
        Integer uoc = null;
        try {
            uoc = Integer.parseInt(userOrderCount);
        } catch (Exception e) {
            uoc = null;
        }
        return uoc;
    }

    private Boolean setVerified2(String verified) {
        Boolean ver = null;
        try {
            ver = "true".equalsIgnoreCase(verified) ? true : "false".equalsIgnoreCase(verified) ? false : null;
        } catch (Exception e) {
            this.verified = null;
        }
        return ver;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 1. add functionality for outputing result to csv file
    // 2. improve ui
    // 3. change functioanlity in order to send strings from services
    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution(resolutionPageString);
    }


    public Resolution testServices() {
        Long startTime = System.currentTimeMillis();
        UsersSearchCriteria criteria = new UsersSearchCriteria();
        String prodnames = params;
        if (prodnames != null) {
            List<String> ids = Arrays.asList(prodnames.split(","));
            criteria.setProductIds(ids);
        }
        if (userOrderCount != null) {
            criteria.setUserOrderCount(setUserOrderCount2(userOrderCount));
        }
        if (equality != null) {
            criteria.setEquality(equality);
        }
        if (verified != null) {
            criteria.setVerified(setVerified2(verified));
        }
        if (pvs != null) {
            List<String> pvids = Arrays.asList(pvs.split(","));
            criteria.setProductVariantIds(pvids);
        }
        if (categories != null) {
            List<String> category = Arrays.asList(categories.split(","));
            criteria.setCategories(category);
        }
        if (emails != null) {
            List<String> emils = Arrays.asList(emails.split(","));
            criteria.setEmails(emils);
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


        List<Object[]> ems = null;
        List<User> users = null;
        try {
            if ("1".equals(minimum)) {
                ems = userSearchService.searchUserInfo(criteria);
            } else {
                users = userSearchService.searchUsers(criteria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("1".equals(minimum)) {
            result = ems.size();
        } else {
            result = users.size();
        }
        Long endTime = System.currentTimeMillis();
        time = endTime - startTime;

        // write output to file
        StringBuffer sb = new StringBuffer();
        String summary = "result size: " + result + " time taken in millis: " + (endTime - startTime);
        sb.append("Summary").append("\n").append(summary).append("\n");

        /*
        * if (NOT_ON_PRODUCTION) {
                projList.add(Projections.distinct(Projections.property("user.login")));
                projList.add(Projections.property("user.email"));
                projList.add(Projections.property("user.name"));
                projList.add(Projections.property("user.subscribedMask"));
                projList.add(Projections.property("user.unsubscribeToken"));
            } else {
                projList.add(Projections.distinct(Projections.property("user.email")));
                projList.add(Projections.property("user.login"));
                projList.add(Projections.property("user.name"));
                projList.add(Projections.property("user.subscribedMask"));
                projList.add(Projections.property("user.unsubscribeToken"));
            }
        * */
        sb.append("\n").append(UsersSearchCriteria.NOT_ON_PRODUCTION ?
                "login, email, name, subscriptionMask, unsubscribeToken" :
                "email, login, name, subscriptionMask, unsubscribeToken");

        if ("1".equals(minimum)) {
            for (Object[] o : ems) {
                sb.append("\n");
                for (Object ob : o) {
                    sb.append(ob);
                    sb.append(",");
                }
            }
        } else {
            //TODO : implemnt this
        }

        try {
            File file = new File("D://temp/queryresults.csv");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            br.write(sb.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ForwardResolution(resolutionPageString);
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
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
}
