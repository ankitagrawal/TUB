package com.hk.impl.dao.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.core.AddressDao;

@SuppressWarnings("unchecked")
@Repository
public class AddressDaoImpl extends BaseDaoImpl implements AddressDao {

    @Transactional
    public Address save(Address address) {
        if (address != null) {
            if (address.getCreateDate() == null)
                address.setCreateDate(BaseUtils.getCurrentTimestamp());
            if (address.isDeleted() == null)
                address.setDeleted(false);
        }
        return (Address) super.save(address);
    }

    public List<Address> getVisibleAddresses(User user) {
        List<Address> addresses = new ArrayList<Address>();
        for (Address address : user.getAddresses()) {
            if (!address.isDeleted())
                addresses.add(address);
        }
        return addresses;
    }

    public List<Address> getVisibleAddresses(List<Address> addressList) {
        List<Address> addresses = new ArrayList<Address>();
        for (Address address : addressList) {
            if (!address.isDeleted())
                addresses.add(address);
        }
        return addresses;
    }

    public Page getVisibleAddressesForManufacturer(Long manufacturerId, String city, int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Address.class);
        List<Long> addressIds = new ArrayList<Long>();
        Manufacturer manufacturer = get(Manufacturer.class, manufacturerId);
        if (manufacturer == null) {
            return null;
        }
        List<Address> addressList = manufacturer.getAddresses();

        if (addressList.size() > 0) {
            for (Address address : addressList) {
                addressIds.add(address.getId());
            }
            criteria.add(Restrictions.in("id", addressIds));

            criteria.add(Restrictions.eq("deleted", Boolean.FALSE));

            if (city != null)
                criteria.add(Restrictions.eq("city", city));

            return list(criteria, pageNo, perPage);
        } else
            return null;
    }

    public List<Address> getAllAddresses() {
        return getSession().createQuery("select distinct o.address from Order o where o.address.phone is not null").list();
    }

    public List<Address> getAllAddressesByCategories(List<Category> applicableCategories) {
        String hqlQuery = "select distinct(li.order.address) from CartLineItem li left join li.productVariant.product.categories c "
                + "where c in (:applicableCategories) and  li.order.address.phone is not null";
        return getSession().createQuery(hqlQuery).setParameterList("applicableCategories", applicableCategories).list();
    }

    @SuppressWarnings("unchecked")
    public List<Address> getDuplicateAddresses(Address a) {
        String hqlQuery = "select a from Address a " + "where a.pin=:pin and a.phone=:phone and a.user <> :user";
        return getSession().createQuery(hqlQuery).setParameter("pin", a.getPin()).setParameter("phone", a.getPhone()).setParameter("user", a.getUser()).list();
    }

    // public List<Address> getVisibleAddressesFromAddressList(List<Address> addressList)
    // {
    // if(addressList.size()!=0)
    // {
    // return getSession().createQuery("select a from Address a where a in (:addressList) and a.deleted= :deleted")
    // .setParameter("deleted",Boolean.FALSE)
    // .setParameterList("addressList",addressList)
    // .list();
    // }
    // else
    // return null;
    //
    // }

}   