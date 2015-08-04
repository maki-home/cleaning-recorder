package cleaning.type;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CleaningTypeService {
    @Autowired
    CleaningTypeRepository cleaningTypeRepository;

    public List<CleaningType> findAll() {
        return cleaningTypeRepository.findAll();
    }
}
