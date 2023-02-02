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

import com.deepak.ExpenseTracker.domain.Category;
import com.deepak.ExpenseTracker.exceptions.EtBadRequestException;
import com.deepak.ExpenseTracker.exceptions.EtResourceNotFoundException;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository{
	
	private static final String SQL_CREATE = "insert into et_categories(user_id, title, description) values(?,?,?)";
	
	private static final String SQL_FIND_BY_ID = "select c.category_id, c.user_id, c.title, c.description,"
			+ "coalesce (sum(t.amount), 0) as total_expense "
			+ "from et_transactions t right outer join et_categories c on c.category_id = t.category_id "
			+ "where c.user_id = ? and c.category_id = ? group by c.category_id";
	
	private static final String FIND_ALL =	"select c.category_id, c.user_id, c.title, c.description,"
			+ "coalesce (sum(t.amount), 0) as total_expense "
			+ "from et_transactions t right outer join et_categories c on c.category_id = t.category_id "
			+ "where c.user_id = ? group by c.category_id";	
	
	private static final String SQL_UPDATE = "update et_categories set title=?, description=?"
			+ " where user_id=? and category_id=?";
	
	private static final String SQL_DEL_CATEGORY = "delete from et_categories where user_id=? and category_id=?";
	
	private static final String SQL_DEL_ALL_TRANSACTIONS = "delete from et_transactions where category_id=?";
	
 	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<Category> findAll(Integer userId) throws EtResourceNotFoundException {
		return jdbcTemplate.query(FIND_ALL, categoryRowMapper, userId);
	}

	@Override
	public Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
		try {
			return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, categoryRowMapper, userId, categoryId );
		} catch (Exception e) {
			System.out.println(e);
			throw new EtResourceNotFoundException("Category not found");
		}
	}

	@Override
	public Integer create(Integer userId, String title, String description) throws EtBadRequestException {
		try {
			KeyHolder keyholder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection->{
				PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
				//System.out.println(userId+" "+title+" "+description);
				ps.setInt(1, userId);
				ps.setString(2, title);
				ps.setString(3, description);
				return ps;
			}, keyholder);
			return (int) keyholder.getKey().longValue();
		} catch (Exception e) {
			throw new EtBadRequestException("Invalid Request");
		}
	}

	@Override
	public void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
		try {
			jdbcTemplate.update(SQL_UPDATE, category.getTitle(), category.getDescription(), userId, categoryId);
		} catch (Exception e) {
			throw new EtBadRequestException("Invalid request");
		}
		
	}

	@Override
	public void removeById(Integer userId, Integer categoryId) {
		this.removeAllCatTransactions(categoryId);
		jdbcTemplate.update(SQL_DEL_CATEGORY, userId, categoryId);
		
	}
	
	private void removeAllCatTransactions(Integer categoryId) {
		jdbcTemplate.update(SQL_DEL_ALL_TRANSACTIONS, categoryId);
	}
	
	private RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> {
		return new Category(rs.getInt("user_id"),
				rs.getInt("category_id"),				
				rs.getString("title"),
				rs.getString("description"),
				rs.getDouble("total_expense"));
	});

}
