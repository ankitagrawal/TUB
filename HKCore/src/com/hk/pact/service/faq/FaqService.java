package com.hk.pact.service.faq;

import com.akube.framework.dao.Page;
import com.hk.domain.faq.Faq;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 13, 2012
 * Time: 2:44:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FaqService {

    public Page getFaqByCategory(String primaryCategory, String secondaryCategory,  int pageNo, int perPage);

    public boolean insertFaq(Faq faq);

    public Page searchFaq(String keywords, String primaryCategory, String secondaryCategory,  int pageNo, int perPage);

    public Faq save(Faq faq);

    public Faq getFaqById(Long id);

    public boolean deleteFaq(Faq faq);

    public String getCategoryFromSlug(String categorySlug);
}
