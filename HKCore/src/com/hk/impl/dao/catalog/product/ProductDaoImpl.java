package com.hk.impl.dao.catalog.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.domain.catalog.product.combo.Combo;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductImage;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.service.ServiceLocatorFactory;

@SuppressWarnings("unchecked")
@Repository
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {


    public Product getProductById(String productId) {
        return get(Product.class, productId);
    }

    public Product getOriginalProductById(String productId) {
      SessionFactory sessionFactory = (SessionFactory)ServiceLocatorFactory.getService("newSessionFactory") ;
      Session session = sessionFactory.openSession();
      Product product = (Product) session.createQuery(
                "select p from Product p where p.id=:productId").setString(
                "productId", productId).uniqueResult();
      if(product != null)
        product.setCategoriesPipeSeparated(product.getPipeSeparatedCategories());
      return product;
    }

    @Transactional
    public Product save(Product product) {
        if (product.getDeleted() == null) {
            product.setDeleted(Boolean.FALSE);
        }
        if (product.getService() == null) {
            product.setService(Boolean.FALSE);
        }
        if (product.getGoogleAdDisallowed() == null) {
            product.setGoogleAdDisallowed(Boolean.FALSE);
        }
        if (product.getJit() == null)   {
            product.setJit(Boolean.FALSE);
        }
	    if (product.getCodAllowed() == null)   {
            product.setCodAllowed(Boolean.FALSE);
        }
	    if (product.getOutOfStock() == null)   {
            product.setOutOfStock(Boolean.FALSE);
        }
	    if (product.isHidden() == null)   {
            product.setHidden(Boolean.FALSE);
        }
        return (Product) super.save(product);
    }

    public List<Product> getProductByCategory(String category) {
        return getSession().createQuery(
                "select p from Product p left join p.categories c where c.name = :category and p.deleted != :deleted and p.isGoogleAdDisallowed != :isGoogleAdDisallowed order by p.orderRanking asc").setString(
                "category", category).setBoolean("deleted", true).setBoolean("isGoogleAdDisallowed", true).list();
    }

    public List<Product> getProductByCategory(List<String> category) {
        return getSession().createQuery(
                "select p from Product p left join p.categories c where c.name in (:category) and p.deleted != :deleted and p.isGoogleAdDisallowed != :isGoogleAdDisallowed order by p.orderRanking asc").setParameterList(
                "category", category).setBoolean("deleted", true).setBoolean("isGoogleAdDisallowed", true).list();
    }

    public List<Product> getProductByCategories(List<String> categoryNames) {
        return getSession().createQuery("select p from Product p where p.primaryCategory.name in (:category) and deleted = :nonDeleted and isGoogleAdDisallowed != :adAllowed order by p.orderRanking asc")
                .setParameterList("category", categoryNames)
                .setParameter("adAllowed",true)
                .setParameter("nonDeleted", false).list();
    }

    public List<Product> getOOSHiddenDeletedProducts(){
       return  (List<Product>) findByNamedParams(" from Product p where p.hidden =:hidden or p.deleted =:deleted or p.outOfStock =:outOfStock", new String[]{"hidden","deleted","outOfStock"}, new Object[]{true, true, true});
    }

    /**
     * returns list of all the products irrespective of whether they are deleted or not.
     *
     * @param category
     * @return
     */
    public List<Product> getAllProductByCategory(String category) {
        return getSession().createQuery("select p from Product p where p.primaryCategory.name = :category order by p.orderRanking asc").setString("category", category).list();
    }


    /**
     * returns list of all products irrespective of category
     * @param
     * @return
     */
    public List<Product> getAllProductsForCatalog() {
        return getSession().createQuery("select p from Product p order by p.primaryCategory.name asc, p.orderRanking asc ").list();
    }
    public List<Product> getAllSubscribableProductsByCategory(String category){
        return getSession().createQuery("select p from Product p where p.primaryCategory.name = :category and isSubscribable=true order by p.orderRanking asc").setString("category", category).list();
    }

    public List<Product> getAllProductNotByCategory(List<String> categoryNames) {
        return getSession().createQuery("select p from Product p where p.primaryCategory.name not in (:category) and deleted = :nonDeleted and isGoogleAdDisallowed != :adAllowed order by p.orderRanking asc")
                .setParameterList("category", categoryNames)
                .setParameter("adAllowed",true)
                .setParameter("nonDeleted", false).list();
    }

    /**
     * checks if a brand name exists or not
     *
     * @param brandName
     * @return
     */
    public boolean doesBrandExist(String brandName) {
        Long count = (Long) (getSession().createQuery("select count(distinct p.brand) from Product p where p.brand = :brandName").setString("brandName", brandName).uniqueResult());
        return (count != null && count > 0);
    }

    public List<Product> getAllProductBySubCategory(String category) {
        return getSession().createQuery("select p from Product p left join p.categories c where c.name = :category order by p.orderRanking asc").setString("category", category).list();
    }

    public List<Product> getAllNonDeletedProducts() {




List<String> a = new ArrayList<String>();

a.add("ALCS01");
a.add("ALCS02");
a.add("ARVDC44");
a.add("ARVDC45");
a.add("BAB063");
a.add("BAB1434");
a.add("BAB152");
a.add("BAB2331");
a.add("BAB2443");
a.add("BAB2444");
a.add("BAB2445");
a.add("BAB2446");
a.add("BAB2447");
a.add("BAB2448");
a.add("BAB2449");
a.add("BAB2450");
a.add("BAB2451");
a.add("BAB2452");
a.add("BAB2453");
a.add("BAB2454");
a.add("BAB2455");
a.add("BAB2456");
a.add("BAB2457");
a.add("BAB2458");
a.add("BAB2459");
a.add("BAB2460");
a.add("BAB2461");
a.add("BAB2462");
a.add("BAB2463");
a.add("BAB2464");
a.add("BAB2465");
a.add("BAB2466");
a.add("BAB2467");
a.add("BAB2468");
a.add("BAB2469");
a.add("BAB2470");
a.add("BAB2472");
a.add("BAB2473");
a.add("BAB2474");
a.add("BAB2475");
a.add("BAB2476");
a.add("BAB2477");
a.add("BAB2478");
a.add("BAB2479");
a.add("BAB2480");
a.add("BAB2481");
a.add("BAB2482");
a.add("BAB2483");
a.add("BAB2485");
a.add("BAB2486");
a.add("BAB2487");
a.add("BAB2488");
a.add("BAB2489");
a.add("BAB2491");
a.add("BAB2492");
a.add("BAB2493");
a.add("BAB2494");
a.add("BAB2495");
a.add("BAB2498");
a.add("BAB2499");
a.add("BAB2500");
a.add("BAB2501");
a.add("BAB2502");
a.add("BAB2503");
a.add("BAB2504");
a.add("BAB2505");
a.add("BAB2508");
a.add("BAB2509");
a.add("BAB2510");
a.add("BAB2511");
a.add("BAB2512");
a.add("BAB2513");
a.add("BAB2514");
a.add("BAB2515");
a.add("BAB2516");
a.add("BAB2517");
a.add("BAB2518");
a.add("BAB2519");
a.add("BAB2521");
a.add("BAB2522");
a.add("BAB2523");
a.add("BAB2524");
a.add("BAB2525");
a.add("BAB2526");
a.add("BAB2527");
a.add("BAB2528");
a.add("BTY150");
a.add("BTY209");
a.add("BTY210");
a.add("BTY211");
a.add("BTY214");
a.add("BTY226");
a.add("BTY228");
a.add("BTY229");
a.add("BTY951");
a.add("BTYDV4");
a.add("BTYPONDS2");
a.add("BTYPONDS4");
a.add("BUT001");
a.add("BUT002");
a.add("BUT003");
a.add("BUT004");
a.add("BUT005");
a.add("BUT006");
a.add("BUT007");
a.add("BUT008");
a.add("BUT009");
a.add("BUT010");
a.add("BUT011");
a.add("BUT012");
a.add("BUT013");
a.add("BUT014");
a.add("BUT015");
a.add("BUT016");
a.add("BUT017");
a.add("BUT018");
a.add("BUT019");
a.add("BUT020");
a.add("BUT021");
a.add("BUT022");
a.add("BUT023");
a.add("CMB01");
a.add("CMB02");
a.add("CMB03");
a.add("CMB04");
a.add("DBRH11");
a.add("DM047");
a.add("DM048");
a.add("DM049");
a.add("DOVE02");
a.add("DOVE03");
a.add("DOVE07");
a.add("DOVE16");
a.add("DS047");
a.add("ELE001");
a.add("ELE002");
a.add("EQU001");
a.add("ERTC31");
a.add("EVA03");
a.add("EVE001");
a.add("EVE002");
a.add("EVE003");
a.add("EVE004");
a.add("EVE005");
a.add("EVE006");
a.add("EVE007");
a.add("EVE008");
a.add("EVE009");
a.add("EVE010");
a.add("EVE011");
a.add("EVE012");
a.add("EVE013");
a.add("EVE014");
a.add("EVE015");
a.add("EVE016");
a.add("EVE017");
a.add("EVE018");
a.add("EYE2317");
a.add("EYE2318");
a.add("EYE2319");
a.add("EYE2320");
a.add("EYE2321");
a.add("EYE2322");
a.add("EYE2323");
a.add("EYE2324");
a.add("EYE2325");
a.add("EYE2326");
a.add("EYE2327");
a.add("EYE2328");
a.add("EYE2329");
a.add("EYE2330");
a.add("EYE2331");
a.add("EYE2332");
a.add("EYE2333");
a.add("EYE2334");
a.add("EYE2335");
a.add("EYE2336");
a.add("EYE2337");
a.add("EYE2338");
a.add("EYE2339");
a.add("EYE2340");
a.add("EYE2341");
a.add("EYE2342");
a.add("EYE2343");
a.add("EYE2344");
a.add("EYE2345");
a.add("EYE2346");
a.add("EYE2347");
a.add("EYE2348");
a.add("EYE2349");
a.add("EYE2350");
a.add("EYE2351");
a.add("EYE2352");
a.add("EYE2353");
a.add("EYE2354");
a.add("EYE2355");
a.add("EYE2356");
a.add("EYE2357");
a.add("EYE2358");
a.add("EYE2359");
a.add("EYE2360");
a.add("EYE2361");
a.add("EYE2362");
a.add("EYE2363");
a.add("EYE2364");
a.add("EYE2365");
a.add("EYE2366");
a.add("EYE2367");
a.add("EYE2368");
a.add("EYE2369");
a.add("EYE2370");
a.add("EYE2371");
a.add("EYE2372");
a.add("EYE2373");
a.add("EYE2374");
a.add("EYE2375");
a.add("EYE2376");
a.add("EYE2377");
a.add("EYE2378");
a.add("EYE2379");
a.add("EYE2380");
a.add("EYE2381");
a.add("EYE2382");
a.add("EYE2383");
a.add("EYE2384");
a.add("EYE2385");
a.add("EYE2386");
a.add("EYE2387");
a.add("EYE2388");
a.add("EYE2389");
a.add("EYE2390");
a.add("EYE2391");
a.add("EYE2392");
a.add("EYE2393");
a.add("EYE2394");
a.add("EYE2395");
a.add("EYE2396");
a.add("EYE2397");
a.add("EYE2398");
a.add("EYE2399");
a.add("EYE2400");
a.add("EYE2401");
a.add("EYE2402");
a.add("EYE2403");
a.add("EYE2404");
a.add("EYE2405");
a.add("EYE2406");
a.add("EYE2407");
a.add("EYE2408");
a.add("EYE2409");
a.add("EYE2410");
a.add("EYE2411");
a.add("EYE2412");
a.add("EYE2413");
a.add("EYE2414");
a.add("EYE2415");
a.add("EYE2416");
a.add("EYE2417");
a.add("EYE2418");
a.add("EYE2419");
a.add("EYE2420");
a.add("EYE2421");
a.add("EYE2422");
a.add("EYE2423");
a.add("EYE2424");
a.add("EYE2425");
a.add("EYE2426");
a.add("EYE2427");
a.add("EYE2428");
a.add("EYE2429");
a.add("EYE2430");
a.add("EYE2431");
a.add("EYE2432");
a.add("EYE2433");
a.add("EYE2434");
a.add("EYE2435");
a.add("EYE2436");
a.add("EYE2437");
a.add("EYE2438");
a.add("EYE2439");
a.add("EYE2440");
a.add("EYE2441");
a.add("EYE2442");
a.add("EYE2443");
a.add("EYE2444");
a.add("EYE2445");
a.add("EYE2446");
a.add("EYE2447");
a.add("EYE2448");
a.add("EYE2449");
a.add("EYE2450");
a.add("EYE2451");
a.add("EYE2452");
a.add("EYE2453");
a.add("EYE2454");
a.add("EYE2455");
a.add("EYE2456");
a.add("EYE2457");
a.add("EYE2458");
a.add("EYE2459");
a.add("EYE2460");
a.add("EYE2461");
a.add("EYE2462");
a.add("EYE2463");
a.add("EYE2464");
a.add("EYE2465");
a.add("EYE2466");
a.add("EYE2467");
a.add("EYE2468");
a.add("EYE2469");
a.add("EYE2470");
a.add("EYE2471");
a.add("EYE2472");
a.add("EYE2473");
a.add("EYE2474");
a.add("EYE2475");
a.add("EYE2476");
a.add("EYE2477");
a.add("EYE2478");
a.add("EYE2479");
a.add("EYE2480");
a.add("EYE2481");
a.add("EYE2482");
a.add("EYE2483");
a.add("EYE2484");
a.add("EYE2485");
a.add("EYE2486");
a.add("EYE2487");
a.add("EYE2488");
a.add("EYE2489");
a.add("EYE2490");
a.add("EYE2491");
a.add("EYE2492");
a.add("EYE2493");
a.add("EYE2494");
a.add("EYE2495");
a.add("EYE2496");
a.add("EYE2497");
a.add("EYE2498");
a.add("EYE2499");
a.add("EYE2500");
a.add("EYE2501");
a.add("EYE2502");
a.add("EYE2503");
a.add("EYE2504");
a.add("EYE2505");
a.add("EYE2506");
a.add("EYE2507");
a.add("EYE2508");
a.add("EYE2509");
a.add("EYE2510");
a.add("EYE2511");
a.add("EYE2512");
a.add("EYE2513");
a.add("EYE2514");
a.add("EYE2515");
a.add("EYE2516");
a.add("EYE2517");
a.add("EYE2518");
a.add("EYE2519");
a.add("EYE2520");
a.add("EYE2521");
a.add("EYE2522");
a.add("EYE2523");
a.add("EYE2524");
a.add("EYE2525");
a.add("EYE2526");
a.add("EYE2527");
a.add("EYE2528");
a.add("EYE2529");
a.add("EYE2530");
a.add("EYE2531");
a.add("EYE2532");
a.add("EYE2533");
a.add("EYE2534");
a.add("EYE2535");
a.add("EYE2536");
a.add("EYE2537");
a.add("EYE2538");
a.add("EYE2539");
a.add("EYE2540");
a.add("EYE2541");
a.add("FAB001");
a.add("FAB002");
a.add("FRAC002");
a.add("FRIENDS001");
a.add("FRLVY3");
a.add("FRUTNI02");
a.add("FRUTNI03");
a.add("FRUTNI04");
a.add("FRUTNI05");
a.add("FRUTNI06");
a.add("FRUTNI07");
a.add("FRUTNI09");
a.add("FRUTNI10");
a.add("FRUTNI11");
a.add("FRUTNI12");
a.add("FRUTNI13");
a.add("FRUTNI14");
a.add("GN001");
a.add("GOD001");
a.add("GOD002");
a.add("GOD003");
a.add("GOD004");
a.add("GOD005");
a.add("GOD006");
a.add("GOD007");
a.add("GOD008");
a.add("GOD009");
a.add("GOD010");
a.add("GOD011");
a.add("GOD012");
a.add("GOD013");
a.add("GOD014");
a.add("HHD001");
a.add("HHD002");
a.add("HHD003");
a.add("HHD004");
a.add("HHD005");
a.add("HNUT201");
a.add("HNUT202");
a.add("HNUT203");
a.add("HNUT204");
a.add("HNUT205");
a.add("HNUT206");
a.add("HNUT207");
a.add("HNUT208");
a.add("HNUT209");
a.add("HNUT210");
a.add("HNUT211");
a.add("HNUT212");
a.add("HNUT213");
a.add("HNUT214");
a.add("HNUT215");
a.add("HNUT216");
a.add("HNUT217");
a.add("HNUT218");
a.add("HNUT219");
a.add("HNUT220");
a.add("HNUT221");
a.add("HNUT222");
a.add("HNUT223");
a.add("HNUT224");
a.add("HNUT225");
a.add("HNUT226");
a.add("HNUT227");
a.add("HNUT228");
a.add("HNUT229");
a.add("HNUT230");
a.add("HNUT231");
a.add("HNUT232");
a.add("HNUT233");
a.add("HNUT234");
a.add("HNUT235");
a.add("HNUT236");
a.add("HV011");
a.add("LAKME20");
a.add("LAKME21");
a.add("LAKME22");
a.add("LAKME23");
a.add("LAKME24");
a.add("LAKNTF8");
a.add("LKM004");
a.add("MOOD01");
a.add("MOOD02");
a.add("MOOD05");
a.add("MORE001");
a.add("MORP122");
a.add("MSH011");
a.add("NIVEA14");
a.add("NIVEA19");
a.add("NOSK001");
a.add("NUT1145");
a.add("NUT1146");
a.add("NUT1155");
a.add("NUT1156");
a.add("NUT1161");
a.add("NUT131");
a.add("NUT1620");
a.add("NUT1637");
a.add("NUT1640");
a.add("NUT1823");
a.add("NUT1825");
a.add("NUT1902");
a.add("NUT2153");
a.add("NUT2154");
a.add("NUT2655");
a.add("NUT2698");
a.add("NUT2928");
a.add("NUT3170");
a.add("NUT3171");
a.add("NUT3172");
a.add("NUT3173");
a.add("NUT3174");
a.add("NUT3175");
a.add("NUT3176");
a.add("NUT3177");
a.add("NUT364");
a.add("NUT376");
a.add("NUT471");
a.add("NUT716");
a.add("NUT793");
a.add("NUT920");
a.add("NUT922");
a.add("NUT974");
a.add("OST001");
a.add("OST002");
a.add("OST003");
a.add("OST004");
a.add("OST005");
a.add("OST006");
a.add("OST007");
a.add("OST008");
a.add("OST009");
a.add("OST010");
a.add("OST011");
a.add("OST012");
a.add("OST013");
a.add("OST014");
a.add("OST015");
a.add("OST016");
a.add("OST017");
a.add("OST018");
a.add("OST019");
a.add("OST020");
a.add("OST021");
a.add("OST022");
a.add("OST023");
a.add("OST024");
a.add("OST025");
a.add("OST026");
a.add("OST027");
a.add("OST028");
a.add("OST029");
a.add("OST030");
a.add("OST031");
a.add("OST032");
a.add("OST033");
a.add("OST034");
a.add("OST035");
a.add("OST036");
a.add("OST037");
a.add("OST038");
a.add("OST039");
a.add("OST040");
a.add("PHI068");
a.add("PHI069");
a.add("PHI070");
a.add("PHI071");
a.add("PHI072");
a.add("PHI073");
a.add("PHI074");
a.add("PHI075");
a.add("PHI076");
a.add("PHI077");
a.add("PHI078");
a.add("PHI079");
a.add("PHI080");
a.add("PHI081");
a.add("PHI082");
a.add("PHI083");
a.add("PHI084");
a.add("PHI085");
a.add("PHI086");
a.add("PHI087");
a.add("PHI088");
a.add("PHI089");
a.add("PNDS08");
a.add("PNDS22");
a.add("PPS011");
a.add("PS018");
a.add("PS019");
a.add("PS020");
a.add("PS021");
a.add("PS022");
a.add("PS043");
a.add("PW003");
a.add("PW004");
a.add("RELI001");
a.add("RSCR02");
a.add("SAN001");
a.add("SAN002");
a.add("SAN003");
a.add("SAN004");
a.add("SAN005");
a.add("SAN006");
a.add("SAN007");
a.add("SAN008");
a.add("SAN009");
a.add("SAN010");
a.add("SAN011");
a.add("SAN012");
a.add("SAN013");
a.add("SAN014");
a.add("SAN015");
a.add("SAN016");
a.add("SAN017");
a.add("SER297");
a.add("SKR007");
a.add("SLER1");
a.add("SLER2");
a.add("SLER3");
a.add("SLER4");
a.add("SLER5");
a.add("SLER6");
a.add("SLER7");
a.add("SLER8");
a.add("SMPLBG01");
a.add("SNF039");
a.add("SNF040");
a.add("SNF041");
a.add("SNF042");
a.add("SNF043");
a.add("SNF044");
a.add("SNF045");
a.add("SNF046");
a.add("SNF047");
a.add("SNF048");
a.add("SNF049");
a.add("SNTPR174");
a.add("SNTPR175");
a.add("SNTPR176");
a.add("SNTPR177");
a.add("SNTPR178");
a.add("SNTPR179");
a.add("SNTPR180");
a.add("SNTPR181");
a.add("SNTPR182");
a.add("SNTPR183");
a.add("SNTPR184");
a.add("SNTPR185");
a.add("SNTPR186");
a.add("SNTPR187");
a.add("SNTPR188");
a.add("SNTPR189");
a.add("SNTPR190");
a.add("SNTPR191");
a.add("SNTPR192");
a.add("SNTPR193");
a.add("SNTPR194");
a.add("SNTPR195");
a.add("SNTPR196");
a.add("SPT1441");
a.add("SPT1442");
a.add("SPT1692");
a.add("SPT2409");
a.add("SPT3870");
a.add("SPT4011");
a.add("SPT4012");
a.add("SPT4013");
a.add("SPT4102");
a.add("SPT4103");
a.add("SPT4104");
a.add("SPT4105");
a.add("SPT4106");
a.add("SPT4107");
a.add("SPT4108");
a.add("SPT4109");
a.add("SPT4139");
a.add("SPT4140");
a.add("SPT4141");
a.add("SPT4142");
a.add("SPT4143");
a.add("SPT4144");
a.add("SPT4145");
a.add("SPT4146");
a.add("SPT4147");
a.add("SPT4152");
a.add("SPT4153");
a.add("SPT4154");
a.add("SPT4155");
a.add("SPT4156");
a.add("SPT4157");
a.add("SPT4158");
a.add("SPT4159");
a.add("SPT4160");
a.add("SPT4161");
a.add("SPT4162");
a.add("SPT4163");
a.add("SPT4164");
a.add("SPT4165");
a.add("SPT4166");
a.add("SPT4167");
a.add("SPT4168");
a.add("SPT4169");
a.add("SPT4170");
a.add("SPT4171");
a.add("SPT4172");
a.add("SPT4173");
a.add("SPT4174");
a.add("SPT4175");
a.add("SPT4176");
a.add("SPT4177");
a.add("SPT4178");
a.add("SPT4179");
a.add("SPT4180");
a.add("SPT4181");
a.add("SPT4182");
a.add("SPT4183");
a.add("SPT4184");
a.add("SPT4185");
a.add("SPT4186");
a.add("SPT4187");
a.add("SPT4188");
a.add("SPT4189");
a.add("SPT4190");
a.add("SPT4191");
a.add("SPT4192");
a.add("SPT4193");
a.add("SPT4194");
a.add("SPT4195");
a.add("SPT4196");
a.add("SPT4197");
a.add("SPT4198");
a.add("SPT4199");
a.add("SPT4200");
a.add("SPT4201");
a.add("SPT4202");
a.add("SPT4203");
a.add("SPT4204");
a.add("SPT4205");
a.add("SPT4206");
a.add("SPT4207");
a.add("SPT4208");
a.add("SPT4209");
a.add("SPT4210");
a.add("SPT4211");
a.add("SPT4212");
a.add("SPT4213");
a.add("SPT4214");
a.add("SPT4215");
a.add("SPT4216");
a.add("SPT4217");
a.add("SPT4218");
a.add("SPT4219");
a.add("SPT4220");
a.add("SPT4221");
a.add("SPT4222");
a.add("SPT4223");
a.add("SPT4224");
a.add("SPT4225");
a.add("SPT4226");
a.add("SPT4227");
a.add("SPT4228");
a.add("SPT4229");
a.add("SPT4230");
a.add("SPT4231");
a.add("SPT4232");
a.add("SPT4233");
a.add("SPT4234");
a.add("SPT4235");
a.add("SPT4236");
a.add("SPT4237");
a.add("SPT4238");
a.add("SPT4239");
a.add("SPT4240");
a.add("SPT4241");
a.add("SPT4242");
a.add("SPT4243");
a.add("SPT4244");
a.add("SPT4245");
a.add("SPT4246");
a.add("SPT4247");
a.add("SPT4248");
a.add("SPT4249");
a.add("SPT4250");
a.add("SPT4251");
a.add("SPT4252");
a.add("SPT4253");
a.add("SPT4254");
a.add("SPT4255");
a.add("SPT4256");
a.add("SPT4257");
a.add("SPT4258");
a.add("SPT4259");
a.add("SPT4260");
a.add("SPT4261");
a.add("SPT4262");
a.add("SPT4263");
a.add("SPT4264");
a.add("SPT4265");
a.add("SPT4266");
a.add("SPT4267");
a.add("SPT4268");
a.add("SPT4269");
a.add("SPT4270");
a.add("SPT4271");
a.add("SPT4272");
a.add("SPT4273");
a.add("SPT4274");
a.add("SPT4275");
a.add("SPT4276");
a.add("SPT4277");
a.add("SPT4278");
a.add("SPT4279");
a.add("SPT4280");
a.add("SPT4281");
a.add("SPT4282");
a.add("SPT4283");
a.add("SPT4284");
a.add("SPT4285");
a.add("SPT4286");
a.add("SPT4287");
a.add("SPT4288");
a.add("SPT4289");
a.add("SPT4290");
a.add("SPT4291");
a.add("SPT4292");
a.add("SPT4293");
a.add("SPT4294");
a.add("SPT4295");
a.add("SPT4296");
a.add("SPT4297");
a.add("SPT4298");
a.add("SPT4299");
a.add("SPT4300");
a.add("SPT4301");
a.add("SPT4302");
a.add("SPT4303");
a.add("SPT4304");
a.add("SPT4305");
a.add("SPT4306");
a.add("SPT4307");
a.add("SPT4308");
a.add("SPT4309");
a.add("SPT4310");
a.add("SPT4311");
a.add("SPT4312");
a.add("SPT4313");
a.add("SPT4314");
a.add("SPT4315");
a.add("SPT4316");
a.add("SPT4317");
a.add("SPT4318");
a.add("SPT4319");
a.add("SPT4320");
a.add("SPT4321");
a.add("SPT4322");
a.add("SPT4323");
a.add("SPT4324");
a.add("SPT4325");
a.add("SPT4326");
a.add("SPT4327");
a.add("SPT4328");
a.add("SPT4329");
a.add("SPT4330");
a.add("SPT4331");
a.add("SPT4332");
a.add("SPT4333");
a.add("SPT4334");
a.add("SPT4335");
a.add("SPT4336");
a.add("SPT4337");
a.add("SPT4338");
a.add("SPT4339");
a.add("SPT4340");
a.add("SPT4341");
a.add("SPT4342");
a.add("SPT4343");
a.add("SPT4344");
a.add("SPT4345");
a.add("SPT4346");
a.add("SPT4347");
a.add("SPT4348");
a.add("SPT4349");
a.add("SPT4350");
a.add("SPT4351");
a.add("SPT4352");
a.add("SPT4353");
a.add("SPT4354");
a.add("SPT4355");
a.add("SPT4356");
a.add("SPT4357");
a.add("SPT4358");
a.add("SPT4359");
a.add("SPT4360");
a.add("SPT4361");
a.add("SPT4362");
a.add("SPT4363");
a.add("SPT4364");
a.add("SPT4365");
a.add("SPT4366");
a.add("SPT4367");
a.add("SPT4368");
a.add("SPT4369");
a.add("SPT4370");
a.add("SPT4371");
a.add("SPT4372");
a.add("SPT4373");
a.add("SPT4374");
a.add("SPT4375");
a.add("SPT4376");
a.add("SPT4377");
a.add("SPT4378");
a.add("SPT4379");
a.add("SPT4380");
a.add("SPT4381");
a.add("SPT4382");
a.add("SPT4383");
a.add("SPT4384");
a.add("SPT4385");
a.add("SPT4386");
a.add("SPT4388");
a.add("SPT4389");
a.add("SPT4390");
a.add("SPT4391");
a.add("SPT4392");
a.add("SPT4393");
a.add("SPT4394");
a.add("SPT4395");
a.add("SPT4396");
a.add("SPT4397");
a.add("SPT4398");
a.add("SPT4399");
a.add("STVKC03");
a.add("STVKC23");
a.add("TYNOR016");
a.add("VAADI39");
a.add("VAS001");
a.add("VAS010");
a.add("VEDIC30");
a.add("VEGA224");
a.add("VEGA225");
a.add("VEGA226");
a.add("VEGA227");
a.add("VEGA228");
a.add("VLC011");
a.add("VLCCFRT63");
        return getSession().createQuery("select p from Product p where p.deleted = false and id in (:pList)").setParameterList("pList", a).list();
    }

    public List<Product> getAllNonDeletedProductsWithImages() {
        return super.findByQuery("select p from Product p where p.deleted = false and p.mainImageId is not null");
    }

    public List<Product> getAllProductByBrand(String brand) {
        return getSession().createQuery("select p from Product p where p.brand = :brand order by p.orderRanking asc").setString("brand", brand).list();
    }

    public Page getAllProductsByCategoryAndBrand(String category, String brand, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        // Criteria criteria = getSession().createCriteria(Product.class);

        if (brand != null)
            criteria.add(Restrictions.eq("brand", brand));

        if (category != null) {
            DetachedCriteria categoryCriteria = criteria.createCriteria("categories");
            categoryCriteria.add(Restrictions.eq("name", category));
        }
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public Page getProductByBrand(String brand, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        // Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("brand", brand));
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return getSession().createQuery("select p from Product p left join p.categories c where c.name = :category and p.brand = :brand and p.deleted != :deleted").setString(
                "category", category).setString("brand", brand).setBoolean("deleted", true).list();
    }

    public Page getProductByCategoryAndBrand(String category, String brand, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        // Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("brand", brand));
        criteria.add(Restrictions.eq("deleted", false));
        DetachedCriteria categoryCriteria = criteria.createCriteria("categories");
        categoryCriteria.add(Restrictions.eq("name", category));
        criteria.addOrder(Order.asc("outOfStock"));
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

	public Page getProductByCategoryAndBrand(List<String> categoryNames, String brand, Boolean onlyCOD, Boolean includeCombo, int page, int perPage) {
		if (categoryNames != null && categoryNames.size() > 0) {
			List<String> productIds = new ArrayList<String>();
			productIds = getSession().createQuery(
					"select p.id from Product p inner join p.categories c where c.name in (:categories) group by p.id having count(*) = :tagCount").setParameterList(
					"categories",
					categoryNames).setInteger("tagCount", categoryNames.size()).list();
			if (productIds != null && productIds.size() > 0) {
				if (!includeCombo) {
					productIds = getSession().createQuery(
							"select distinct pv.product.id from ProductVariant pv where pv.product.id in (:productIds)").setParameterList(
							"productIds",
							productIds).list();
				}

				if (productIds != null && productIds.size() > 0) {

					DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
					if (StringUtils.isNotBlank(brand)) {
						criteria.add(Restrictions.eq("brand", brand));
					}
					criteria.add(Restrictions.in("id", productIds));
					criteria.add(Restrictions.eq("deleted", false));
					criteria.add(Restrictions.eq("isGoogleAdDisallowed", false));
					criteria.add(Restrictions.eq("hidden", false));
					if (onlyCOD) {
						criteria.add(Restrictions.eq("codAllowed", true));
					}

					criteria.addOrder(Order.asc("outOfStock"));
					criteria.addOrder(Order.asc("orderRanking"));

					return list(criteria, page, perPage);
				}
			}
		}
		return null;
	}

	public Page getProductByCategoryBrandAndOptions(List<String> categoryNames, String brand, List<Long> filterOptions, int groupsCount, Double minPrice, Double maxPrice,
                                                    Boolean onlyCOD, Boolean includeCombo, int page, int perPage) {
		if (categoryNames != null && categoryNames.size() > 0) {
			List<String> productIds = new ArrayList<String>();
            if (includeCombo){
                productIds = getSession().createQuery("select p.id from Product p inner join p.categories c where c.name in (:categories) and p.deleted <> 1 group by p.id having count(*) = :tagCount").setParameterList("categories", categoryNames).setInteger("tagCount", categoryNames.size()).list();
            }else{
                productIds = getSession().createQuery("select  distinct pv.product.id from ProductVariant pv inner join pv.product.categories c where c.name in (:categories) and pv.product.deleted <> 1 group by pv.product.id having count(*) = :tagCount").setParameterList("categories", categoryNames).setInteger("tagCount", categoryNames.size()).list();
            }

			if (productIds != null && !productIds.isEmpty()) {
				productIds = getSession().createQuery("select pv.product.id from ProductVariant pv where pv.product.id in (:productIds) and pv.hkPrice between :minPrice and :maxPrice and pv.deleted <> 1").setParameterList("productIds", productIds).setParameter("minPrice", minPrice).setParameter("maxPrice", maxPrice).list();
				if (productIds != null && !productIds.isEmpty() && filterOptions != null && !filterOptions.isEmpty() && groupsCount > 0) {
					productIds = getSession().createSQLQuery("select distinct pv.product_id from product_variant_has_product_option pvhpo, product_variant pv where pvhpo.product_variant_id=pv.id and pv.product_id in (:productIds) and pvhpo.product_option_id in (:filterOptions) group by pvhpo.product_variant_id having count(pvhpo.product_variant_id) = :groupsCount").setParameterList("productIds", productIds).setParameterList("filterOptions", filterOptions).setParameter("groupsCount", groupsCount).list();
				}
				if (productIds != null && !productIds.isEmpty()) {
					DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
					if (StringUtils.isNotBlank(brand)) {
						criteria.add(Restrictions.eq("brand", brand));
					}
					criteria.add(Restrictions.in("id", productIds));
					criteria.add(Restrictions.eq("deleted", false));
					criteria.add(Restrictions.eq("isGoogleAdDisallowed", false));
					criteria.add(Restrictions.eq("hidden", false));
          if (onlyCOD){
              criteria.add(Restrictions.eq("codAllowed", true));
          }
          criteria.addOrder(Order.asc("outOfStock"));
					criteria.addOrder(Order.asc("orderRanking"));
					return list(criteria, page, perPage);
				}
			}
		}
		return null;
	}

	// test code
    public Page getProductByCategoryAndBrandNew(Category cat1, Category cat2, Category cat3, String brand, int page, int perPage) {

        String q = "SELECT c.product_id FROM category_has_product c WHERE c.category_name =\"" + cat1.getName() + ""
                + "\" AND product_id IN (SELECT c.product_id FROM category_has_product c WHERE c.category_name =\"" + cat2.getName()
                + "\" AND product_id IN (SELECT c.product_id FROM category_has_product c WHERE c.category_name =\"" + cat3.getName() + "\"))";

        List<String> productIds = getSession().createSQLQuery(q).list();

        if (productIds != null && productIds.size() > 0) {

            DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
            if (StringUtils.isNotBlank(brand)) {
                criteria.add(Restrictions.eq("brand", brand));
            }
            criteria.add(Restrictions.in("id", productIds));
            criteria.add(Restrictions.eq("deleted", false));
            criteria.addOrder(Order.asc("orderRanking"));

            return list(criteria, page, perPage);
        }
        return null;
    }

    public List<Product> getProductByName(String name) {
        return getSession().createQuery("select p from Product p where p.name like :name and p.deleted != :deleted").setString("name", "%" + name + "%").setBoolean("deleted", true).list();
    }

    public Page getProductByName(String name, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.like("name", "%" + name + "%"));
        criteria.add(Restrictions.eq("deleted", false));
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public Page getProductByName(String name,boolean onlyCOD, boolean includeCombo, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.eq("deleted", false));
        if (onlyCOD){
            criteria.add(Restrictions.eq("codAllowed", true));
        }
        if (!includeCombo){
            List<String> productIds =  getSession().createQuery("select distinct pv.product.id from ProductVariant pv where pv.product.name like (:name) group by pv.product.id")
                    .setString("name","%" + name + "%").list();
            criteria.add(Restrictions.in("id", productIds));
        }else{
            criteria.add(Restrictions.like("name", "%" + name + "%"));
        }
        criteria.addOrder(Order.asc("orderRanking"));
        return list(criteria, page, perPage);
    }

    public List<Product> getRelatedProducts(Product product) {
        // List<Category> categoryList = new ArrayList<Category>();
        // Category lowestLevelCategory = product.getLowestLevelCategory();
        // if (lowestLevelCategory != null) {
        // categoryList.add(lowestLevelCategory);
        // }
        // categoryList.add(productManager.getTopLevelCategory(product));
        // return getSession().createQuery("select distinct p from Product p left join p.categories c where c in
        // (:categories) and p <> :currentProduct and p.deleted != :deleted and p.isGoogleAdDisallowed !=
        // :isGoogleAdDisallowed order by p.orderRanking asc ")
        // .setParameterList("categories", categoryList)
        // .setEntity("currentProduct", product)
        // .setBoolean("deleted", true)
        // .setBoolean("isGoogleAdDisallowed", true)
        // .setMaxResults(4)
        // .list();

        return new ArrayList<Product>();
    }

    public Set<Product> getProductsFromProductIds(String productIds) {
        Set<Product> products = new HashSet<Product>();

        String[] productIdArr = productIds.split(",");
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.in("id", Arrays.asList(productIdArr)));

        List<Product> productList = findByCriteria(criteria);
        products.addAll(productList);

        /*
         * for (String productId : productIds.split(",")) { Product product = find(productId.trim()); if (product !=
         * null) products.add(product); }
         */

        return products;

    }

    public List<Product> getAllProductsById(List<String> productIdList) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.in("id", productIdList));
        return findByCriteria(criteria);
    }

    public Page getPaginatedResults(List<String> productIdList, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
        criteria.add(Restrictions.in("id", productIdList));
        return list(criteria, page, perPage);
    }

    public List<Product> getRecentlyAddedProducts() {
        String query = "select p from Product p where p.deleted != :deleted and p.createDate != null order by p.createDate desc";
        return getSession().createQuery(query).setBoolean("deleted", true).list();
    }

    public ProductImage getProductImageByChecksum(String checksum) {
        return (ProductImage) getSession().createQuery("from ProductImage pi where pi.checksum = :checksum").setString("checksum", checksum).uniqueResult();
    }

    public List<ProductImage> getImagesByProductForProductMainPage(Product product) {
        return getSession().createQuery("from ProductImage pi where pi.product = :product and pi.productVariant is null and pi.hidden != :isHidden").setParameter("product",
                product)
                // .setParameter("productMainImageId", product.getMainImageId())
                .setBoolean("isHidden", true).list();
    }

    @SuppressWarnings("unchecked")
    public ProductExtraOption findProductExtraOptionByNameAndValue(String name, String value) {
        name = StringUtils.strip(name);
        value = StringUtils.strip(value);
        List<ProductExtraOption> extraOptionList = getSession().createQuery("from ProductExtraOption pxo where pxo.name = :name and pxo.value = :value").setString("name", name).setString(
                "value", value).list();
        return extraOptionList != null && extraOptionList.size() > 0 ? extraOptionList.get(0) : null;
    }

    public ProductGroup findProductGroupByName(String name) {
        return (ProductGroup) getSession().createQuery("from ProductGroup pg where pg.name=:name").setString("name", name).uniqueResult();
    }

    public ProductOption findProductOptionByNameAndValue(String name, String value) {
        name = StringUtils.strip(name);
        value = StringUtils.strip(value);
        List<ProductOption> optionList = getSession().createQuery("from ProductOption po where po.name = :name and po.value = :value").setString("name", name).setString("value",
                value).list();
        return optionList != null && optionList.size() > 0 ? optionList.get(0) : null;
    }
	
	public List<ProductOption> getProductOptions(List<Long> options) {
        return getSession().createQuery("from ProductOption po where po.id in(:options) order by upper(po.name), po.value asc").setParameterList("options", options).list();
    }

}
