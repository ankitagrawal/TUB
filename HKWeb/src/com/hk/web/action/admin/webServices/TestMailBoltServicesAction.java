package com.hk.web.action.admin.webServices;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserSearchService;
import com.hk.pact.service.catalog.CategoryService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
    @Autowired
    CategoryService categoryService;

    // variables passed as params to services
    private String zones;
    private String pvs;
    private String params;
    private String minimum = "true";
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


    /*@Value("#{hkEnvProps['" + Keys.Env.mailBolt + "']}")
    String mailBoltDownloadsPath;*/


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
            if (categories != null) {
                Set<Category> cats = categoryService.getCategoriesFromCategoryNames(categories);
                List<Product> prods = new ArrayList<Product>();
                for (Category c : cats) {
                    List<Product> p = c.getProducts();
                    prods.addAll(p);
                }
                List<String> prodIds = new ArrayList<String>();
                for (Product pr : prods) {
                    prodIds.add(pr.getId());
                }
                ids.addAll(prodIds);
            }
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


        List<Object[]> ems = null;
        List<User> users = null;
        try {
            if (minimum != null && "true".equals(minimum)) {
                ems = userSearchService.searchUserInfo(criteria);
            } else {
                users = userSearchService.searchUsers(criteria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (minimum != null && "true".equals(minimum)) {
            result = ems.size();
        } else {
            result = users.size();
        }
        Long endTime = System.currentTimeMillis();
        time = endTime - startTime;

        // write output to file
        StringBuffer summarySB = new StringBuffer();
        String summary = "result size: " + result + " time taken in millis: " + (endTime - startTime);
        summarySB.append("Summary").append("\n").append(summary).append("\n");

        if (production != null && "true".equals(production)) {
            UsersSearchCriteria.NOT_ON_PRODUCTION = false;
        } else {
            UsersSearchCriteria.NOT_ON_PRODUCTION = true;
        }

        StringBuffer resultSB = new StringBuffer();
        resultSB.append("\n").append(UsersSearchCriteria.NOT_ON_PRODUCTION ?
                "login, email, name, subscriptionMask, unsubscribeToken" :
                "email, login, name, subscriptionMask, unsubscribeToken");

        if (minimum != null && "true".equals(minimum)) {
            for (Object[] o : ems) {
                resultSB.append("\n");
                for (int i = 0; i < o.length; i++) {
                    Object ob = o[i];
                    resultSB.append(ob);
                    if (i < o.length - 1) {
                        resultSB.append(",");
                    }
                }
            }
        } else {
            //TODO : implemnt this
        }

        return new StreamingResolution("text/csv", summarySB.append("\n\n").toString() + resultSB.toString()).setFilename("queryResult.csv");
    }

    private File uniqueFile(String initialName) {
        String finalName = initialName;
        File file = new File(finalName);
        int index = 1;
        while (file.exists()) {
            finalName = initialName + "_" + index++;
            file = new File(finalName);
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }
}
