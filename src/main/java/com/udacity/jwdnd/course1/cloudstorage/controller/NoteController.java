package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;
    private UserService userService;
    public NoteController(NoteService noteService, UserService userService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping
    public String createOrUpdateNote(Authentication authentication, @ModelAttribute Note note, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        note.setUserId(userId);
        int result;
        if (note.getNoteId() != null) {
            result = this.noteService.updateNote(note.getNoteTitle(), note.getNoteDescription(), note.getNoteId(), note.getUserId());
        } else {
            result = this.noteService.addNote(note);
        }
        //model.addAttribute("resultSuccess", true);
        //model.addAttribute("notes", noteService.getNotes(userId));
        if (result > 0) {
            model.addAttribute("resultSuccess", true);
        } else {
            model.addAttribute("resultError", "Create or Update Note Failed");
        }
        return "result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication authentication, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        int result = noteService.deleteNote(noteId, userId);
        if (result > 0) {
            model.addAttribute("resultSuccess", true);
        } else {
            model.addAttribute("resultError", "Delete Note Failed");
        }
        //model.addAttribute("notes", noteService.getNotes(userId));
        return "result";
    }
}
