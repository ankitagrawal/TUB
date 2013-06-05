package com.hk.impl.service.codbridge;

import com.hk.domain.user.User;

public class UserThreadLocal {
	
	private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<User>(){};
	
	public static void set(User user) {
		userThreadLocal.set(user);
	}

	public static void unset() {
		userThreadLocal.remove();
	}

	public static User get() {
		return userThreadLocal.get();
	}
}
