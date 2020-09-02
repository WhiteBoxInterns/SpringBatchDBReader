package demo.batch.infrastructure;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ColumnRangePartitioner implements Partitioner {
	
	private JdbcOperations jdbcTemplate;
	
	private String table;
	
	private String column;
	
	/**
	 * The name of the SQL table the data are in.
	 *
	 * @param table the name of the table
	 */
	public void setTable(String table) {
		this.table = table;
	}
	
	/**
	 * The name of the column to partition.
	 *
	 * @param column the column name.
	 */
	public void setColumn(String column) {
		this.column = column;
	}
	
	/**
	 * The data source for connecting to the database.
	 *
	 * @param dataSource a {@link DataSource}
	 */
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Partition a database table assuming that the data in the column specified
	 * are uniformly distributed. The execution context values will have keys
	 * <code>minValue</code> and <code>maxValue</code> specifying the range of
	 * values to consider in each partition.
	 *
	 * @see Partitioner#partition(int)
	 */
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		int targetSize = jdbcTemplate.queryForObject("SELECT COUNT(" + column + ") from " + table, Integer.class);
		targetSize /= gridSize;
		
		Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
		int number = 0;
		int start = 0;
		int end = targetSize - 1;
		
		
		while (start <= targetSize) {
			ExecutionContext value = new ExecutionContext();
			result.put("partition" + number, value);
			
			String min = jdbcTemplate.queryForObject("SELECT " + column + " from " + table + " GROUP BY " + column + " LIMIT " +
				start + ", " + "1", String.class);
			String max = jdbcTemplate.queryForObject("SELECT " + column + " from " + table + " GROUP BY " + column + " LIMIT " +
				end + ", " + "1", String.class);
			if (end >= targetSize) {
				end = targetSize;
			}
			value.putString("minValue", "a");
			value.putString("maxValue", "b");
			start += targetSize;
			end += targetSize;
			number++;
		}
		
		return result;
	}
}