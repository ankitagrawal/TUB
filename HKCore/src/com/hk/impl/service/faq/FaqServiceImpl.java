package com.hk.impl.service.faq;

import com.hk.pact.service.faq.FaqService;
import com.hk.pact.dao.faq.FaqDao;
import com.hk.domain.faq.Faq;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public List<Faq> getFaqByCategory(String primaryCategory){
        return getFaqDao().getFaqByCategory(primaryCategory);
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

    public List<Faq> searchFaq(String keywords){
        List<Faq> faqList = getFaqDao().searchFaq(keywords);
        return faqList;
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

    public FaqDao getFaqDao() {
        return faqDao;
    }

    public void setFaqDao(FaqDao faqDao) {
        this.faqDao = faqDao;
    }
}
