package com.hk.pact.service.faq;

import com.hk.domain.faq.Faq;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 13, 2012
 * Time: 2:44:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FaqService {

    public List<Faq> getFaqByCategory(String category);

    public boolean insertFaq(String question, String answer, String primaryCategory, String secondaryCategory, String keywordString);

    public List<Faq> searchFaq(String keywords);

    public Faq save(Faq faq);
}
