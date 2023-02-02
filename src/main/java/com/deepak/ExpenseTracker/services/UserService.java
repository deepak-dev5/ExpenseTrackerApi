package com.deepak.ExpenseTracker.services;

import com.deepak.ExpenseTracker.domain.User;
import com.deepak.ExpenseTracker.exceptions.EtAuthException;

public interface UserService {
	
	User validateUser(String email, String password) throws EtAuthException;
	
	User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException;

}
