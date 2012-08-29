package com.hk.impl.service.faq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.domain.faq.Faq;
import com.hk.pact.dao.faq.FaqDao;
import com.hk.pact.service.faq.FaqService;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 13, 2012
 * Time: 2:49:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FaqServiceImpl implements FaqService{
    @Autowired
    FaqDao faqDao;



    private static Logger logger                 = LoggerFactory.getLogger(FaqServiceImpl.class);

    public Page getFaqByCategory(String primaryCategory, String secondaryCategory,  int pageNo, int perPage){
        return getFaqDao().getFaqByCategory(primaryCategory, secondaryCategory, pageNo, perPage);
    }

    public boolean insertFaq(Faq faq){
        Faq newFaq = new Faq();
        try{
            newFaq.setQuestion(faq.getQuestion());
            newFaq.setAnswer(faq.getAnswer());
            newFaq.setPrimaryCategory(faq.getPrimaryCategory());
            newFaq.setSecondaryCategory(faq.getSecondaryCategory());
            newFaq.setKeywords(faq.getKeywords());
            newFaq.setPageRank(faq.getPageRank());
            save(newFaq);
            return true;
        }
        catch(Exception e){
            logger.debug("Unable to insert FAQ: ", e);
        }
        return false;
    }

    public Faq save(Faq faq){
        return (Faq)getFaqDao().save(faq);
    }

    public Page searchFaq(String keywords, String primaryCategory, String secondaryCategory,  int pageNo, int perPage){
        return getFaqDao().searchFaq(keywords, primaryCategory, secondaryCategory, pageNo, perPage);
    }


    public Faq getFaqById(Long id){
        return getFaqDao().get(Faq.class, id);
    }

    public boolean deleteFaq(Faq faq){
        try{
            getFaqDao().delete(faq);
            return true;
        }catch(Exception e){
            logger.debug("unable to delete FAQ", e);
        }
        return false;
    }

    public String getCategoryFromSlug(String categorySlug){
        if(categorySlug != null){
            return categorySlug.replace("-", " ");
        }
        else{
            return null;
        }
    }

    public FaqDao getFaqDao() {
        return faqDao;
    }

    public void setFaqDao(FaqDao faqDao) {
        this.faqDao = faqDao;
    }
}
