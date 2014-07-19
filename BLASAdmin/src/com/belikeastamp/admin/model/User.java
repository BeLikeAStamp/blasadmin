package com.belikeastamp.admin.model;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6200533120706672347L;
	/**
	 * 
	 */

	private Long id;
	private String firstname;
	private String name;
	private String phone;
	private String email;
	private String address;
	private Boolean isPartener;
	private Boolean isHost;
	
	public User() {}

	public User(String firstname, String name, String phone, String email) {
		super();
		this.firstname = firstname;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsPartener() {
		return isPartener;
	}

	public void setIsPartener(Boolean isPartener) {
		this.isPartener = isPartener;
	}

	public Boolean getIsHost() {
		return isHost;
	}

	public void setIsHost(Boolean isHost) {
		this.isHost = isHost;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", firstname=" + firstname + ", name="
				+ name + ", phone=" + phone + ", email=" + email + "]";
	}

}
