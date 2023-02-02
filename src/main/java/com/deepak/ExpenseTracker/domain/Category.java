package com.deepak.ExpenseTracker.domain;

public class Category {
	
	private Integer userId;
	private Integer categoryId;
	private String title;
	private String description;
	private double totalExpense;
	
	public Category(Integer userId, Integer categoryId, String title, String description, double totalExpense) {
		super();
		this.userId = userId;
		this.categoryId = categoryId;
		this.title = title;
		this.description = description;
		this.totalExpense = totalExpense;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(double totalExpense) {
		this.totalExpense = totalExpense;
	}
		

}
