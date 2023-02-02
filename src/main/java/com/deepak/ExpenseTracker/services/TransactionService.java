package com.deepak.ExpenseTracker.services;

import java.util.List;

import com.deepak.ExpenseTracker.domain.Transaction;
import com.deepak.ExpenseTracker.exceptions.EtBadRequestException;
import com.deepak.ExpenseTracker.exceptions.EtResourceNotFoundException;

public interface TransactionService {
	
	List<Transaction> fetchAllTransaction(Integer userId, Integer categoryId);
	
	Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;
	
	Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException;
	
	void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws EtBadRequestException;
	
	void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;
	

}
