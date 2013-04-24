package com.hk.admin.pact.service.email;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/18/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductVariantNotifyMeEmailService {

    public void sendNotifyMeEmail(final float notifyConversionRate, final int bufferRate);

}
