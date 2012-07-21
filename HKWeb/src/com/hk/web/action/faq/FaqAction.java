package com.hk.web.action.faq;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.faq.Faq;
import com.hk.pact.service.faq.FaqService;
import com.hk.constants.FaqCategoryEnums;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.SimpleError;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 12, 2012
 * Time: 3:20:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FaqAction extends BaseAction {

    @Autowired
    FaqService faqService;

    List<Faq> faqList;
    String searchString;
    String primaryCategory;
    private Faq faq;

/*    @ValidationMethod(on = "addNewFaq")
    public void validateSearch() {
        if (StringUtils.isBlank(question) || StringUtils.isBlank(answer)) {
            getContext().getValidationErrors().add("1", new SimpleError("Question or answer cannot be left blank"));
        }
    }*/

    @DefaultHandler
    public Resolution pre() {
        primaryCategory = FaqCategoryEnums.EnumFaqPrimaryCateogry.Nutrition.getName();
        if (searchString != null && !searchString.equals("")) {
            faqList = faqService.searchFaq(searchString);
        } else {
            faqList = faqService.getFaqByCategory(primaryCategory);
        }
        return new ForwardResolution("/pages/faq/faq.jsp");
    }

    public Resolution addNewFaq() {
        if (faq==null || faq.getAnswer() == null || faq.getQuestion() == null) {
            return new RedirectResolution(FaqAction.class);
        }
        Boolean status = faqService.insertFaq(faq);

        if (!status) {
            return new RedirectResolution(FaqAction.class);
        }
        return new RedirectResolution(FaqAction.class);
    }

    public Resolution searchFaq() {
        faqList = faqService.searchFaq(searchString);
        return new ForwardResolution("/pages/faq/faq.jsp");
    }

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

    public Resolution deleteFaq() {
        Boolean status = faqService.deleteFaq(faq);
        return new ForwardResolution("/pages/close.jsp");
    }

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
}
