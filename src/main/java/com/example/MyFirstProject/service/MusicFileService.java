package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.exception.FileStorageException;
import com.example.MyFirstProject.exception.MyFileNotFoundException;
import com.example.MyFirstProject.model.MusicMetaDate;
import com.example.MyFirstProject.model.MyMusicFile;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.repository.MusicMetaDateRepository;
import com.example.MyFirstProject.repository.MyMusicFileRepository;
import com.mpatric.mp3agic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class MusicFileService {

    private final static String AUDIO_EXTENSIONS = "audio/mpeg";
    private final static String EMPTY_LINE = "";

    @Autowired
    private MyMusicFileRepository myMusicFileRepository;

    @Autowired
    private MusicMetaDateRepository musicMetaDateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Path fileStorageLocation;

    public MyMusicFile storeMusicFile(final MultipartFile file, final User user) {

        if (!Objects.equals(file.getContentType(), AUDIO_EXTENSIONS)) {
            throw new CustomException("message.nonMusic", HttpStatus.BAD_REQUEST);
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

        String location = fileStorageLocation.toFile().getAbsolutePath() + "/" + fileName;

        MyMusicFile myMusicFile = new MyMusicFile();

        myMusicFile.setFileName(fileName);
        myMusicFile.setLocalAddress(location);
        myMusicFile.setContentType(file.getContentType());
        myMusicFile.setUser(user);

        myMusicFileRepository.save(myMusicFile);

        user.getFiles().add(myMusicFile);

        userService.saveAndFlush(user);

        return myMusicFile;
    }

    public Resource loadFileAsResource(final String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Set<MusicMetaDate> findFile(final String fileName) {

        Iterable<MusicMetaDate> musicMetaDates = musicMetaDateRepository.findAll();

        Set<MusicMetaDate> found = new HashSet<>();

        if (EMPTY_LINE.equals(fileName)) {
            found.addAll(musicMetaDateRepository.findAll());
            return found;
        }

        for (MusicMetaDate musicMetaDate : musicMetaDates) {
            if (musicMetaDate.getNameMusic().contains(fileName)) {
                found.add(musicMetaDate);
            }
        }

        return found;
    }

    public MusicMetaDate findUserFile(final Long idUser, final Long idFile) {

        List<MyMusicFile> listIdMusics = myMusicFileRepository.findByUserId(idUser);

        for (MyMusicFile myMusicFile : listIdMusics) {
            if (myMusicFile.getMusicMetaDate().getId().equals(idFile)) {
                return myMusicFile.getMusicMetaDate();
            }
        }

        return new MusicMetaDate();
    }

    public MyMusicFile storeMp3FileToFile(MyMusicFile myMusicFile) {
        Mp3File mp3file = null;
        try {
            mp3file = new Mp3File(myMusicFile.getLocalAddress());
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.getMessage();
        }
        myMusicFile.setMp3File(mp3file);

        return myMusicFile;
    }

    public void saveMetaDateMusicFile(final MyMusicFile myMusicFile) {

        final MusicMetaDate musicMetaDate = new MusicMetaDate();
        musicMetaDate.setNameMusic(myMusicFile.getFileName().substring(0, myMusicFile.getFileName().length() - 4).trim());

        Mp3File mp3file = myMusicFile.getMp3File();

        if (Objects.requireNonNull(mp3file).hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            musicMetaDate.setTrack(id3v1Tag.getTrack());
            musicMetaDate.setArtist(id3v1Tag.getArtist());
            musicMetaDate.setTitle(id3v1Tag.getTitle());
            musicMetaDate.setAlbum(id3v1Tag.getAlbum());
            musicMetaDate.setYear(id3v1Tag.getYear());
            musicMetaDate.setGenre(id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
            musicMetaDate.setComment(id3v1Tag.getComment());
        }

        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            musicMetaDate.setTrack(id3v2Tag.getTrack());
            musicMetaDate.setArtist(id3v2Tag.getArtist());
            musicMetaDate.setTitle(id3v2Tag.getTitle());
            musicMetaDate.setAlbum(id3v2Tag.getAlbum());
            musicMetaDate.setYear(id3v2Tag.getYear());
            musicMetaDate.setGenre(id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
            musicMetaDate.setComment(id3v2Tag.getComment());
        }

        musicMetaDateRepository.save(musicMetaDate);

        myMusicFile.setMusicMetaDate(musicMetaDate);

        myMusicFileRepository.saveAndFlush(myMusicFile);
    }
}
