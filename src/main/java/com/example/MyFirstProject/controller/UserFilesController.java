package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.MusicMetaDate;
import com.example.MyFirstProject.service.MusicFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserFilesController {

    @Autowired
    private MusicFileService musicFileService;

    @GetMapping("{idUser}/files/search")
    public MusicMetaDate findUserFile(@PathVariable Long idUser, @RequestParam("id") Long idFile) {
        return musicFileService.findUserFile(idUser, idFile);
    }

    @GetMapping("{idUser}/files/{idFile}")
    public MusicMetaDate findUserFilePart(@PathVariable Long idUser, @PathVariable Long idFile) {
        return musicFileService.findUserFile(idUser, idFile);
    }
}
