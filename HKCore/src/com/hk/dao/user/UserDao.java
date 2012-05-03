package com.hk.dao.user;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.dao.impl.RoleDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.dto.user.UserFilterDto;

public interface UserDao {

    public User getUserById(Long userId) ;


    @Transactional(readOnly = false)
    public User save(User user) ;

    public List<User> findByEmail(String email) ;

    public User findByLogin(String login) ;

    public User findByEmailAndPassword(String email, String password) ;

    public User findByUserHandle(String userHandle) ;

    public User findByUserHash(String userHash) ;

    public List<User> findByRole(Role role) ;

    public Page findByRole(Role role, int pageNo, int perPage) ;

    public Page listUsersByRole(Role role, int page, int perPage) ;

    public Page search(UserFilterDto userFilterDto, int pageNo, int perPage) ;


    public List<User> referredUserList(User user) ;

    public User findByOfferInstance(OfferInstance offerInstance) ;

    public Page getAllMailingList(int pageNo, int perPage) ;

    public Page getAllUnverifiedMailingList(int pageNo, int perPage) ;

    public List<User> getAllMissingUsersLastOrderId(Integer missingSinceDays) ;


    public Page getMailingList(Category category, int pageNo, int perPage) ;

    public RoleDao getRoleDao() ;

    public void setRoleDao(RoleDao roleDao) ;

}
