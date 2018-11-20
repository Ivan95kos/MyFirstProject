package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.exception.FileStorageException;
import com.example.MyFirstProject.model.MyMusicFile;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.repository.MusicMetaDateRepository;
import com.example.MyFirstProject.repository.MyMusicFileRepository;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.Mp3File;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class MusicFileServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private MyMusicFileRepository myMusicFileRepository;

    @Mock
    private MusicMetaDateRepository musicMetaDateRepository;

    @Mock
    private Path fileStorageLocation;

    @Mock
    private Mp3File mp3File;

    @InjectMocks
    private MusicFileService musicFileService;

    @Test
    public void storeMusicFile() {

        User user = new User("ivan", "ivan@gmail.com", "password");

        MockMultipartFile firstFile = new MockMultipartFile(
                "data",
                "Фліт.mp3",
                "audio/mpeg",
                "some".getBytes());

        Mockito.when(fileStorageLocation.toFile()).
                thenReturn(new File("uploads"));

        String location = fileStorageLocation.toFile().getAbsolutePath() + "/" + firstFile.getOriginalFilename();

        Mockito.when(fileStorageLocation.resolve(firstFile.getOriginalFilename())).
                thenReturn(Paths.get(location));

        MyMusicFile myMusicFile = musicFileService.storeMusicFile(firstFile, user);

        Assert.assertEquals(location, myMusicFile.getLocalAddress());
        Assert.assertEquals(firstFile.getOriginalFilename(), myMusicFile.getFileName());
        Assert.assertEquals(firstFile.getContentType(), myMusicFile.getContentType());
        Assert.assertEquals(user, myMusicFile.getUser());

        Mockito.verify(myMusicFileRepository, Mockito.times(1)).save(myMusicFile);
        Mockito.verify(userService, Mockito.times(1)).saveAndFlush(user);
    }

    @Test(expected = CustomException.class)
    public void storeMusicFileFailTestNonMusic() {

        User user = new User("ivan", "ivan@gmail.com", "password");

        MockMultipartFile firstFile = new MockMultipartFile(
                "data",
                "Фліт.jpeg",
                "image/jpeg",
                "some".getBytes());

        musicFileService.storeMusicFile(firstFile, user);
    }

    @Test(expected = FileStorageException.class)
    public void storeMusicFileFailTestInvalidCharacters() {

        User user = new User("ivan", "ivan@gmail.com", "password");

        MockMultipartFile firstFile = new MockMultipartFile(
                "data",
                "Фліт..mp3",
                "audio/mpeg",
                "some".getBytes());

        musicFileService.storeMusicFile(firstFile, user);
    }

    @Test
    public void saveMetaDateMusicFile() {

        User user = new User("ivan", "ivan@gmail.com", "password");

        ID3v1 id3v1Tag = new ID3v1Tag();

        mp3File.setId3v1Tag(id3v1Tag);

        MyMusicFile myMusicFile = new MyMusicFile();

        myMusicFile.setId(111L);
        myMusicFile.setFileName("Фліт.mp3");
        myMusicFile.setLocalAddress("/Users/ivankosovych/IdeaProjects/MyFirstProject/uploads/Фліт.mp3");
        myMusicFile.setContentType("audio/mpeg");
        myMusicFile.setMp3File(mp3File);
        myMusicFile.setUser(user);

        musicFileService.saveMetaDateMusicFile(myMusicFile);

        Mockito.verify(musicMetaDateRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        Mockito.verify(myMusicFileRepository, Mockito.times(1)).saveAndFlush(myMusicFile);

    }
}