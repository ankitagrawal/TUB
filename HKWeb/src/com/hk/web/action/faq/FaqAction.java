package com.hk.web.action.faq;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.faq.Faq;
import com.hk.pact.service.faq.FaqService;
import com.hk.constants.FaqCategoryEnums;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.ForwardResolution;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 12, 2012
 * Time: 3:20:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FaqAction extends BaseAction{

    @Autowired
    FaqService faqService;

    List<Faq> faqList;
    String primaryCategory;

    @DefaultHandler
    public Resolution pre(){
        primaryCategory = FaqCategoryEnums.EnumFaqPrimaryCateogry.Nutrition.getName();
        faqList = faqService.getFaqByCategory(primaryCategory);
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
}
