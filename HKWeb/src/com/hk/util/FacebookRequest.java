package com.hk.util;



public class FacebookRequest {
	public FacebookRequest() {
	}

	User user;
	String algorithm;
	String issued_at;
	String user_id;
	String oauth_token;
	String expires;
	String app_data;

	Page page;
	String profile_id;

	public static class Page {
		String id;
		boolean liked;
		boolean admin;

		public Page() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public boolean isLiked() {
			return liked;
		}

		public void setLiked(boolean liked) {
			this.liked = liked;
		}

		public boolean isAdmin() {
			return admin;
		}

		public void setAdmin(boolean admin) {
			this.admin = admin;
		}
	}

	public static class User {
		String locale;
		String country;

		public User() {
		}

		public static class Age {
			String min;
			String max;

			public Age() {
			}

			public String getMin() {
				return min;
			}

			public void setMin(String min) {
				this.min = min;
			}

			public String getMax() {
				return max;
			}

			public void setMax(String max) {
				this.max = max;
			}
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(String issued_at) {
		this.issued_at = issued_at;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getApp_data() {
		return app_data;
	}

	public void setApp_data(String app_data) {
		this.app_data = app_data;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}
}
