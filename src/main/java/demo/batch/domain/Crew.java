package demo.batch.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.*;

@Entity
@Table(name = "crew")
@Document(indexName = "crew")
public class Crew {
	@Column(name = "tconst")
	@Field(name = "id")
	@Id
	public String tconst;
	@Column(name = "directors")
	@Field(name = "directors")
	public String directors;
	
	public Crew(String tconst, String writers, String directors) {
		this.tconst = tconst;
		this.writers = writers;
		this.directors = directors;
	}
	
	public Crew() {
	}
	
	public String getTconst() {
		return tconst;
	}
	
	public void setTconst(String tconst) {
		this.tconst = tconst;
	}
	
	public String getDirectors() {
		return directors;
	}
	
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	
	public String getWriters() {
		return writers;
	}
	
	public void setWriters(String writers) {
		this.writers = writers;
	}
	
	@Column(name = "writers")
	@Field(name = "writers")
	public String writers;
	
	
}
