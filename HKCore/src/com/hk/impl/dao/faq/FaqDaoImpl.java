package com.hk.impl.dao.faq;

import org.springframework.stereotype.Repository;
import org.hibernate.Query;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.faq.FaqDao;
import com.hk.domain.faq.Faq;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
@Repository
public class FaqDaoImpl extends BaseDaoImpl implements FaqDao {

    public List<Faq> searchFaq(String searchString) {

        String hqlQueryForNotNullPageRank = "from Faq faq where (faq.question like :searchString or faq.answer like :searchString or faq.primaryCategory like :searchString) " +
                                         "and faq.pageRank is not null order by faq.pageRank";

        Query faqListQuery = getSession().createQuery(hqlQueryForNotNullPageRank).setParameter("searchString", "%"+searchString+"%");

        List<Faq> faqList =  (List<Faq>) faqListQuery.list();

        String hqlQueryForNullPageRank = "from Faq faq where (faq.question like :searchString or faq.answer like :searchString or faq.primaryCategory like :searchString) " +
                                         "and faq.pageRank is null order by faq.pageRank";

        faqListQuery = getSession().createQuery(hqlQueryForNullPageRank).setParameter("searchString", "%"+searchString+"%");

        faqList.addAll((List<Faq>) faqListQuery.list());

        return faqList;
    }
    
    public List<Faq> getFaqByCategory(String category){
        String hqlQueryForNotNullPageRank = "from Faq faq where faq.primaryCategory like :category " +
                "and faq.pageRank is not null order by faq.pageRank";

        Query faqListQuery = getSession().createQuery(hqlQueryForNotNullPageRank).setParameter("category", category);

        List<Faq> faqList = (List<Faq>) faqListQuery.list();

        String hqlQueryForNullPageRank = "from Faq faq where faq.primaryCategory like :category " +
                "and faq.pageRank is null order by faq.pageRank";

        faqListQuery = getSession().createQuery(hqlQueryForNullPageRank).setParameter("category", "%"+category+"%");

        faqList.addAll((List<Faq>) faqListQuery.list());

        return faqList;
    }
}