package com.hk.impl.service.faq;

import com.hk.pact.service.faq.FaqService;
import com.hk.pact.dao.BaseDao;
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
    BaseDao baseDao;

    private static Logger logger                 = LoggerFactory.getLogger(FaqServiceImpl.class);
    
    public List<Faq> getFaqByCategory(String primaryCategory){
        List<Faq> faqListFiltered = new ArrayList<Faq>();
        List<Faq> faqList = getBaseDao().getAll(Faq.class);
        if(primaryCategory == null){
            return faqList;
        }
        for(Faq faq : faqList){
            if(faq.getPrimary_category().equals(primaryCategory)){
                faqListFiltered.add(faq);
            }
        }
        return faqListFiltered;
    }

    public boolean insertFaq(String question, String answer, String primaryCategory, String secondaryCategory, String keywordString){
        Faq faq = new Faq();
        try{
            faq.setQuestion(question);
            faq.setAnswer(answer);
            faq.setPrimary_category(primaryCategory);
            faq.setSecondary_category(secondaryCategory);
            faq.setKeywords(keywordString);
            getBaseDao().save(faq);
            return true;
        }
        catch(Exception e){
            logger.debug("Unable to insert FAQ: ", e);
        }
        return false;
    }

    public List<Faq> searchFaq(String keywords){
        List<Faq> faqList = new ArrayList<Faq>();
        return faqList;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
