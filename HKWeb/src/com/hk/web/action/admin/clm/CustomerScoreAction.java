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
import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.clm.CategoryKarmaProfile;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartDefaultWebException;
import com.hk.pact.service.catalog.CategoryService;
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
    @Autowired
    private CategoryService     categoryService;

    FileBean                    fileBean;
    FileBean                    categoryFileBean;

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

        uploadTotalScoreInDB(excelFilePath);

        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/clm/CustomerScoreUpload.jsp");
    }

    public Resolution uploadCategoryScoreExcel() throws Exception {
        String excelFilePath = adminUploadsPath + "/clmExcelFiles/customerCategoryScore" + System.currentTimeMillis() + ".xls";
        // String excelFilePath ="E:\\test\\customerscorecat" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        categoryFileBean.save(excelFile);

        uploadCategoryScoreInDB(excelFilePath);

        excelFile.delete();
        addRedirectAlertMessage(new SimpleMessage("Database Updated"));
        return new ForwardResolution("/pages/admin/clm/CustomerScoreUpload.jsp");
    }

    private void uploadCategoryScoreInDB(String excelFilePath) {
        try {
            ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, SHEET_NAME_CUSTOMER_SCORE);
            Iterator<HKRow> rowIterator = parser.parse();
            Set<CategoryKarmaProfile> categoryKarmaProfileSet = new HashSet<CategoryKarmaProfile>();

            while (null != rowIterator && rowIterator.hasNext()) {
                HKRow curHkRow = rowIterator.next();
                CategoryKarmaProfile categoryKarmaProfile;
                User user;
                Category category;

                int i = 0;

                // iterating till the last column
                while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
                    // user = getUserService().getUserById(new Long(curHkRow.getColumnValue(i)));
                    user = getUserService().getUserById(new Long(curHkRow.getColumnValue(i)));
                    i++;
                    // iteratiing for 9 categories
                    while (i < 10) {
                        switch (i) {
                            case 1:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.BABY);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.BABY).getCategory();
                                break;
                            case 2:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.BEAUTY);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.BEAUTY).getCategory();
                                break;
                            case 3:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.DIABETES);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.DIABETES).getCategory();
                                break;
                            case 4:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.EYE);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.EYE).getCategory();
                                break;
                            case 5:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.HEALTH_DEVICES);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.HEALTH_DEVICES).getCategory();
                                break;
                            case 6:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.NUTRITION);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.NUTRITION).getCategory();
                                break;
                            case 7:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.PERSONAL_CARE);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.PERSONAL_CARE).getCategory();
                                break;
                            case 8:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.SERVICES);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.SERVICES).getCategory();
                                break;
                            case 9:
                                // category = getCategoryService().getCategoryByName(CategoryConstants.SPORTS);
                                category = CategoryCache.getInstance().getCategoryByName(CategoryConstants.SPORTS).getCategory();
                                break;
                            default:
                                throw new HealthkartDefaultWebException("Invalid category");

                        }
                        categoryKarmaProfile = getKarmaProfileService().findByUserAndCategory(user, category);
                        if (categoryKarmaProfile == null) {
                            categoryKarmaProfile = new CategoryKarmaProfile();
                            categoryKarmaProfile.setUser(user);
                            categoryKarmaProfile.setCategory(category);
                        }
                        categoryKarmaProfile.setKarmaPoints(Double.parseDouble(curHkRow.getColumnValue(i)));
                        categoryKarmaProfileSet.add(categoryKarmaProfile);
                        i++;
                    }
                }
            }

            for (CategoryKarmaProfile categoryKarmaProfile : categoryKarmaProfileSet) {
                if (categoryKarmaProfile != null) {
                    getKarmaProfileService().save(categoryKarmaProfile);
                }
                logger.info("inserting or updating into userid:" + categoryKarmaProfile.getUser().getId() + " score: " + categoryKarmaProfile.getKarmaPoints() + " for category: "
                        + categoryKarmaProfile.getCategory().getDisplayName());
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed " + e.getMessage()));
        }
    }

    private void uploadTotalScoreInDB(String excelFilePath) {
        try {
            ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, SHEET_NAME_CUSTOMER_SCORE);
            Iterator<HKRow> rowIterator = parser.parse();
            Set<KarmaProfile> karmaProfileSet = new HashSet<KarmaProfile>();

            while (null != rowIterator && rowIterator.hasNext()) {
                HKRow curHkRow = rowIterator.next();
                KarmaProfile karmaProfile;

                int i = 0;
                while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
                    User user = getUserService().getUserById(new Long(curHkRow.getColumnValue(i)));
                    karmaProfile = getKarmaProfileService().findByUser(user);
                    if (karmaProfile == null) {
                        karmaProfile = new KarmaProfile();
                        karmaProfile.setUser(user);
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

    public FileBean getCategoryFileBean() {
        return categoryFileBean;
    }

    public void setCategoryFileBean(FileBean categoryFileBean) {
        this.categoryFileBean = categoryFileBean;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
