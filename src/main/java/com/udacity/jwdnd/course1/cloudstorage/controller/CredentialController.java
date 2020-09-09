package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateNote(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        credential.setUserId(userId);
        int result;
        if (credential.getCredentialId() != null) {
            result = credentialService.updateCredential(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), credential.getPassword(), userId);
        } else {
            result = credentialService.createCredential(credential);
        }
        if (result > 0) {
            model.addAttribute("resultSuccess", true);
        } else {
            model.addAttribute("resultError", "Create or Update Credential Failed");
        }
        return "result";
    }

    @GetMapping("delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Authentication authentication, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        int result = credentialService.deleteCredential(credentialId, userId);
        if (result > 0) {
            model.addAttribute("resultSuccess", true);
        } else {
            model.addAttribute("resultError", "Delete Credential Failed");
        }
        return "result";
    }

}
