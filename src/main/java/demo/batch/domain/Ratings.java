package demo.batch.domain;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Ratings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
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
}
