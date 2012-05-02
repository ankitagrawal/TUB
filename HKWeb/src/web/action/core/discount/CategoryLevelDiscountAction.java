package web.action.core.discount;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.ProductManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.BaseDao;
import com.hk.dao.affiliate.AffiliateCategoryDao;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.affiliate.DefaultCategoryCommission;
import com.hk.service.AffilateService;
import com.hk.service.CategoryService;
import com.hk.service.ProductService;
import com.hk.service.ProductVariantService;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Jul 21, 2011 Time: 3:31:08 PM To change this template use File |
 * Settings | File Templates.
 */
@Secure(hasAnyPermissions = { PermissionConstants.MANAGE_AFFILIATES }, authActionBean = AdminPermissionAction.class)
@Component
public class CategoryLevelDiscountAction extends BaseAction {

    private ProductManager                  productManager;
    private ProductService                  productService;
    private AffilateService                 affiliateService;
    private CategoryService                 categoryService;
    private ProductVariantService           productVariantService;
    private BaseDao                         baseDao;
    private AffiliateCategoryDao affiliateCategoryCommissionDao;
    
    // AffiliateCategoryDao affiliateCategoryDao;
    // CategoryDao categoryDao;

    // DefaultCategoryCommissionDao defaultCategoryCommissionDao;
    // AffiliateDao affiliateDao;
    
    // ProductVariantDao productVariantDao;

    AffiliateCategoryCommission             affiliateCategoryCommission;
    DefaultCategoryCommission               defaultCategoryCommission;

    @Validate(required = true)
    String                                  category;
    @Validate(required = true)
    Double                                  commissionFirstTime;
    @Validate(required = true)
    Double                                  commissionLatterTime;
    @Validate(required = true)
    String                                  overview;

    private List<DefaultCategoryCommission> defaultAffiliateCategoryCommissionList;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/categoryLevelDiscount.jsp");
    }

    public Resolution saveExisting() {
        for (DefaultCategoryCommission categoryCommission : defaultAffiliateCategoryCommissionList) {
            if (categoryCommission.isSelected()) {
                getBaseDao().save(categoryCommission);
            }
        }
        return new RedirectResolution(CategoryLevelDiscountAction.class);
    }

    public Resolution deleteExisting() {
        for (DefaultCategoryCommission categoryCommission : defaultAffiliateCategoryCommissionList) {
            if (categoryCommission.isSelected()) {
                getBaseDao().delete(categoryCommission.getId());
                affiliateCategoryCommissionDao.deleteCategoryCommissionForExistingAffiliates(categoryCommission.getAffiliateCategory());
                getProductVariantService().findAndSetBlankAffiliateCategory(categoryCommission.getAffiliateCategory());
                getBaseDao().delete(categoryCommission.getAffiliateCategory().getAffiliateCategoryName());
            }
        }
        return new RedirectResolution(CategoryLevelDiscountAction.class);
    }

    public Resolution saveNew() {
        saveCommission(category, commissionFirstTime, commissionLatterTime, overview);
        return new RedirectResolution(CategoryLevelDiscountAction.class);
    }

    public void saveCommission(String category, Double commissionFirstTime, Double commissionLatterTime, String overview) {
        AffiliateCategory affiliateCategory = getBaseDao().get(AffiliateCategory.class, category);
        if (affiliateCategory == null) {
            affiliateCategory = new AffiliateCategory();
            defaultCategoryCommission = new DefaultCategoryCommission();
            affiliateCategory.setAffiliateCategoryName(category);
            affiliateCategory.setOverview(overview);
            getBaseDao().save(affiliateCategory);
            defaultCategoryCommission.setAffiliateCategory(affiliateCategory);
            updateAffiliateCommissionPlans(category, commissionFirstTime, commissionLatterTime);
        }
        defaultCategoryCommission.setCommissionFirstTime(commissionFirstTime);
        defaultCategoryCommission.setCommissionLatterTime(commissionLatterTime);
        getBaseDao().save(defaultCategoryCommission);
    }

    public void updateAffiliateCommissionPlans(String category, Double commissionFirstTime, Double commissionLatterTime) {
        List<Affiliate> affiliates = getBaseDao().getAll(Affiliate.class);
        for (Affiliate affiliate : affiliates) {
            affiliateCategoryCommission = new AffiliateCategoryCommission();
            affiliateCategoryCommission.setAffiliate(affiliate);
            affiliateCategoryCommission.setAffiliateCategory(getBaseDao().get(AffiliateCategory.class, category));
            affiliateCategoryCommission.setCommissionFirstTime(commissionFirstTime);
            affiliateCategoryCommission.setCommissionLatterTime(commissionLatterTime);
            getBaseDao().save(affiliateCategoryCommission);
        }
    }

    public List<DefaultCategoryCommission> getDefaultAffiliateCategoryCommissionList() {
        defaultAffiliateCategoryCommissionList = getBaseDao().getAll(DefaultCategoryCommission.class);
        return defaultAffiliateCategoryCommissionList;
    }

    public void setDefaultAffiliateCategoryCommissionList(List<DefaultCategoryCommission> defaultAffiliateCategoryCommissionList) {
        this.defaultAffiliateCategoryCommissionList = defaultAffiliateCategoryCommissionList;
    }

    public Double getCommissionFirstTime() {
        return commissionFirstTime;
    }

    public void setCommissionFirstTime(Double commissionFirstTime) {
        this.commissionFirstTime = commissionFirstTime;
    }

    public Double getCommissionLatterTime() {
        return commissionLatterTime;
    }

    public void setCommissionLatterTime(Double commissionLatterTime) {
        this.commissionLatterTime = commissionLatterTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public AffilateService getAffiliateService() {
        return affiliateService;
    }

    public void setAffiliateService(AffilateService affiliateService) {
        this.affiliateService = affiliateService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}