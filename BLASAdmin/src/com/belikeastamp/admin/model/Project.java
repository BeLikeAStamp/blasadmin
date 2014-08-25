package com.belikeastamp.admin.model;

import java.io.Serializable;

public class Project implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8669324256727505603L;

	private Long id;
	private Long userId;
	private String name;
	private String subDate;
	private String detail;
	private String type;
	private String orderDate;
	private String perso;
	private String colors;
	private int status;
	private int quantity;

	public Project() {}



	public Project(Long userId, String project_name, String sub_date, int project_status,
			String detail, String type, String order_date, int nbr_cards, String perso) {
		super();
		this.userId = userId;
		this.name = project_name;
		this.subDate = sub_date;
		this.status = project_status;
		this.detail = detail;
		this.type = type;
		this.orderDate = order_date;
		this.quantity = nbr_cards;
		this.setPerso(perso);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSubDate() {
		return subDate;
	}



	public void setSubDate(String subDate) {
		this.subDate = subDate;
	}



	public String getDetail() {
		return detail;
	}



	public void setDetail(String detail) {
		this.detail = detail;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getOrderDate() {
		return orderDate;
	}



	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public String getPerso() {
		return perso;
	}



	public void setPerso(String perso) {
		this.perso = perso;
	}



	public String getColors() {
		return colors;
	}



	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getPrintableDetails() {

		String s = getDetail().replace(", ", "\n");
		return s;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", userId=" + userId + ", name=" + name
				+ ", subDate=" + subDate + ", detail=" + detail + ", type="
				+ type + ", orderDate=" + orderDate + ", perso=" + perso
				+ ", colors=" + colors + ", status=" + status + ", quantity="
				+ quantity + "]";
	}


}
