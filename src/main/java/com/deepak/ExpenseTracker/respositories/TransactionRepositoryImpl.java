package com.deepak.ExpenseTracker.respositories;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.deepak.ExpenseTracker.domain.Transaction;
import com.deepak.ExpenseTracker.exceptions.EtBadRequestException;
import com.deepak.ExpenseTracker.exceptions.EtResourceNotFoundException;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository{
	
	private static final String SQL_CREATE = "insert into et_transactions(category_id, user_id, amount, note, transaction_date) values(?,?,?,?,?)";
	private static final String SQL_FIND_BY_ID = "select transaction_id, category_id, user_id, amount, note, transaction_date from "
			+ "et_transactions where user_id=? and category_id=? and transaction_id=?";
	private static final String SQL_FIND_ALL="select transaction_id, category_id, user_id, amount, note, transaction_date from "
			+ "et_transactions where user_id=? and category_id=?";
	
	private static final String SQL_UPDATE = "update et_transactions set amount=?, note=?,transaction_date=? where user_id=? and category_id=? "
			+ "and transaction_id = ?"; 
	
	private static final String SQL_DELETE = "delete from et_transactions where user_id=? and category_id=? and transaction_id=?";
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Transaction> findAll(Integer userId, Integer categoryId) {
		return jdbcTemplate.query(SQL_FIND_ALL, transactionRowMapper, userId, categoryId);
	}

	@Override
	public Transaction findById(Integer userId, Integer categoryId, Integer transactionId)
			throws EtResourceNotFoundException {
		try {
			return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, transactionRowMapper, userId, categoryId, transactionId);
		} catch (Exception e) {
			System.out.println(e);
			throw new EtResourceNotFoundException("not found");
		}
	}

	@Override
	public Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate)
			throws EtBadRequestException {
		try {
			KeyHolder keyholder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection->{
				PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, categoryId);
				ps.setInt(2, userId);
				ps.setDouble(3,  amount);
				ps.setString(4, note);
				ps.setLong(5, transactionDate);
				System.out.println(ps);
				return ps;
			}, keyholder);
			return (int) keyholder.getKey().longValue();
			
		} catch (Exception e) {
			throw new EtBadRequestException("invalid Request");
		}
	}

	@Override
	public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
			throws EtBadRequestException {
		try {
			jdbcTemplate.update(SQL_UPDATE, transaction.getAmount(), transaction.getNote(), transaction.getTransactionDate(), userId, categoryId, transactionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EtBadRequestException("invalid request");
		}
		
	}

	@Override
	public void removeById(Integer userId, Integer categoryId, Integer transactionId) throws EtBadRequestException {
		int count = jdbcTemplate.update(SQL_DELETE, userId, categoryId, transactionId);
		if(count == 0) {
			throw new EtResourceNotFoundException("Transaction not found");
		}
		
	}
	
	
	private RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) -> {
		return new Transaction(rs.getInt("transaction_id"),
				rs.getInt("category_id"),
				rs.getInt("user_id"),
				rs.getDouble("amount"),
				rs.getString("note"),
				rs.getLong("transaction_date"));
	});

}
