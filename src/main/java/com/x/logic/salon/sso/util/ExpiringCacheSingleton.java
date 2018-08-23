package com.x.logic.salon.sso.util;

public class ExpiringCacheSingleton {

	public static SelfExpiringMap<String, String> map;
	public final static int LOGIN_SLEEP_TIME = 300000;

	public ExpiringCacheSingleton() {
		synchronized (ExpiringCacheSingleton.class) {
			if (map == null) {
				getMapInstance();
			}
		}
	}

	private void getMapInstance() {
		map = new SelfExpiringHashMap<String, String>();
	}

}
