package com.hk.core.search;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/1/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersSearchCriteria {

    // list of variables that might be received
    private String state;
    private String city;
    private String zone;
    private Long productVariantId;
    private Long productId;
    private Boolean verified;

    public DetachedCriteria getSearchCriteria() {
        return getTestCriteria();
//        return getCriteriaFromBaseCriteria();
    }

    private DetachedCriteria getTestCriteria() {
        DetachedCriteria c = DetachedCriteria.forClass(User.class);
        c.add(Restrictions.eq("id", "97"));
        return c;
    }

    private DetachedCriteria getCriteriaFromBaseCriteria() {

        DetachedCriteria criteria = DetachedCriteria.forClass(User.class, "u");

        if (productId != null || productVariantId != null) {
            DetachedCriteria c2 = criteria.createCriteria("baseOrder");
            DetachedCriteria c3 = c2.createCriteria("cartLineItem");
            DetachedCriteria c4 = c3.createCriteria("productVariant");
            DetachedCriteria c5 = c4.createCriteria("product");

            if (productId != null) {
                c5.add(Restrictions.eq("id", productId));
            } else if (productVariantId != null) {
                DetachedCriteria c6 = c5.createCriteria("productVariant");
                c6.add(Restrictions.eq("id", productVariantId));
            }

        }

        if (StringUtils.isNotBlank(city)) {
            DetachedCriteria c2 = criteria.createCriteria("address");
            c2.add(Restrictions.eq("city", city));
        }

        if (StringUtils.isNotBlank(state)) {
            DetachedCriteria c2 = criteria.createCriteria("address");
            c2.add(Restrictions.eq("state", state));
        }

        if (StringUtils.isNotBlank(zone)) {
            DetachedCriteria c2 = criteria.createCriteria("address");
            c2.add(Restrictions.eq("zone", zone));
        }

		/*if (productId != null || productVariantId != null) {
            criteria.createAlias("u.orders", "o");
            criteria.createAlias("o.cartLineItems", "cli");
            criteria.createAlias("cli.productVariant", "pv");
            criteria.createAlias("pv.Product", "p");


            String prod_id = null;
            if (productId != null) {
                prod_id = productId.toString();
            } else if (productVariantId != null) {
                prod_id = getProdId(productVariantId);
            }
            criteria.add(Restrictions.eq("p.id", prod_id));
        }*/
        return criteria;
    }


    public UsersSearchCriteria setZone(String zone) {
        this.zone = zone;
        return this;
    }

    public UsersSearchCriteria setVerified(String verified) {
        try {
            this.verified = Boolean.parseBoolean(verified);
        } catch (Exception e) {
            this.verified = null;
        }
        return this;
    }

    public UsersSearchCriteria setProductId(String id) {
        this.productId = Long.parseLong(id);
        return this;
    }

    public UsersSearchCriteria setProductVariantId(String id) {
        this.productVariantId = Long.parseLong(id);
        return this;
    }

    public UsersSearchCriteria setCity(String city) {
        this.city = city;
        return this;
    }


    public UsersSearchCriteria setState(String state) {
        this.state = state;
        return this;
    }

    private String getProdId(Long productVariantId) {
        //FIXME get prod from db and not from this approach since it isn't immune to change of mapping between prod and prod variant ids
        String pv = productVariantId.toString();
        int index = pv.indexOf('-');
        pv = pv.substring(0, index);
        return pv;
    }

}
