package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.form.StorageForm;
import com.udacity.jwdnd.course1.cloudstorage.service.db.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.db.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.db.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.db.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;

    public HomeController(FileService fileService, NoteService noteService, UserService userService, CredentialService credentialService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @ModelAttribute
    public StorageForm getStorageForm() {
        return new StorageForm();
    }
    @ModelAttribute
    public NoteForm noteForm(){
        return new NoteForm();
    }
    @ModelAttribute
    public CredentialForm credentialForm(){return new CredentialForm();}

    @GetMapping()
    public String getHomePage(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("UploadedFiles", fileService.fileList(user.getUserId()));
        model.addAttribute("SavedNotes", noteService.getNotesList(user.getUserId()));
        model.addAttribute("SavedCredentials",credentialService.getCredentialList(user.getUserId()));
        System.out.println("app enters @GetMapping in HomeController");
        return "home";
    }

    @PostMapping
    public String postHomePage(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("UploadedFiles", fileService.fileList(user.getUserId()));
        model.addAttribute("SavedNotes", noteService.getNotesList(user.getUserId()));
        model.addAttribute("SavedCredentials",credentialService.getCredentialList(user.getUserId()));
        return "home";
    }

    @PostMapping("/logout")
    public String logoutView(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}