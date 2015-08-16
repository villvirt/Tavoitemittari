
package wadp.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wadp.domain.GradeLevel;
import wadp.repository.GradeLevelRepository;

@Service
public class GradeLevelService {
    
    @Autowired
    private GradeLevelRepository levelRepository;
    
    @Transactional
    public void addGradeLevel(GradeLevel level){
        levelRepository.save(level);
    }

    public GradeLevel findGradeLevelById(Long id) {
        return levelRepository.findOne(id);
    }
    
}
