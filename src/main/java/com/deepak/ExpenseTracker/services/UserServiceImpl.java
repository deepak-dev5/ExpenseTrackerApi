package com.deepak.ExpenseTracker.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deepak.ExpenseTracker.domain.User;
import com.deepak.ExpenseTracker.exceptions.EtAuthException;
import com.deepak.ExpenseTracker.respositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public User validateUser(String email, String password) throws EtAuthException {
		if(email!=null) email=email.toLowerCase();
		User user = userRepository.findByEmailAndPassword(email, password);
		return user;		
	}

	@Override
	public User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
		Pattern pattern = Pattern.compile("^(.+)@(.+)$");
		if(email!=null) email = email.toLowerCase();
		if(!pattern.matcher(email).matches())
			throw new EtAuthException("Invalid email format");
		Integer count = userRepository.getCountbyEmail(email);
		
		if(count>0)
			throw new EtAuthException("Emial already in use");
		Integer userId = userRepository.create(firstName, lastName, email, password);
		return userRepository.findByUserId(userId);
	}

}
