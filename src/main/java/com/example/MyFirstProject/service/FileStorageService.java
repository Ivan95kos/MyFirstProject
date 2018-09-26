package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.FileStorageException;
import com.example.MyFirstProject.exception.MyFileNotFoundException;
import com.example.MyFirstProject.model.MusicMetaDate;
import com.example.MyFirstProject.model.MyFile;
import com.example.MyFirstProject.property.FileStorageProperties;
import com.example.MyFirstProject.repository.FileRepository;
import com.example.MyFirstProject.repository.MusicMetaDateRepository;
import com.mpatric.mp3agic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MusicMetaDateRepository musicMetaDateRepository;

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

    public String storeFile(final MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        MyFile myFile = new MyFile();

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

//            MyFile dbMyFile = new MyFile(fileName, file.getContentType(), file.getBytes());

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            if (file.getContentType().equals("audio/mpeg")) {
                saveMetaDate(fileName);
            }

            myFile.setLocalAddress(fileName);
            fileRepository.save(myFile);

            return fileName;
        } catch (IOException | InvalidDataException | UnsupportedTagException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

//    public MyFile getFile(String fileId) {
//        return fileRepository.findById(fileId)
//                .orElseThrow(() -> new MyFileNotFoundException("MyFile not found with id " + fileId));
//    }

    public Resource loadFileAsResource(String fileName) {
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

    public File multipartToFile(MultipartFile file) throws IllegalStateException, IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public void saveMetaDate(String fileName) throws IOException, InvalidDataException, UnsupportedTagException {

        MusicMetaDate musicMetaDate = new MusicMetaDate();
//        musicMetaDate.setNameMusic(file.getOriginalFilename().substring(0,file.getOriginalFilename().length()- 4).trim());

        Mp3File mp3file = new Mp3File(fileName);

        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            musicMetaDate.setTrack("Track: " + id3v1Tag.getTrack());
            musicMetaDate.setArtist("Artist: " + id3v1Tag.getArtist());
            musicMetaDate.setTitle("Title: " + id3v1Tag.getTitle());
            musicMetaDate.setAlbum("Album: " + id3v1Tag.getAlbum());
            musicMetaDate.setYear("Year: " + id3v1Tag.getYear());
            musicMetaDate.setGenre("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
            musicMetaDate.setComment("Comment: " + id3v1Tag.getComment());
        }

        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            musicMetaDate.setTrack("Track: " + id3v2Tag.getTrack());
            musicMetaDate.setArtist("Artist: " + id3v2Tag.getArtist());
            musicMetaDate.setTitle("Title: " + id3v2Tag.getTitle());
            musicMetaDate.setAlbum("Album: " + id3v2Tag.getAlbum());
            musicMetaDate.setYear("Year: " + id3v2Tag.getYear());
            musicMetaDate.setGenre("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
            musicMetaDate.setComment("Comment: " + id3v2Tag.getComment());
//            System.out.println("Composer: " + id3v2Tag.getComposer());
//            System.out.println("Publisher: " + id3v2Tag.getPublisher());
//            System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
//            System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
//            System.out.println("Copyright: " + id3v2Tag.getCopyright());
//            System.out.println("URL: " + id3v2Tag.getUrl());
//            System.out.println("Encoder: " + id3v2Tag.getEncoder());
        }

        musicMetaDateRepository.save(musicMetaDate);
    }
}
