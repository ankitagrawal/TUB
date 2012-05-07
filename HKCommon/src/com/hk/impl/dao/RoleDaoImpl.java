package com.hk.impl.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Role;
import com.hk.pact.dao.RoleDao;

@Repository
public class RoleDaoImpl extends BaseDaoImpl implements RoleDao {

    public Role getRoleByName(EnumRole enumRole) {
        return getRoleByName(enumRole.getRoleName());
    }
    
    public Role getRoleByName(String roleName) {
        return get(Role.class, roleName);
    }

    public List<Role> listAllExceptAdmins() {
        List<Role> roles = getAll(Role.class);

        for (Iterator<Role> iterator = roles.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();
            if (role.getName().equals(RoleConstants.ADMIN) || role.getName().equals(RoleConstants.GOD) || role.getName().equals(RoleConstants.ROCKSTAR)
                    || role.getName().equals(RoleConstants.CATEGORY_MANAGER) || role.getName().equals(RoleConstants.CUSTOMER_SUPPORT)
                    || role.getName().equals(RoleConstants.CUSTOMER_SUPPORT_L2) || role.getName().equals(RoleConstants.CUSTOMER_SUPPORT_L3)
                    || role.getName().equals(RoleConstants.OPS_MANAGER) || role.getName().equals(RoleConstants.TICKETADMIN) || role.getName().equals(RoleConstants.REPORT_ADMIN)
                    || role.getName().equals(RoleConstants.HK_EMPLOYEE) || role.getName().equals(RoleConstants.MASTER_BUG_RESOLVER)
                    || role.getName().equals(RoleConstants.SUPPLIER)) {
                iterator.remove();
            }
        }

        return roles;
    }

    @SuppressWarnings("unchecked")
    public List<Role> listAllBasicRoles() {

        List<String> basicRoleNames = new ArrayList<String>();

        basicRoleNames.add(EnumRole.COD_BLOCKED.getRoleName());
        basicRoleNames.add(EnumRole.HK_BLOCKED.getRoleName());
        basicRoleNames.add(EnumRole.HK_DEACTIVATED.getRoleName());
        basicRoleNames.add(EnumRole.HK_DELETED.getRoleName());
        basicRoleNames.add(EnumRole.HK_UNVERIFIED.getRoleName());
        basicRoleNames.add(EnumRole.TEMP_USER.getRoleName());
        basicRoleNames.add(EnumRole.UNSUBSCRIBED_USER.getRoleName());
        basicRoleNames.add(EnumRole.HK_USER.getRoleName());
        basicRoleNames.add(EnumRole.HK_IHO_USER.getRoleName());

        DetachedCriteria criteria = DetachedCriteria.forClass(Role.class);
        criteria.add(Restrictions.in("name", Arrays.asList(basicRoleNames)));

        return findByCriteria(criteria);
    }

}
