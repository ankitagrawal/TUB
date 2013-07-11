package com.hk.admin.util;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 5/30/13
 * Time: 11:37 PM
 * To change this template use File | Settings | File Templates.
 */

import com.hk.constants.XslConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.exception.DuplicateEntryException;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.exception.ProductDeletedException;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class RelatedProductXlsParser {

    private static Logger logger = LoggerFactory.getLogger(RelatedProductXlsParser.class);

    @Autowired
    private ProductService productService;


    public Set<Product> readRelatedProductExcel(File file) throws Exception {
        logger.debug("parsing RelatedProduct info : " + file.getAbsolutePath());
        ExcelSheetParser excel = new ExcelSheetParser(file.getAbsolutePath(), "Sheet1", true);
        Iterator<HKRow> rowIterator = excel.parse();
        int rowCount = 1;
        try {
            while (rowIterator.hasNext()) {
                rowCount++;
                HKRow row = rowIterator.next();
                String productId = row.getColumnValue(XslConstants.PRODUCT_ID);
                String relatedProductStr = row.getColumnValue(XslConstants.RELATED_PRODUCTS);
                List<Product> relatedProducts = new ArrayList<Product>();

                if (StringUtils.isEmpty(productId)) {
                    logger.error("product id cannot be null/empty");
                    throw new ExcelBlankFieldException("product id  cannot be empty" + "    ", rowCount);
                }
                if (StringUtils.isEmpty(relatedProductStr)) {
                    logger.error("Related Product cannot be null/empty");
                    throw new ExcelBlankFieldException("Related Product  cannot be empty" + "    ", rowCount);
                }

                Product product = getProductService().getProductById(productId.trim());
                if (product==null){
                       throw new ExcelBlankFieldException(product+ "product is invalid");
                } else{
                List<Product> productHasRelated = product.getRelatedProducts();
                String[] relatedProductStrArray = StringUtils.split(relatedProductStr, "|");
                for (String relatedProductId : relatedProductStrArray) {
                    if(relatedProductId==null){
                        throw new  ExcelBlankFieldException("related product is empty");
                    }else{
                    Product relatedProduct = getProductService().getProductById(relatedProductId);
                    if (relatedProduct != null && !relatedProduct.equals(product) && !relatedProduct.isDeleted() && !productHasRelated.contains(relatedProduct)) {
                        relatedProducts.add(relatedProduct);
                    }
                    else if (relatedProduct==null){
                        throw new ExcelBlankFieldException("related product is invalid"+" "+relatedProduct);
                    }
                    else if(relatedProduct.equals(product)){
                        throw new DuplicateEntryException("related product"+ " "+relatedProduct+" "+"same as product"+product);
                    }
                    else if(relatedProduct.isDeleted()){
                        throw  new ProductDeletedException("product is deleted",relatedProduct);
                    }
                    if (productHasRelated.contains(relatedProduct)) {
                        logger.error(relatedProduct + "Already Added");
                    }

                }
                if (relatedProducts.isEmpty()) {
                    logger.error("All Product are already added");
                    throw new Exception("All related product are already added for product_id: " + product);
                } else if (relatedProducts.size() == 1) {
                    productHasRelated.add(relatedProducts.get(0));
                } else {
                    productHasRelated.addAll(relatedProducts);
                }
                product.setRelatedProducts(productHasRelated);
                getProductService().save(product);
                }
                }
            }
        } catch (ExcelBlankFieldException e) {
            throw new ExcelBlankFieldException(e.getMessage());
        }

        return null;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

}

