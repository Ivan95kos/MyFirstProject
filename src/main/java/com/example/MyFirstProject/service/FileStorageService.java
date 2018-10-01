package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.FileStorageException;
import com.example.MyFirstProject.exception.MyFileNotFoundException;
import com.example.MyFirstProject.model.MusicMetaDate;
import com.example.MyFirstProject.model.MyFile;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.property.FileStorageProperties;
import com.example.MyFirstProject.repository.MusicMetaDateRepository;
import com.example.MyFirstProject.repository.MyFileRepository;
import com.example.MyFirstProject.repository.UserRepository;
import com.mpatric.mp3agic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class FileStorageService {

    private final static String AUDIO_EXTENSIONS = "audio/mpeg";
    private final static String EMPTY_LINE = "";

    @Autowired
    private MyFileRepository myFileRepository;

    private final Path fileStorageLocation;

    @Autowired
    private MusicMetaDateRepository musicMetaDateRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public FileStorageService(final FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(final MultipartFile file, final String username) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String location = fileStorageLocation.toFile().getAbsolutePath() + "/" + fileName;

        MyFile myFile = new MyFile();
        User user = userRepository.findOneByUsername(username);

        myFile.setLocalAddress(location);
        myFile.setUser(user);

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

//        if (!myFileRepository.existsByLocalAddress(location)) {

        if (Objects.equals(file.getContentType(), AUDIO_EXTENSIONS)) {
                myFile.setMusicMetaDate(saveMetaDate(fileName, location));
            }
            myFileRepository.save(myFile);

//        } else {
//            MyFile saveFile = myFileRepository.findByLocalAddress(location);
//
//            MusicMetaDate musicMetaDate = saveFile.getMusicMetaDate();
//
//            myFile.setMusicMetaDate(musicMetaDate);
//
//            myFileRepository.save(myFile);
//        }

        user.getFiles().add(myFile);

        userRepository.saveAndFlush(user);

        return fileName;
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

        List<MyFile> listIdMusics = myFileRepository.findByUserId(idUser);

        for (MyFile myFile : listIdMusics) {
            if (myFile.getMusicMetaDate().getId().equals(idFile)) {
                return myFile.getMusicMetaDate();
            }
        }

//        if (listIdMusics.size()>idFile) {
//            int idFiles = idFile.intValue();
//
//            MyFile myFile = listIdMusics.get(idFiles);
//
//            return myFile.getMusicMetaDate();
//        }

        return new MusicMetaDate();
    }

    public MusicMetaDate saveMetaDate(final String fileName, final String location) {

        MusicMetaDate musicMetaDate = new MusicMetaDate();
        musicMetaDate.setNameMusic(fileName.substring(0, fileName.length() - 4).trim());

        Mp3File mp3file = null;
        try {
            mp3file = new Mp3File(location);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.getMessage();
        }

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

        return musicMetaDateRepository.save(musicMetaDate);
    }
}
