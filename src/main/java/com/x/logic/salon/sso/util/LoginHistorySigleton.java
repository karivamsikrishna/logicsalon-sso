package com.x.logic.salon.sso.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginHistorySigleton {

	private Map<String, String> loginLinkMap = new HashMap<>();
	private Map<String, List<String>> loginTokenLinkMap = new HashMap<>();

	public static LoginHistorySigleton loginHistorySigleton = null;

	public LoginHistorySigleton() {
		if (loginHistorySigleton == null) {
			synchronized (LoginHistorySigleton.class) {
				if (loginHistorySigleton == null) {
					loginHistorySigleton = new LoginHistorySigleton();
				}
			}
		}
	}

	public void addLoginLinkMap(String key, String value) {
		loginLinkMap.put(key, value);
	}

	public String getLoginLinkMapValue(String key) {
		return loginLinkMap.get(key);
	}

	public void addLoginTokenLinkMap(String key, List<String> value) {
		loginTokenLinkMap.put(key, value);
	}

	public List<String> getLoginTokenLinkMapValue(String key) {
		return loginTokenLinkMap.get(key);
	}
}
