package com.deepak.ExpenseTracker.respositories;

import java.util.List;

import org.springframework.stereotype.Component;

import com.deepak.ExpenseTracker.domain.Category;
import com.deepak.ExpenseTracker.exceptions.EtBadRequestException;
import com.deepak.ExpenseTracker.exceptions.EtResourceNotFoundException;

public interface CategoryRepository {
	
	List<Category> findAll(Integer userId) throws EtResourceNotFoundException;
	
	Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
	
	Integer create(Integer userId, String title, String description) throws EtBadRequestException;
	
	void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;
	
	void removeById(Integer userId, Integer categoryId);
	
}
