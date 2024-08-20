package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;

    public LabelDTO findById(Long id) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Метка с id = " + id + " не найдена"));
        return labelMapper.map(label);
    }

    public List<LabelDTO> getAll() {
        List<Label> labels = labelRepository.findAll();
        return labels.stream()
                .map(labelMapper::map)
                .toList();
    }

    public LabelDTO create(LabelCreateDTO createDTO) {
        Label label = labelMapper.map(createDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO update(LabelUpdateDTO updateDTO, Long id) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Метка с id = " + id + " не найдена"));
        labelMapper.update(updateDTO, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }
}
