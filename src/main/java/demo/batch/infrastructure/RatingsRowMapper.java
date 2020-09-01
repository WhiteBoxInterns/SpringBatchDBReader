package demo.batch.infrastructure;


import demo.batch.domain.Ratings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingsRowMapper implements RowMapper<Ratings> {
	@Override
	public Ratings mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return new Ratings(resultSet.getString("tconst"),
			resultSet.getInt("averageRating"),
			resultSet.getInt("numVotes"));
	}
}
