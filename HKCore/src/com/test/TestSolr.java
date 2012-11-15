package com.test;


/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/19/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.*/


public class TestSolr 
//extends AbstractSolrTestCase 
{

    /*private SolrServer server;
    @Override
    public String getSchemaFile() {
        return "solr/conf/schema.xml";
    }

    @Override
    public String getSolrConfigFile() {
        return "solr/conf/solrconfig.xml";
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        server = new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
    }

    @Test
    public void testThatNoResultsAreReturned() throws SolrServerException {
        SolrParams params = new SolrQuery("text that is not found");
        QueryResponse response = server.query(params);
        assertEquals(0L, response.getResults().getNumFound());
    }

    @Test
    public void testThatDocumentIsFound() throws SolrServerException, IOException {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "1");
        document.addField("name", "my name");

        server.add(document);
        server.commit();

        SolrParams params = new SolrQuery("name");
        QueryResponse response = server.query(params);
        assertEquals(1L, response.getResults().getNumFound());
        assertEquals("1", response.getResults().get(0).get("id"));
    }*/

}
