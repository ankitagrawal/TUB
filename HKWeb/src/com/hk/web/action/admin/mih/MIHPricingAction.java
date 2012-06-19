package com.hk.web.action.admin.mih;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 11:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MIHPricingAction extends BaseAction {


    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                      adminUploadsPath;
    File                               xlsFile;

    FileBean fileBean;

    private static Logger logger                    = LoggerFactory.getLogger(MIHPricingAction.class);

    @DefaultHandler
    public Resolution pre() {

        return new ForwardResolution("/pages/admin/mih/MIHPricing.jsp");
    }

    public Resolution uploadMIHPricingExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/mihFiles/mihPricing.xls";
       // String excelFilePath ="E:\\mih\\mihpricing.xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        if(excelFile.exists()){
          excelFile.delete();   
        }
        fileBean.save(excelFile);

        addRedirectAlertMessage(new SimpleMessage("File Uploaded"));
        return new ForwardResolution("/pages/admin/mih/MIHPricing.jsp");
    }

    public Resolution downloadMIHPricingExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/mihFiles/mihPricing.xls";
        //String excelFilePath ="E:\\mih\\mihpricing.xls";
        xlsFile = new File(excelFilePath);
        if(!xlsFile.exists()){
          addRedirectAlertMessage(new SimpleMessage("File doesnot exist yet"));
          return new ForwardResolution("/pages/admin/mih/MIHPricing.jsp");
        }else{

         return new HTTPResponseResolution();
        }
    }

    /**
     * Custom resolution for HTTP response. The resolution will write the output file in response
     */

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            OutputStream out = null;
            InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
            res.setContentLength((int) xlsFile.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
            out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
        }
    }


    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }
}