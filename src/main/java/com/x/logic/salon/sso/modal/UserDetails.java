package com.x.logic.salon.sso.modal;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserDetails {

	private String name;
	private String sirName;
	private String title;
	private UserType userType;
	private int noOfFailedAttemps;
	@Id
	private String email;
	private String password;
	private String cell;
	private boolean isLocked;
	private List<String> companyList;
	private Address address;
	private String actionerCapabilityType;
	private String actionerUserName;
	private boolean isRequestFromloggedInUser;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSirName() {
		return sirName;
	}

	public void setSirName(String sirName) {
		this.sirName = sirName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}


	public List<String> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<String> companyList) {
		this.companyList = companyList;
	}

	public int getNoOfFailedAttemps() {
		return noOfFailedAttemps;
	}

	public void setNoOfFailedAttemps(int noOfFailedAttemps) {
		this.noOfFailedAttemps = noOfFailedAttemps;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getActionerCapabilityType() {
		return actionerCapabilityType;
	}

	public void setActionerCapabilityType(String actionerCapabilityType) {
		this.actionerCapabilityType = actionerCapabilityType;
	}

	public String getActionerUserName() {
		return actionerUserName;
	}

	public void setActionerUserName(String actionerUserName) {
		this.actionerUserName = actionerUserName;
	}

	public boolean isRequestFromloggedInUser() {
		return isRequestFromloggedInUser;
	}

	public void setRequestFromloggedInUser(boolean isRequestFromloggedInUser) {
		this.isRequestFromloggedInUser = isRequestFromloggedInUser;
	}

}
