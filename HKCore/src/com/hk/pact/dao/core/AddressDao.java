package com.hk.pact.dao.core;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.order.OrderBillingAddress;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

public interface AddressDao extends BaseDao {

    public Address save(Address address);

    public List<Address> getVisibleAddresses(User user);

    public List<Address> getVisibleAddresses(List<Address> addressList);

    public Page getVisibleAddressesForManufacturer(Long manufacturerId, String city, int pageNo, int perPage);

    public List<Address> getAllAddresses();

    public List<Address> getAllAddressesByCategories(List<Category> applicableCategories);

    @SuppressWarnings("unchecked")
    public List<Address> getDuplicateAddresses(Address a);

     public OrderBillingAddress getBillingAddress(Order order );

}