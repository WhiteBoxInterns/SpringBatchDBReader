package demo.batch.infrastructure;


import demo.batch.domain.Crew;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CrewRowMapper implements RowMapper<Crew> {
	@Override
	public Crew mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return new Crew(resultSet.getString("tconst"),
			resultSet.getString("directors"),
			resultSet.getString("writers"));
	}
}
