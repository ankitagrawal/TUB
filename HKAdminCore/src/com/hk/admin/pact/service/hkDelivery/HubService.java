package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.user.User;

import java.util.List;


public interface HubService {

    public List<Hub> getAllHubs();

    public Hub findHubByName(String hubName);

    public Hub getHubForUser(User user);

    public List<User> getAgentsForHub(Hub hub);

    public Boolean addAgentToHub(Hub hub, User user);

	public Boolean removeAgentFromHub(Hub hub, User user);
}
