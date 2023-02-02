package com.deepak.ExpenseTracker.respositories;

import com.deepak.ExpenseTracker.domain.User;
import com.deepak.ExpenseTracker.exceptions.EtAuthException;

public interface UserRepository {
	
	Integer create(String firstName, String lastName, String email, String password) throws EtAuthException;
	
	User findByEmailAndPassword(String email, String password)  throws EtAuthException;
	
	Integer getCountbyEmail(String email)  throws EtAuthException;
	
	User findByUserId (Integer userId)  throws EtAuthException;	

}
