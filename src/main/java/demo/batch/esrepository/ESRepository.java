package demo.batch.esrepository;

import demo.batch.domain.Ratings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ESRepository extends ElasticsearchRepository<Ratings, Integer> {

}
