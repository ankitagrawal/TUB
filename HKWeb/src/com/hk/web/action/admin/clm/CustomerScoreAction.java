package com.hk.web.action.admin.clm;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.domain.clm.KarmaProfile;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

/**
 * Created by IntelliJ IDEA. User: Pradeep Date: May 29, 2012 Time: 3:48:27 PM To change this template use File |
 * Settings | File Templates.
 */
@Component
public class CustomerScoreAction extends BaseAction {

    private static final String SHEET_NAME_CUSTOMER_SCORE = "CustomerScore";

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                      adminUploadsPath;

    @Autowired
    private KarmaProfileService karmaProfileService;

    FileBean                    fileBean;

    private static Logger       logger                    = LoggerFactory.getLogger(CustomerScoreAction.class);

    @DefaultHandler
    public Resolution pre() {

        return new ForwardResolution("/pages/admin/clm/CustomerScoreUpload.jsp");
    }

    public Resolution uploadScoreExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/clmExcelFiles/customerScore" + System.currentTimeMillis() + ".xls";
        // String excelFilePath ="E:\\test\\customerscore" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        uploadInDB(excelFilePath);

        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/clm/CustomerScoreUpload.jsp");
    }

    public void uploadInDB(String excelFilePath) {
        try {
            ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, SHEET_NAME_CUSTOMER_SCORE);
            Iterator<HKRow> rowIterator = parser.parse();
            Set<KarmaProfile> karmaProfileSet = new HashSet<KarmaProfile>();

            while (null != rowIterator && rowIterator.hasNext()) {
                HKRow curHkRow = rowIterator.next();
                KarmaProfile karmaProfile;

                int i = 0;
                while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
                    karmaProfile = getKarmaProfileService().findByUser(getUserService().getUserById(new Long(curHkRow.getColumnValue(i))));
                    if (karmaProfile == null) {
                        karmaProfile = new KarmaProfile();
                        karmaProfile.setUser(getUserService().getUserById(new Long(curHkRow.getColumnValue(i))));
                    }
                    i++;
                    karmaProfile.setKarmaPoints(Integer.parseInt(curHkRow.getColumnValue(i)));
                    i++;
                    karmaProfileSet.add(karmaProfile);
                }
            }

            for (KarmaProfile karmaProfile : karmaProfileSet) {
                if (karmaProfile != null) {
                    getKarmaProfileService().save(karmaProfile);
                }
                logger.info("inserting or updating into userid:" + karmaProfile.getUser().getId() + " score: " + karmaProfile.getKarmaPoints());
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
        }

    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public KarmaProfileService getKarmaProfileService() {
        return karmaProfileService;
    }

    public void setKarmaProfileService(KarmaProfileService karmaProfileService) {
        this.karmaProfileService = karmaProfileService;
    }
}
