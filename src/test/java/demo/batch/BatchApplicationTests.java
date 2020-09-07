package demo.batch;

import demo.batch.infrastructure.ColumnRangePartitioner;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BatchApplicationTests {
	@Autowired
	private DataSource dataSource;

	@Test
	void partionerTest() {
		ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();

		columnRangePartitioner.setColumn("tconst");
		columnRangePartitioner.setDataSource(this.dataSource);
		columnRangePartitioner.setTable("crew");

		Map<String, ExecutionContext> map = columnRangePartitioner.partition(4);
		System.out.println(map);
		assertEquals(4, map.size());

	}
	
}
