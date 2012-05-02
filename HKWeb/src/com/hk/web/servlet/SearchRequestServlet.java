package com.hk.web.servlet;

import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Sep 12, 2011
 * Time: 3:02:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchRequestServlet extends javax.servlet.http.HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String q = req.getParameter("q");
    String limit = req.getParameter("limit");
    PrintWriter writer = res.getWriter();
    List<TermsResponse.Term> terms = query(q, Integer.parseInt(limit));

    if (terms != null) {
      for (TermsResponse.Term t : terms) {
        writer.println(t.getTerm());
      }
    }
  }

  private List<TermsResponse.Term> query(String q, int limit) {
    List<TermsResponse.Term> items = null;
    CommonsHttpSolrServer server = null;

    try {
      server = new CommonsHttpSolrServer("http://localhost:8983/solr");
    } catch (Exception e) { e.printStackTrace(); }

    // escape special characters
    SolrQuery query = new SolrQuery();
    query.addTermsField("spell");
    query.setTerms(true);
    query.setTermsLimit(limit);
    query.setTermsLower(q);
    query.setTermsPrefix(q);
    query.setQueryType("/terms");
    try {
      QueryResponse qr = server.query(query);
      TermsResponse resp = qr.getTermsResponse();
      items = resp.getTerms("name");
    } catch (SolrServerException e) {
      items = null;
    }
    return items;
  }
}
