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
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class RelatedProductXlsParser {

    private static Logger logger = LoggerFactory.getLogger(RelatedProductXlsParser.class);

        public Set<Product> readProductDispatchDateExcel(File file) throws Exception {
            logger.debug("parsing DispatchDate info : " + file.getAbsolutePath());
            Set<Product> productSet = new HashSet<Product>();
            ExcelSheetParser excel = new ExcelSheetParser(file.getAbsolutePath(), "Sheet1", true);
            Iterator<HKRow> rowiterator = excel.parse();
            int rowCount = 1;
            try {

}
