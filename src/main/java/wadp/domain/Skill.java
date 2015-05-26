

package wadp.domain;

import java.util.ArrayList;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Skill extends AbstractPersistable<Long>{
    
    private String description;
    private ArrayList<Exercise> exercises;
    
    public Skill(String description){
        this.description=description;
        this.exercises=new ArrayList<Exercise>();
    }
    
    public void addExercise(Exercise exercise){
        this.exercises.add(exercise);
    }
    
    public ArrayList<Exercise> getExercises(){
        return this.exercises;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
