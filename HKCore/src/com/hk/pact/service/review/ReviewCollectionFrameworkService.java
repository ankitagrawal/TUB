package com.hk.pact.service.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;


public interface ReviewCollectionFrameworkService {

    public boolean sendTestEmail(User user, Product product);

    public int sendDueEmails();

    public void doUserEntryForReviewMail(Order order);
}
