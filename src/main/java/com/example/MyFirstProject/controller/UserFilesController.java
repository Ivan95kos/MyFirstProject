package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.MusicMetaDate;
import com.example.MyFirstProject.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserFilesController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("{idUser}/files")
    public MusicMetaDate findUserFile(@PathVariable Long idUser, @RequestParam("id") Long idFile) {
        return fileStorageService.findUserFile(idUser, idFile);
    }
}
