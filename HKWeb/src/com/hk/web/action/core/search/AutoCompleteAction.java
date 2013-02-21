package com.hk.web.action.core.search;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@UrlBinding("/autocomplete-search")
public class AutoCompleteAction extends BaseAction {

    @Value("#{hkEnvProps['" + Keys.Env.solrUrl + "']}")
    String                solrUrl;

    @Autowired
    CommonsHttpSolrServer server;

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(SearchAction.class);
    String                q      = "";
    String                limit  = "10";

    private static enum RegexType {
      NONE,
      LEFT,
      RIGHT,
      BOTH
    }

    @DontValidate
    public Resolution pre() throws Exception {
        List<String> terms = query(q, Integer.parseInt(limit), RegexType.NONE, new ArrayList<String>());
        Set<String> suggestedStrings = new HashSet<String>();
        return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Done", terms));
    }

    private List<String> query(String q, int limit, RegexType regexType, List<String> suggestedStrings) throws MalformedURLException {
        List<TermsResponse.Term> items = null;
        q = q.trim();
        q = q.replaceAll(",","");
        // escape special characters
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addTermsField("title_autocomplete");
        query.setTerms(true);
        query.setTermsLimit(limit);
        // query.setTermsLower(q);
        query.setHighlight(true);
        if (regexType.equals(RegexType.NONE))
          query.setTermsPrefix(q.toLowerCase());
        else if (regexType.equals(RegexType.RIGHT))
          query.setTermsRegex(q.toLowerCase() + ".*");
        else if (regexType.equals(RegexType.BOTH))
          query.setTermsRegex(".*" + q.toLowerCase() + ".*");

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
              String completeTerm = item.getTerm();
              suggestedStrings.add(completeTerm);
            }
            if (suggestedStrings.size() < limit && regexType.equals(RegexType.NONE)) {
              suggestedStrings = query(q, limit-suggestedStrings.size(), RegexType.RIGHT, suggestedStrings);
            }
            if (suggestedStrings.size() < limit && regexType.equals(RegexType.RIGHT)) {
              suggestedStrings = query(q, limit-suggestedStrings.size(), RegexType.BOTH, suggestedStrings);
            }
        }
        return suggestedStrings;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
