package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {
    @Autowired
    private ThemeRepository themeRepository;

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
