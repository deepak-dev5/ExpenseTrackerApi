package com.deepak.ExpenseTracker.respositories;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.deepak.ExpenseTracker.domain.User;
import com.deepak.ExpenseTracker.exceptions.EtAuthException;

@Repository
public class UserRepositroyImpl implements UserRepository{
	
	private static final String SQL_CREATE = "INSERT INTO et_users(first_name, last_name, email, password) values(?,?,?,?)";
	private static final String SQL_COUNT_BY_EMAIL = "Select count(*) from et_users where email=?";
	private static final String SQL_FIND_BY_ID = "Select user_id,first_name, last_name, email,password from et_users where user_id=?";
	private static final String SQL_FIND_BY_EMAIL_PASS = "select user_id,first_name, last_name, email, password from et_users where email=?";
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {
		String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt(10));
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, email);
				ps.setString(4, hashedPassword);
				return ps;
			}, keyHolder);
			
			return (int) keyHolder.getKey().longValue();
			
		} catch (Exception e) {
			System.out.println(e);
			throw new EtAuthException("Invalid details. Failed to create account");
		}
	}

	@Override
	public User findByEmailAndPassword(String email, String password) throws EtAuthException {
		try {
			User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL_PASS, new Object[] {email}, userRowMapper);
			if(!BCrypt.checkpw(password, user.getPassword()))
				throw new EtAuthException("invalid email password");
			return user;
		}catch(EmptyResultDataAccessException e) {
			throw new EtAuthException("invalid email password");
		}
	}

	@Override
	public Integer getCountbyEmail(String email) throws EtAuthException {
		return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[] {email}, Integer.class);
	}

	@Override
	public User findByUserId(Integer userId) throws EtAuthException {
		return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[] {userId}, userRowMapper);
	}
		
		 private RowMapper<User> userRowMapper = ((rs,rowNum)->{ 
			return new User(rs.getInt("user_id")
					,rs.getString("first_name")
					,rs.getString("last_name")
					,rs.getString("email")
					,rs.getString("password"));
		});

}
