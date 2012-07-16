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
    String primaryCategory;
    String question;
    String answer;
    String secondaryCategory;
    String keywordString;

/*    @ValidationMethod(on = "addNewFaq")
    public void validateSearch() {
        if (StringUtils.isBlank(question) || StringUtils.isBlank(answer)) {
            getContext().getValidationErrors().add("1", new SimpleError("Question or answer cannot be left blank"));
        }
    }*/

    @DefaultHandler
    public Resolution pre() {
        primaryCategory = FaqCategoryEnums.EnumFaqPrimaryCateogry.Nutrition.getName();
        faqList = faqService.getFaqByCategory(primaryCategory);
        return new ForwardResolution("/pages/faq/faq.jsp");
    }

    public Resolution addNewFaq() {
        faqService.insertFaq(question, answer, primaryCategory, secondaryCategory, keywordString);
        addRedirectAlertMessage(new SimpleMessage("New FAQ inserted."));
        return new ForwardResolution("/pages/faq/faq.jsp");
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public String getKeywordString() {
        return keywordString;
    }

    public void setKeywordString(String keywordString) {
        this.keywordString = keywordString;
    }
}
