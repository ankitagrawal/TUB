package com.hk.rest.mobile.service.action;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.web.action.core.search.SearchAction;
import com.hk.web.HealthkartResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 13, 2012
 * Time: 6:47:26 PM
 * To change this template use File | Settings | File Templates.
 */

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.action.*;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.web.HealthkartResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletResponse;

@Path("/mAutocomplete")
@Component
public class MAutoCompleteAction extends MBaseAction {

    @Value("#{hkEnvProps['" + Keys.Env.solrUrl + "']}")
    String                solrUrl;

    @Autowired
    CommonsHttpSolrServer server;

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(SearchAction.class);
//    String                q      = "";
    String                limit  = "10";

    @DefaultHandler
    @GET
    @Path("/autosearch/")
    @Produces("application/json")
    public String autoSearch(@Context HttpServletResponse response,@QueryParam("q") String q) throws Exception {
        List<String> terms = query(q, Integer.parseInt(limit));
        return com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Done", terms));
    }

    private List<String> query(String q, int limit) throws MalformedURLException {

        List<String> suggestedStrings = new ArrayList<String>();
        List<TermsResponse.Term> items = null;

        // escape special characters
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addTermsField("title_autocomplete");
        query.setTerms(true);
        query.setTermsLimit(limit);
        // query.setTermsLower(q);
        query.setHighlight(true);
        // query.setTermsPrefix(q);
        query.setTermsRegex(".*" + q + ".*");
        // query.setTermsRegexFlag("dotall");
        query.setQueryType("/terms");
        try {
            QueryResponse qr = server.query(query);
            TermsResponse resp = qr.getTermsResponse();
            items = resp.getTerms("title_autocomplete");
        } catch (SolrServerException e) {
            items = null;
        }

        if (items != null) {
            for (TermsResponse.Term item : items) {
                suggestedStrings.add(item.getTerm());
            }
        }
        return suggestedStrings;
    }

  /*  public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
*/
    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
