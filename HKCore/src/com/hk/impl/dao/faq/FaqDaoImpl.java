package com.hk.impl.dao.faq;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.faq.Faq;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.faq.FaqDao;

@SuppressWarnings("unchecked")
@Repository
public class FaqDaoImpl extends BaseDaoImpl implements FaqDao {

    public Page searchFaq(String searchString, String primaryCategory, String secondaryCategory, int pageNo, int perPage) {

        DetachedCriteria faqCriteria = DetachedCriteria.forClass(Faq.class);

        if(primaryCategory != null){
            faqCriteria.add(Restrictions.like("primaryCategory", "%" + primaryCategory + "%"));
        }
        if(secondaryCategory != null){
            faqCriteria.add(Restrictions.like("secondaryCategory", "%" + secondaryCategory + "%"));
        }

        faqCriteria.add(Restrictions.or(
                Restrictions.like("question", "%"+searchString+"%"),
                Restrictions.like("answer", "%"+searchString+"%")
        ));

        faqCriteria.addOrder(org.hibernate.criterion.Order.asc("pageRank"));

        return list(faqCriteria, pageNo, perPage);
    }

    public Page getFaqByCategory(String primaryCategory, String secondaryCategory, int pageNo, int perPage){

        DetachedCriteria faqCriteria = DetachedCriteria.forClass(Faq.class);

        if(primaryCategory != null){
            faqCriteria.add(Restrictions.like("primaryCategory", "%" + primaryCategory + "%"));
        }
        if(secondaryCategory != null){
            faqCriteria.add(Restrictions.like("secondaryCategory", "%" + secondaryCategory + "%"));
        }

        faqCriteria.addOrder(org.hibernate.criterion.Order.asc("pageRank"));

        return list(faqCriteria, pageNo, perPage);


/*
        String hqlQueryForNotNullPageRank = "from Faq faq where faq.primaryCategory like :category " +
                "and faq.pageRank is not null order by faq.pageRank";

        Query faqListQuery = getSession().createQuery(hqlQueryForNotNullPageRank).setParameter("category", category);

        List<Faq> faqList = (List<Faq>) faqListQuery.list();

        String hqlQueryForNullPageRank = "from Faq faq where faq.primaryCategory like :category " +
                "and faq.pageRank is null order by faq.pageRank";

        faqListQuery = getSession().createQuery(hqlQueryForNullPageRank).setParameter("category", "%"+category+"%");

        faqList.addAll((List<Faq>) faqListQuery.list());

        return faqList;*/
    }
}