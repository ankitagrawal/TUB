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
        String hqlQuery = "from Faq faq where faq.question like :searchString or faq.answer like :searchString or faq.primaryCategory like :searchString";

        Query faqListQuery = getSession().createQuery(hqlQuery).setParameter("searchString", "%"+searchString+"%");

        return (List<Faq>) faqListQuery.list();
    } 
}