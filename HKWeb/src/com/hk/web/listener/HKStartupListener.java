package com.hk.web.listener;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akube.framework.util.BaseUtils;
import com.hk.cache.CategoryCache;
import com.hk.cache.HkApiUserCache;
import com.hk.cache.RoleCache;
import com.hk.cache.vo.CategoryVO;
import com.hk.domain.api.HkApiUser;
import com.hk.domain.catalog.category.Category;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.LoadPropertyService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.web.AppConstants;

public class HKStartupListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(HKStartupListener.class);

    public static File    hibernateCfgFile;
    public static File    hibernateReadCfgFile;
    public static File    ehcacheCfgFile;
    public static File    freeMarkerDir;
    public static File    environmentPropertiesFile;
    public static File    antiSamyPolicyFile;
    public static String  contextPath;
    public static String  appBasePath;
    public static String  environmentDir;

    private BaseDao       baseDao;

    // private BatchProcessManager batchProcessManager;

    // Public constructor is required by servlet spec
    public HKStartupListener() {
    }

    public void contextInitialized(ServletContextEvent event) {

        AppConstants.contextPath = event.getServletContext().getContextPath();
        AppConstants.appBasePath = event.getServletContext().getRealPath("/");

        LoadPropertyService loadPropertyService = ServiceLocatorFactory.getService(LoadPropertyService.class);

        AppConstants.isHybridRelease = loadPropertyService.isHybridRelease();
        AppConstants.syncEdge = loadPropertyService.isSyncEdge();

        /*
         * PropertyConfigurator.configure( AppConstants.appBasePath + "WEB-INF/log4j.properties"); logger.info("logger
         * configured");
         */

        /*
         * This method is called when the servlet context is initialized(when the Web application is deployed). You can
         * initialize servlet context related data here.
         */

        /*
         * TimeZone.setDefault(TimeZone.getTimeZone("IST")); Locale.setDefault(Locale.ENGLISH); contextPath =
         * event.getServletContext().getContextPath(); // get usepath appBasePath =
         * event.getServletContext().getRealPath("/"); environmentDir = getEnvironmentDir(appBasePath);
         * setupLogger(environmentDir); // Environment specific properties environmentPropertiesFile = new
         * File(environmentDir + "/environment.properties"); if (!environmentPropertiesFile.exists()) { String errorMsg =
         * "environment.properties file not found in " + environmentDir; System.err.println(errorMsg); throw new
         * RuntimeException(errorMsg); } // setup hibernate hibernateCfgFile = new File(environmentDir +
         * "/hibernate.cfg.xml"); if (!hibernateCfgFile.exists()) { String errorMsg = "hibernate.cfg.xml file not found
         * in " + environmentDir; System.err.println(errorMsg); throw new RuntimeException(errorMsg); } // setup
         * hibernate read slave config hibernateReadCfgFile = new File(environmentDir + "/hibernateRead.cfg.xml"); if
         * (!hibernateReadCfgFile.exists()) { String errorMsg = "hibernateRead.cfg.xml file not found in " +
         * environmentDir; System.err.println(errorMsg); throw new RuntimeException(errorMsg); } // setup ehcache
         * ehcacheCfgFile = new File(appBasePath + "/ehcache.xml"); if (!ehcacheCfgFile.exists()) { String errorMsg =
         * "ehcache.xml file not found in " + appBasePath + "/WEB-INF/properties"; System.err.println(errorMsg); throw
         * new RuntimeException(errorMsg); } else { System.out.println("ehcache.xml file found"); } // setup freemarker
         * templates directory String freemarkerDirPath = appBasePath + "/freemarker"; freeMarkerDir = new
         * File(freemarkerDirPath); // setup antisamy antiSamyPolicyFile = new File(environmentDir +
         * "/antisamy-hk-1.3.xml");
         */

        // InjectorFactory.getInjector();
        // Boolean startBackgroundTaskManager = ServiceLocatorFactory.getService(Key.get(Boolean.class,
        // Names.named(Keys.Env.startBackgroundTaskManager)));
        // if (startBackgroundTaskManager) {
        if (true) {
            System.out.println("------------- Starting Batch Process Manager ---------------------");

            logger.info("START POPULATING HK API USER CACHE");
            System.out.println("START POPULATING HK API USER CACHE");
            populateHKApiUserCache();
            logger.info("END POPULATING HK API USER CACHE");
            System.out.println("END POPULATING HK API USER CACHE");

            logger.info("START POPULATING ROLE CACHE");
            System.out.println("START POPULATING ROLE CACHE");
            // populateRoleCache();
            RoleCache.getInstance().populateRoleCache();
            logger.info("END POPULATING ROLE CACHE");
            System.out.println("END POPULATING ROLE CACHE");

            /*
             * logger.info("START POPULATING USER CACHE"); System.out.println("START POPULATING USER CACHE");
             * populateUserCache(); logger.info("END POPULATING USER CACHE"); System.out.println("END POPULATING USER
             * CACHE");
             */

            logger.info("START POPULATING CATEGORY  CACHE");
            System.out.println("START POPULATING CATEGORY  CACHE");
            populateCategoryCache();
            logger.info("END POPULATING CATEGORY CACHE");
            System.out.println("END POPULATING CATEGORY CACHE");

            /*
             * batchProcessManager = ServiceLocatorFactory.getService(BatchProcessManager.class);
             * batchProcessManager.start();
             */
        } else {
            System.out.println("------------- WARNING! Batch Process Manager is disabled! ---------------------");
        }

    }

    private void populateHKApiUserCache() {
        HkApiUserCache.getInstance().reset();
        HkApiUserCache hkApiUserCache = HkApiUserCache.getInstance().getTransientCache();

        List<HkApiUser> apiUsersList = getBaseDao().getAll(HkApiUser.class);

        for (HkApiUser apiUser : apiUsersList) {
            hkApiUserCache.addHkApiUser(apiUser);
        }
        hkApiUserCache.freeze();

    }

    /*
     * private void populateRoleCache() { RoleCache.getInstance().reset(); RoleCache roleCache =
     * RoleCache.getInstance().getTransientCache(); List<Role> allRoles = getBaseDao().getAll(Role.class); for (Role
     * role : allRoles) { roleCache.addRole(new RoleVO(role)); } roleCache.freeze(); }
     */

    private void populateCategoryCache() {
        CategoryCache.getInstance().reset();
        CategoryCache categoryCache = CategoryCache.getInstance().getTransientCache();

        List<Category> allCategories = getBaseDao().getAll(Category.class);

        for (Category category : allCategories) {
            categoryCache.addCategory(new CategoryVO(category));
        }
        categoryCache.freeze();

    }

    @SuppressWarnings("unused")
    private String getEnvironmentDir(String appBasePath) {
        String propertyLocatorFileLocation = "/WEB-INF/propertyLocator.properties";
        propertyLocatorFileLocation = appBasePath + propertyLocatorFileLocation;
        File propertyLocatorFile = new File(propertyLocatorFileLocation);
        if (!propertyLocatorFile.exists()) {
            String errorMsg = "propertyLocator.properties file not found in path : " + propertyLocatorFileLocation + "\n" + "A valid propertyLocator.properties could be : \n"
                    + "usePath=WEB-INF/properties/kani";
            System.err.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);

        // next load up the properties
        if (StringUtils.isBlank(properties.getProperty("usePath"))) {
            String errorMsg = "propertyLocator.properties (" + propertyLocatorFileLocation + ") does not have the property usePath \n"
                    + "A calid propertyLocator.properties could be : \n" + "usePath=WEB-INF/properties/kani";
            System.err.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        String environmentDir = appBasePath + properties.getProperty("usePath");
        System.out.println("Using properties from usePath = " + environmentDir);
        return environmentDir;
    }

    @SuppressWarnings("unused")
    private void setupLogger(String environmentDir) {
        String loggerPropertiesFileLocation = environmentDir + "/logger-config.properties";
        File log4jFile = new File(loggerPropertiesFileLocation);

        if (!log4jFile.exists()) {
            System.err.println("ERROR: Cannot read the configuration file at " + log4jFile.getAbsolutePath() + ". Please check the path of the config init param in web.xml");
            throw new RuntimeException("ERROR: Cannot read the configuration file. Please check the path of the config init param in web.xml");
        }

        // look up another init parameter that tells whether to watch this
        // configuration file for changes.
        String watch = "false";

        // since we have not yet set up our log4j environment,
        // we will use System.err for some basic information
        System.err.println("Using properties file: " + loggerPropertiesFileLocation);
        System.err.println("Watch is set to: " + watch);

        // use the props file to load up configuration parameters for log4j
        if (watch.equalsIgnoreCase("true"))
            PropertyConfigurator.configureAndWatch(loggerPropertiesFileLocation);
        else
            PropertyConfigurator.configure(loggerPropertiesFileLocation);

        System.out.println("log4j init success");
        // once configured, we can start using the Logger now

        org.slf4j.Logger logger = LoggerFactory.getLogger(HKStartupListener.class);
        logger.debug("Log4j + slf4j Initialised Successfully ... ");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        /*
         * This method is invoked when the Servlet Context (the Web application) is undeployed or Application Server
         * shuts down.
         */
        System.out.println("------------- Shutting Down CacheManager ---------------------");
        // CacheManager.getInstance().shutdown();
        System.out.println("================  SHUTTING DOWN  ==================");
        // LogManager.shutdown();
    }

    public BaseDao getBaseDao() {
        if (baseDao == null) {
            baseDao = ServiceLocatorFactory.getService(BaseDao.class);
        }
        return baseDao;
    }

}
