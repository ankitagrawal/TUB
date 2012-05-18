package com.hk.web.action.admin.email;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.MailingListManager;
import com.hk.constants.core.Keys;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;
import com.hk.pact.service.catalog.CategoryService;

@Secure(hasAnyRoles = { RoleConstants.MARKETING })
@Component
public class EmailListByCategoryAction extends BaseAction {

    @Validate(required = true)
    private String     category;

    private String     mailingListCSV_URL;

    private List<User> userList;

    @Autowired
    CategoryService    categoryService;
    @Autowired
    MailingListManager mailingListManager;

    @Value("#{hkEnvProps['" + Keys.Env.mailingListDir + "']}")
    String             mailingListDirPath;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/emailListByCategory.jsp");
    }

    /*
     * public Resolution showMailingList() { Category cat = categoryDao.getCategoryByName(category);
     * this.setUserList(mailingListManager.getUserList(cat));
     * this.setMailingListCSV_URL("mailingList/"+cat+"MailingList.csv"); //return new
     * RedirectResolution(EmailAListByCategoryAction.class).addParameter("mailingList", mailingList); return new
     * ForwardResolution("/pages/admin/mailingListCSV.jsp"); }
     */

    public Resolution getMailingListAsCSV() {
        Category cat = getCategoryService().getCategoryByName(category);
        this.setUserList(mailingListManager.getUserList(cat, 1, 1000000));
        this.setMailingListCSV_URL("mailingList/" + cat.getDisplayName() + "MailingList.csv");
        // return new RedirectResolution(EmailAListByCategoryAction.class).addParameter("mailingList", mailingList);
        return new RedirectResolution("/mailingList/" + cat.getDisplayName() + "MailingList.csv");
    }

    @DontValidate
    public Resolution getAllMailingListAsCSV() {
        this.setUserList(mailingListManager.getAllUserList(1, 1000000));
        this.setMailingListCSV_URL("mailingList/" + "All" + "MailingList.csv");
        // return new RedirectResolution(EmailAListByCategoryAction.class).addParameter("mailingList", mailingList);
        return new RedirectResolution("/mailingList/" + "All" + "MailingList.csv");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getMailingListCSV_URL() {
        return mailingListCSV_URL;
    }

    public void setMailingListCSV_URL(String mailingListCSV_URL) {
        this.mailingListCSV_URL = mailingListCSV_URL;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
