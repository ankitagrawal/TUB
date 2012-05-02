package com.hk.web.action.core.search;



import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Value;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.web.HealthkartResponse;

@UrlBinding("/autocomplete-search")
public class AutoCompleteAction extends BaseAction {
  // @Named(Keys.Env.solrUrl)
    @Value("#{hkEnvProps['solrUrl']}")
  String solrUrl;

  private static Logger logger = LoggerFactory.getLogger(SearchAction.class);
  String q = "";
  String limit = "10";

  @DontValidate
  public Resolution pre() throws Exception {
    List<String> terms = query(q, Integer.parseInt(limit));
    return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Done", terms));
  }

  private List<String> query(String q, int limit) throws MalformedURLException {

    List<String> suggestedStrings = new ArrayList<String>();
    List<TermsResponse.Term> items = null;
    CommonsHttpSolrServer server = null;
    try {
      server = new CommonsHttpSolrServer(solrUrl);
    } catch (Exception e) {e.printStackTrace(); }

    // escape special characters
    SolrQuery query = new SolrQuery();
    query.setQuery("*:*");
    query.addTermsField("title_autocomplete");
    query.setTerms(true);
    query.setTermsLimit(limit);
//    query.setTermsLower(q);
    query.setHighlight(true);
//    query.setTermsPrefix(q);
    query.setTermsRegex(".*"+q+".*");
//    query.setTermsRegexFlag("dotall");
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
