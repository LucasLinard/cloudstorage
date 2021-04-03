package com.udacity.jwdnd.course1.cloudstorage.controller.db;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.db.File;
import com.udacity.jwdnd.course1.cloudstorage.model.form.StorageForm;
import com.udacity.jwdnd.course1.cloudstorage.service.db.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.db.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;

@Controller
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }


    @GetMapping("/file")
    public String getFileList(@ModelAttribute("storageForm") StorageForm storageForm, Model model) {
        model.addAttribute("files", fileService.fileList(storageForm.getUserId()));
        return "home";
    }


    @PostMapping("/file")
    public ModelAndView uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth, StorageForm storageForm, ModelMap attributes) throws IOException {
        User user = userService.getUserByUsername(auth.getName());
        System.out.println("The size of the file uploading is: " + fileUpload.getSize());
        if (fileUpload.isEmpty()) {
            attributes.addAttribute("UploadErrorBool", true);
            attributes.addAttribute("UploadError", "Please select a file to upload! ");
        } else if (fileService.getFile(fileUpload.getOriginalFilename(), user.getUserId()) != null) {
            attributes.addAttribute("UploadErrorBool", true);
            attributes.addAttribute("UploadError", "There is a file by that name already, please upload another file, or rename your file! ");
        } else if (fileUpload.getSize() > 10485760) {
            attributes.addAttribute("UploadErrorBool", true);
            attributes.addAttribute("UploadError", "Sorry, the file you are trying to upload, exceeds the 10 MB limit!");
        } else {
            this.fileService.addFile(fileService.convertMpFile(fileUpload, user.getUserId()));
            attributes.addAttribute("UploadedFiles", fileService.fileList(user.getUserId()));
            attributes.addAttribute("UploadSuccessBool", true);
            attributes.addAttribute("UploadSuccess", "Your file has been uploaded successfully! ");

        }
        return new ModelAndView("forward:/result", attributes);
    }


    @GetMapping("/delete")
    public ModelAndView deleteFile(StorageForm storageForm, ModelMap attributes, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName());
        for (File file : this.fileService.fileList(user.getUserId())) {
            if (file.getFilename().equals(storageForm.getFileName())) {
                this.fileService.removeFile(user.getUserId(), file.getFilename());
                attributes.addAttribute("UploadSuccess", "Your file has been deleted succesfully");
            }

        }
        attributes.addAttribute("UploadedFiles", fileService.fileList(user.getUserId()));
        return new ModelAndView("forward:/result", attributes);

    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> fileDownload(@RequestParam("fileName") String fileName, Authentication authentication) {

        User user = userService.getUserByUsername(authentication.getName());
        File file = fileService.getFile(fileName, user.getUserId());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

}
