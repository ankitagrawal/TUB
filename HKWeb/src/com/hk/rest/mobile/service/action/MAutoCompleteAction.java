package com.hk.rest.mobile.service.action;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.DefaultHandler;

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

import com.hk.constants.core.Keys;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.search.SearchAction;

@Path("/mAutocomplete")
@Component
public class MAutoCompleteAction extends MBaseAction {

    @Value("#{hkEnvProps['" + Keys.Env.solrUrl + "']}")
    String                solrUrl;

    @Autowired
    CommonsHttpSolrServer server;

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(SearchAction.class);
    String                limit  = "10";

    @DefaultHandler
    @GET
    @Path("/autosearch/")
    @Produces("application/json")
    public String autoSearch(@Context HttpServletResponse response,@QueryParam("q") String q) throws Exception {
        List<String> terms = query(q, Integer.parseInt(limit));
        return com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(new HealthkartResponse(MHKConstants.STATUS_OK, MHKConstants.STATUS_DONE, terms));
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}