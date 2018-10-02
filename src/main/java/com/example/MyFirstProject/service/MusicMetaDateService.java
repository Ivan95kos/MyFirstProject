package com.example.MyFirstProject.service;

import com.example.MyFirstProject.model.MusicMetaDate;
import com.example.MyFirstProject.repository.MusicMetaDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MusicMetaDateService {

    @Autowired
    private MusicMetaDateRepository musicMetaDateRepository;

    public Page<MusicMetaDate> getAll(Pageable pageable){
       return musicMetaDateRepository.findAll(pageable);
    }

}
