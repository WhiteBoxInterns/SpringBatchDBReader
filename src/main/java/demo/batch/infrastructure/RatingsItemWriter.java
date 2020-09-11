package demo.batch.infrastructure;

import demo.batch.domain.Ratings;
import demo.batch.esrepository.ESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.batch.api.chunk.ItemWriter;
import java.io.Serializable;
import java.util.List;
@Service
public class RatingsItemWriter implements ItemWriter {
	@Autowired
	private ESRepository esRatingsRepository;
	
	public RatingsItemWriter() {
	}
	
	public void setEsRatingsRepository(ESRepository esRatingsRepository) {
		this.esRatingsRepository = esRatingsRepository;
	}
	
	public ESRepository getEsRatingsRepository() {
		return esRatingsRepository;
	}
	
	@Override
	public void writeItems(List<Object> items) throws Exception {
		for(Object ratings : items)
		this.esRatingsRepository.save((Ratings)ratings);
		
	}
	
	@Override
	public void open(Serializable checkpoint) throws Exception {
	
	}
	
	@Override
	public void close() throws Exception {
	
	}
	
	@Override
	public Serializable checkpointInfo() throws Exception {
		return null;
	}
}
