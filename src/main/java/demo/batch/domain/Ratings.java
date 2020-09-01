package demo.batch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ratings")
public class Ratings {
	@Id
	@Column(name = "tconst")
	public String tconst;
	public int averageRating;
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
