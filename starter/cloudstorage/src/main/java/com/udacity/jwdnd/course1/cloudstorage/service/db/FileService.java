package com.udacity.jwdnd.course1.cloudstorage.service.db;

import com.udacity.jwdnd.course1.cloudstorage.mapper.db.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.db.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
  private final FileMapper fileMapper;

  public FileService(FileMapper fileMapper) {
    this.fileMapper = fileMapper;
  }

  public void addFile(File file) {
    fileMapper.insert(file);
  }

  public void removeFile(Integer userId, String fileName) {
    fileMapper.delete(fileName, userId);
  }

  public File convertMpFile(MultipartFile mpFile, Integer userId) throws IOException {
    File file =
        new File(
            mpFile.getOriginalFilename(),
            mpFile.getContentType(),
            mpFile.getSize() + "",
            userId,
            mpFile.getBytes());
    return file;
  }

  public File getFile(String fileName, Integer userId) {
    return this.fileMapper.getFile(fileName, userId);
  }

  public List<File> fileList(Integer userId) {
    return this.fileMapper.getFiles(userId);
  }
}
