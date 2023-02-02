package com.deepak.ExpenseTracker.services;

import java.util.List;

import com.deepak.ExpenseTracker.domain.Category;
import com.deepak.ExpenseTracker.exceptions.EtBadRequestException;
import com.deepak.ExpenseTracker.exceptions.EtResourceNotFoundException;

public interface CategoryService {

	List<Category> fetchAllCategory(Integer userId);
	
	Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
	
	Category addCategory(Integer userId, String title, String description) throws EtBadRequestException;
	
	void updateCategory(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;
	
	void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
	
	
}
