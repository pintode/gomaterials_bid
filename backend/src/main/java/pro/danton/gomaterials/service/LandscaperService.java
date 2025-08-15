package pro.danton.gomaterials.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pro.danton.gomaterials.model.Landscaper;
import pro.danton.gomaterials.repository.LandscaperRepository;

@Service
public class LandscaperService {
    @Autowired
    private LandscaperRepository landscaperRepository;

    @Transactional
    public Landscaper create(Landscaper landscaper) {
        return landscaperRepository.save(landscaper);
    }

    public Optional<Landscaper> findById(Long id) {
        return landscaperRepository.findById(id);
    }

    public List<Landscaper> findAll() {
        return landscaperRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}