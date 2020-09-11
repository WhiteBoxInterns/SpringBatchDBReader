package demo.batch.esrepository;

import demo.batch.domain.Crew;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ESRepository extends ElasticsearchRepository<Crew, Integer> {

}
