package demo.batch.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
@Document(indexName = "tconst")
public class Ratings {
	@Column(name = "tconst")
	@Field(name = "id")
	@Id
	public String tconst;
	@Column(name = "averageRating")
	@Field(name = "averageRating")
	public int averageRating;
	@Column(name = "numVotes")
	@Field(name = "numVotes")
	public int numVotes;
	
	public Ratings() {
	}
	
	public Ratings(String tconst, int averageRating, int numVotes) {
		this.tconst = tconst;
		this.averageRating = averageRating;
		this.numVotes = numVotes;
		
	}
	
	public String getTconst() {
		return tconst;
	}
	
	public void setTconst(String tconst) {
		this.tconst = tconst;
	}
	
	public int getAverageRating() {
		return averageRating;
	}
	
	public void setAverageRating(int averageRating) {
		this.averageRating = averageRating;
	}
	
	public int getNumVotes() {
		return numVotes;
	}
	
	public void setNumVotes(int numVotes) {
		this.numVotes = numVotes;
	}
	
	@Override
	public String toString() {
		return "Ratings{" +
			"tconst='" + tconst + '\'' +
			", averageRating=" + averageRating +
			", numVotes=" + numVotes +
			'}';
	}
}
