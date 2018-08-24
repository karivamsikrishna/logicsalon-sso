package com.x.logic.salon.sso.util;

public class ExpiringCacheSingleton {

	public SelfExpiringMap<String, String> map = new SelfExpiringHashMap<String, String>();
	public final static int LOGIN_SLEEP_TIME = 300000;

	private static ExpiringCacheSingleton instance = null;

	public static ExpiringCacheSingleton getInstance() {
		if (instance == null) {
			synchronized (ExpiringCacheSingleton.class) {
				if (instance == null) {
					instance = new ExpiringCacheSingleton();
					return instance;
				}
			}
		}
		return instance;

	}

}
