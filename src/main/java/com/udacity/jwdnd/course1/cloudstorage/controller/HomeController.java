package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileService fileService;

    public HomeController(UserService userService, FileService fileService, CredentialService credentialService, NoteService noteService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        model.addAttribute("credentials", this.credentialService.getCredentials(userId));
        model.addAttribute("notes", this.noteService.getNotes(userId));
        model.addAttribute("files", this.fileService.getFiles(userId));
        return "home";
    }
}
