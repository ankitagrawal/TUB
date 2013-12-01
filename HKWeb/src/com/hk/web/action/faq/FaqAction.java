package com.hk.web.action.faq;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.faq.Faq;
import com.hk.pact.service.faq.FaqService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA.
 * User: Tarun Mittal
 * Date: Jul 12, 2012
 * Time: 3:20:54 PM
 * To change this template use File | Settings | File Templates.
 */
@UrlBinding("/faq/{primaryCategory}/{secondaryCategory}")
@Component
public class FaqAction extends BasePaginatedAction {

    @Autowired
    FaqService faqService;

    List<Faq> faqList;
    String searchString;
    String primaryCategory;
    String secondaryCategory;
    private Faq faq;
    Page faqPage;

    private int defaultPerPage = 10;

    @DefaultHandler
    public Resolution pre() {
        primaryCategory = faqService.getCategoryFromSlug(primaryCategory);
        secondaryCategory = faqService.getCategoryFromSlug(secondaryCategory);
        if (searchString != null && !searchString.equals("")) {
            faqPage = faqService.searchFaq(searchString, primaryCategory, secondaryCategory, getPageNo(), getPerPage());
            faqList = faqPage.getList();
        } else {
            faqPage = faqService.getFaqByCategory(primaryCategory, secondaryCategory, getPageNo(), getPerPage());
            faqList = faqPage.getList();
        }
        return new ForwardResolution("/pages/faq/faq.jsp");
    }

	@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS}, authActionBean = AdminPermissionAction.class)
    public Resolution addNewFaq() {
        if (faq == null || faq.getAnswer() == null || faq.getQuestion() == null) {
            return new RedirectResolution(FaqAction.class);
        }
        Boolean status = faqService.insertFaq(faq);

        if (!status) {
            return new RedirectResolution(FaqAction.class);
        }
        return new RedirectResolution(FaqAction.class, "pre");
    }

	@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS}, authActionBean = AdminPermissionAction.class)
    public Resolution saveFaq() {
        if (faq.getAnswer() == null
                || faq.getQuestion() == null
                || faq.getQuestion().equals("")
                || faq.getAnswer().equals("")
                ) {
            addRedirectAlertMessage(new SimpleMessage("Question or answer cannot be left blank"));
            return new ForwardResolution("/pages/faq/editFaq.jsp");
        }
        faq = faqService.save(faq);
        return new ForwardResolution("/pages/close.jsp");
    }

	@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS}, authActionBean = AdminPermissionAction.class)
    public Resolution deleteFaq() {
        Boolean status = faqService.deleteFaq(faq);
        return new ForwardResolution("/pages/close.jsp");
    }

	@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS}, authActionBean = AdminPermissionAction.class)
    public Resolution editFaq() {
        if (faq == null) {
            addRedirectAlertMessage(new SimpleMessage("Unable to determine faq"));
            return new ForwardResolution("/pages/faq/faq.jsp");
        }
        return new ForwardResolution("/pages/faq/editFaq.jsp");
    }

    public List<Faq> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<Faq> faqList) {
        this.faqList = faqList;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return faqPage == null ? 0 : faqPage.getTotalPages();
    }

    public int getResultCount() {
        return faqPage == null ? 0 : faqPage.getTotalResults();
    }


    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("searchString");
        params.add("primaryCategory");
        params.add("secondaryCategory");
        return params;
    }
}
