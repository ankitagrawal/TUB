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
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

import java.io.File;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelatedProductXlsParser {

    private static Logger logger = LoggerFactory.getLogger(RelatedProductXlsParser.class);

    @Autowired
    private ProductService productService;


    public Set<Product> readRelatedProductExcel(File file) throws Exception {
        logger.debug("parsing RelatedProduct info : " + file.getAbsolutePath());
        Set<Product> productSet = new HashSet<Product>();
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
                Product product = getProductService().getProductById(productId);

                String[] relatedProductStrArray = StringUtils.split(relatedProductStr, "|");
                for (String relatedProductId : relatedProductStrArray) {
                    Product relatedProduct = getProductService().getProductById(relatedProductId);
                    if (relatedProduct != null && !relatedProduct.equals(product) && !relatedProduct.isDeleted() && !relatedProducts.contains(relatedProduct)) {
                        relatedProducts.add(relatedProduct);
                    }
                }
                product.setRelatedProducts(relatedProducts);
                getProductService().save(product);
                productSet.add(product);
                relatedProducts.add(product);

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

