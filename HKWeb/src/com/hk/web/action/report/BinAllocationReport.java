package com.hk.web.action.report;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.BinManager;
import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.constants.core.Keys;
import com.hk.constants.report.ReportConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.Bin;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.io.HkXlsWriter;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 11, 2012
 * Time: 8:41:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinAllocationReport extends BaseAction {


  @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
  String adminDownloads;
  private File xlsFile;
  @Autowired
  SkuService skuService;
  @Autowired
  UserService userService;
  @Autowired
  SkuItemDao skuItemDao;
  @Autowired
  BinManager binManager;
  @Autowired
  BinDao binDao;


  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/reports/binAllocationReport.jsp");
  }


  public Resolution generateBinAllocationExcel() {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      xlsFile = new File(adminDownloads + "/reports/bin-report" + sdf.format(new Date()) + ".xls");
      xlsFile = generateBinAllocationXsl(xlsFile.getPath());
      if (xlsFile == null) {
        addRedirectAlertMessage(new SimpleMessage("Please Login in Warehouse"));
        return new ForwardResolution("/pages/admin/reports/binAllocationReport.jsp");
      }
      addRedirectAlertMessage(new SimpleMessage("Bin Report  successfully generated."));
    } catch (Exception e) {
      e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
      addRedirectAlertMessage(new SimpleMessage("Bin Report generation failed"));
    }
    return new HTTPResponseResolution();
  }


  public class HTTPResponseResolution implements Resolution {
    public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
      OutputStream out = null;
      InputStream in = null;
       try {
       in =  new BufferedInputStream(new FileInputStream(xlsFile));
        res.setContentLength((int) xlsFile.length());
        res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
        out = res.getOutputStream();
        //Copy the contents of the file to the output stream
        byte[] buf = new byte[4096];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
          out.write(buf, 0, count);
        }
      }
      finally {
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.close();
        }
      }
    }

  }

  public File generateBinAllocationXsl(String xslFilePath) throws Exception {

    HkXlsWriter xlsWriter = new HkXlsWriter();

    xlsWriter.addHeader(ReportConstants.PRODUCT_VARIANT_ID, ReportConstants.PRODUCT_VARIANT_ID);
    xlsWriter.addHeader(ReportConstants.PRODUCT_NAME, ReportConstants.PRODUCT_NAME);
    xlsWriter.addHeader(ReportConstants.HK_Barcode, ReportConstants.HK_Barcode);
    xlsWriter.addHeader(ReportConstants.MRP, ReportConstants.MRP);
    xlsWriter.addHeader(ReportConstants.LOCATION, ReportConstants.LOCATION);

    int rowCounter = 0;
    Warehouse warehouse = userService.getWarehouseForLoggedInUser();
    if (warehouse == null) {
      return null;
    }

   List<Bin> allBin = binManager.getAllBinByWarehouse(warehouse);
    List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
    Map<SkuGroup, Bin> skuGroupBinMap = new HashMap<SkuGroup, Bin>();
     for(Bin bin: allBin){
      List<SkuItem> skuItemList= bin.getSkuItems();
       if(skuItemList != null && skuItemList.size() >0){
         for(SkuItem skuitem:skuItemList){
            SkuGroup skugroup=skuitem.getSkuGroup();
           if(!(skuGroupList.contains(skugroup))){
              skuGroupList.add(skugroup);
             skuGroupBinMap.put(skugroup,bin);
           }
         }

       }

     }

    Set<Map.Entry<SkuGroup, Bin>> skuGroupSet = skuGroupBinMap.entrySet();
    for (Map.Entry<SkuGroup, Bin> entry : skuGroupSet) {
      ProductVariant productVariant = entry.getKey().getSku().getProductVariant();
      String productVariantId = productVariant.getId();
      String productVariantName = productVariant.getProduct().getName();
      String mrp = Double.toString(productVariant.getMarkedPrice());
      String Location = entry.getValue().getBarcode();

      rowCounter++;
      xlsWriter.addCell(rowCounter, productVariantId);
      xlsWriter.addCell(rowCounter, productVariantName);
      xlsWriter.addCell(rowCounter,entry.getKey().getBarcode() );
      xlsWriter.addCell(rowCounter, mrp);
      xlsWriter.addCell(rowCounter, Location);
    }
    return xlsWriter.writeData(xslFilePath);
  }

}