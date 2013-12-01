package com.hk.admin.impl.service.hkDelivery;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.constants.core.EnumRole;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;

@Service
public class HubServiceImpl implements HubService {

    @Autowired
    private HubDao hubDao;

    public List<Hub> getAllHubs() {
        return hubDao.getAllHubs();  
    }

    public Hub findHubByName(String hubName) {
        return hubDao.findHubByName(hubName);
    }

    @Override
    public Hub getHubForUser(User user) {
        return hubDao.getHubForUser(user);
    }

    @Override
    public List<User> getAgentsForHub(Hub hub){
        return hubDao.getAgentsForHub(hub);
    }

    @Override
    public Boolean addAgentToHub(Hub hub, User user){
        if(getHubForUser(user) != null){
            return false;
        }
        else{
            Set<Role> userRole = user.getRoles();
            if(!userRole.contains(EnumRole.HK_DELIVERY_HUB_MANAGER.toRole())){
                userRole.add(EnumRole.HK_DELIVERY_GUY.toRole());
            }
            user.setRoles(userRole);
            hub.getUsers().add(user);
            hubDao.save(hub);
            return true;
        }
    }

	@Override
	public Boolean removeAgentFromHub(Hub hub, User user) {
		if(getHubForUser(user) != null){
			if(user.getRoles().contains(EnumRole.HK_DELIVERY_GUY.toRole())){
				user.getRoles().remove(EnumRole.HK_DELIVERY_GUY.toRole());
			}
			hub.getUsers().remove(user);
			hubDao.save(hub);
			return true;
		}
		return false;
	}
}
