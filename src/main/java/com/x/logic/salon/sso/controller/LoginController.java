package com.x.logic.salon.sso.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.x.logic.salon.sso.message.Message;
import com.x.logic.salon.sso.modal.LoginHistory;
import com.x.logic.salon.sso.modal.TimeStamp;
import com.x.logic.salon.sso.modal.UserDetails;
import com.x.logic.salon.sso.repos.LoginHistoryRepository;
import com.x.logic.salon.sso.repos.UserRepository;
import com.x.logic.salon.sso.reqresp.LoginRequest;
import com.x.logic.salon.sso.reqresp.LoginResponse;
import com.x.logic.salon.sso.util.EAD;
import com.x.logic.salon.sso.util.ExpiringCacheSingleton;
import com.x.logic.salon.sso.util.LoginHistorySigleton;

public class LoginController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	LoginHistorySigleton loginHistorySigleton = LoginHistorySigleton.getInstance();
	ExpiringCacheSingleton cacheSingleton = ExpiringCacheSingleton.getInstance();

	public LoginController() {
		//cacheSingleton = new ExpiringCacheSingleton();
	}

	public boolean validateLoginRequest(LoginRequest loginRequest) {
		LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-2");
		if (loginRequest.getUserName() != null && !("").equals(loginRequest.getUserName())
				&& loginRequest.getPassword() != null && !("").equals(loginRequest.getPassword())
				&& loginRequest.getCompanyId() != null && !("").equals(loginRequest.getCompanyId())) {
			LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-3");
			return true;
		}
		LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-4");
		return false;
	}

	public LoginResponse setloginResponse(UserRepository userRepository, LoginRequest loginRequest,
			LoginHistoryRepository loginHistoryRepository, HttpServletResponse response, LoginResponse loginResponse) {

		LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-6");
		if (isUserAlreadyLoggedIn(loginRequest.getUserName())) {
			LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-7");
			kickOutUserFromSystem(loginRequest.getUserName(), loginHistoryRepository,
					"System kickedOut due to new login request!.");
		}
		UserDetails userDetails = userRepository.findOne(loginRequest.getUserName());
		Message message = new Message();
		boolean userLoginValidatedSuccessfully = false;
		if (userDetails != null) {
			LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-8");
			if (validatePassword(loginRequest.getPassword(), userDetails.getPassword())
					&& validateCompanyProfile(loginRequest.getCompanyId(), userDetails.getCompanyList())) {
				userLoginValidatedSuccessfully = true;
			}
		} else {
			LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-9");
			message.setErrorMessage("User Not found. Please register.");
		}

		if (userLoginValidatedSuccessfully) {
			LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-10");
			String token = UUID.randomUUID().toString();
			// setResponseCookie(response, token);
			setResponseHeaders(response, token);
			setTokenIntoCache(token, userDetails);
			loginResponse.setUser(userDetails);
			message.setSuccessMessage("User login sucessfull.");
			List<String> tokenList = new ArrayList<>();
			tokenList.add(token);
			loginHistorySigleton.addLoginTokenLinkMap(userDetails.getEmail(), tokenList);
			makeLoginAuditEntry(userDetails.getEmail(), loginHistoryRepository);
		} else {
			LOG.info("----------------SSO-------------"+ loginRequest.getUserName()+"-11");
			message.setErrorMessage("Username and password do not match.");
		}

		loginResponse.setMessage(message);

		
		return loginResponse;
	}

	private boolean validateCompanyProfile(String companyId, List<String> companyList) {
		if (companyId.contains(companyId)) {
			return true;
		}
		return false;
	}

	private void kickOutUserFromSystem(String userName, LoginHistoryRepository loginHistoryRepository, String message) {
		String loginHistoryId = loginHistorySigleton.getLoginLinkMapValue(userName);
		if (loginHistoryId != null && !loginHistoryId.equals("")) {
			LoginHistory history = loginHistoryRepository.findOne(loginHistoryId);
			TimeStamp timeStamp = history.getTimeStamp();
			timeStamp.setLoggedOut(new Date());
			history.setMessage(message);
			loginHistoryRepository.save(history);
			loginHistorySigleton.removeLoginLinkMap(userName);
			if (loginHistorySigleton.checkLoginTokenLinkMapExist(userName)) {
				List<String> tokenList = loginHistorySigleton.getLoginTokenLinkMapValue(userName);
				for (String token : tokenList) {
					cacheSingleton.map.remove(token);
				}
				loginHistorySigleton.removeLoginTokenLinkMap(userName);
			}
		}
	}

	private boolean isUserAlreadyLoggedIn(String userName) {
		if (loginHistorySigleton.checkLoginLinkMapExist(userName)) {
			return true;
		}
		return false;
	}

	private void setTokenIntoCache(String token, UserDetails userDetails) {
		eriseSencitiveInformation(userDetails);
		Gson gson = new Gson();
		cacheSingleton.map.put(token, gson.toJson(userDetails), ExpiringCacheSingleton.LOGIN_SLEEP_TIME);
	}

	private void eriseSencitiveInformation(UserDetails userDetails) {
		userDetails.setPassword("");
	}

	private void setResponseCookie(HttpServletResponse response, String token) {
		response.addCookie(new Cookie("auth-token", token));
	}

	private void setResponseHeaders(HttpServletResponse response, String token) {
		response.addHeader("auth-token", token);
	}

	private boolean validatePassword(String looginPassword, String userPassword) {
		String encriptPassword = EAD.encrypt("hfhfhfhfhfhfhfhf", "RandomInitVector", looginPassword);
		if (encriptPassword != null && !encriptPassword.equals("") && encriptPassword.equals(userPassword)) {
			return true;
		}

		return false;
	}

	private void makeLoginAuditEntry(String userName, LoginHistoryRepository loginHistoryRepository) {
		LoginHistory loginHistory = new LoginHistory();
		loginHistory.setUserId(userName);

		TimeStamp timeStamp = new TimeStamp();
		timeStamp.setLoggedIn(new Date());

		loginHistory.setTimeStamp(timeStamp);
		LoginHistory history = loginHistoryRepository.save(loginHistory);
		loginHistorySigleton.addLoginLinkMap(userName, history.getId());
	}

	public boolean validateLogedInUser(HttpServletRequest request, HttpServletResponse response, String userName,
			LoginHistoryRepository loginHistoryRepository) {

		String token = request.getHeader("auth-token");
		if (token != null && "".equals(token) && cacheSingleton.map.containsKey(token)) {
			String value = cacheSingleton.map.get(token);
			String newToken = UUID.randomUUID().toString();
			cacheSingleton.map.put(newToken, value, ExpiringCacheSingleton.LOGIN_SLEEP_TIME);
			response.addHeader("auth-token", newToken);
			return true;
		} else {
			response.addHeader("auth-token", "");
			if (isUserAlreadyLoggedIn(userName)) {
				kickOutUserFromSystem(userName, loginHistoryRepository, "User authrorization failed.");
			}
		}

		return false;
	}

	public boolean logoutUser(HttpServletRequest request, HttpServletResponse response, String userName,
			LoginHistoryRepository loginHistoryRepository) {

		kickOutUserFromSystem(userName, loginHistoryRepository, "User Logedout action.");
		String token = request.getHeader("auth-token");
		if (token != null && !"".equals(token)) {
			response.addHeader("auth-token", "");
			return true;
		}
		return false;
	}
}
