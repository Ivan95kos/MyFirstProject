package com.example.MyFirstProject.controller;


import com.example.MyFirstProject.model.MusicMetaDate;
import com.example.MyFirstProject.model.MyMusicFile;
import com.example.MyFirstProject.payload.UploadFileResponse;
import com.example.MyFirstProject.service.MusicFileService;
import com.example.MyFirstProject.service.MusicMetaDateService;
import com.example.MyFirstProject.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private MusicMetaDateService musicMetaDateService;

    @Autowired
    private MusicFileService musicFileService;

    @Autowired
    private UserService userService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") final MultipartFile file, final Authentication authentication) {
        MyMusicFile myMusicFile = musicFileService.storeMusicFile(file, userService.findByUsername(authentication.getName()));

        musicFileService.saveMetaDateMusicFile(musicFileService.storeMp3FileToFile(myMusicFile));

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/downloadFile/")
                .path(myMusicFile.getFileName())
                .toUriString();

        return new UploadFileResponse(myMusicFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") final MultipartFile[] files, final Authentication authentication) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, authentication))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String fileName, final HttpServletRequest request) {
        // Load file as Resource
        final Resource resource = musicFileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/search")
    public Set<MusicMetaDate> search(@RequestParam("fileName") final String fileName) {
        return musicFileService.findFile(fileName);
    }

    @GetMapping
    public Page<MusicMetaDate> getMusicMetaDate(final Pageable pageable) {
        return musicMetaDateService.getAll(pageable);
    }
}
