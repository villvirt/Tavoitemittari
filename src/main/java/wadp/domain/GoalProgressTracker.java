
package wadp.domain;

import java.util.HashMap;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class GoalProgressTracker extends AbstractPersistable<Long>{
    
    private boolean ready;
    private HashMap<Skill, Boolean> skills;
    
    public GoalProgressTracker(Goal goal){
        this.ready=false;
        skills=new HashMap<Skill, Boolean>();
        for (Skill skill : goal.getSkills()) {
            skills.put(skill, false);
        }
    }
    
    public void setSkillReady(Skill skill){
        skills.put(skill, true);
        boolean allReady = true;
        for(boolean goalStatus : skills.values()){
            if(!goalStatus){
                allReady = false;
                break;
            }
        }
        if(allReady) this.ready=true;
    }
    
    public void setSkillUnready(Skill skill){
        skills.put(skill, false);
    }

    public boolean getStatus() {
        return ready;
    }

    public void setStatus(boolean status) {
        this.ready = status;
    }

    public HashMap<Skill, Boolean> getSkills() {
        return skills;
    }

    public void setSkills(HashMap<Skill, Boolean> skills) {
        this.skills = skills;
    }
    
    
    
}
