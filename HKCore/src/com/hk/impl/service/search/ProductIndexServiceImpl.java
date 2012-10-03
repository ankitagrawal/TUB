package com.hk.impl.service.search;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.search.SolrProduct;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.search.ProductIndexService;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/3/12
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProductIndexServiceImpl implements ProductIndexService {

    private static Logger logger = LoggerFactory.getLogger(ProductIndexServiceImpl.class);
    @Autowired
    CommonsHttpSolrServer solr;

    @Autowired
    ProductService productService;

    public void indexProduct(Product product){
        try{
            SolrProduct solrProduct = productService.createSolrProduct(product);
            updateExtraProperties(product, solrProduct);
            indexProduct(solrProduct);
        } catch (Exception ex) {
            logger.error(String.format("Unable to build Solr index for Product %s", product.getId()), ex);
        }
    }

    private void updateExtraProperties(Product pr, SolrProduct solrProduct){
        for (ProductVariant pv : pr.getProductVariants()){
            if (pv.getProductOptions() != null){
                for (ProductOption po : pv.getProductOptions()){
                    if (po.getValue() != null){
                        solrProduct.getVariantNames().add(pr.getName() + " " + po.getValue());
                    }
                }
            }
        }
    }

    private void indexProduct(SolrProduct product){
        try{
            solr.addBean(product);
        }catch(SolrServerException ex){
            logger.error("Solr error during indexing the product", ex);
        }catch(IOException ex){
            logger.error("Solr error during indexing the product", ex);
        }
    }
}
