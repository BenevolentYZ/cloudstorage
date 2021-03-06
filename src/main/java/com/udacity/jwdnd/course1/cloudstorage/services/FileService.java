package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getAllUserFiles(userId);
    }

    public boolean isFileNameAvailable(String fileName, Integer userId) {
        return fileMapper.getFileByName(fileName, userId) == null;
    }

    public int uploadFile(Integer userId, MultipartFile fileUpload) {
        InputStream inputStream;
        byte[] data = new byte[0];
        try {
            inputStream = fileUpload.getInputStream();
            data = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data.length > 0) {
            File file = new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), String.valueOf(fileUpload.getSize()), userId, data);
            return this.fileMapper.uploadFile(file);
        } else {
            return 0;
        }
    }

    public File getFileByFileId(Integer fileId, Integer userId) {
        return fileMapper.getFile(fileId, userId);
    }

    public int deleteFile(Integer fileId, Integer userId) {
        return fileMapper.deleteFileById(fileId, userId);
    }
}
