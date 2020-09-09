package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    private List<Note> notes;
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
        this.notes = new ArrayList();
    }

    public int addNote(Note note) {
        return this.noteMapper.addNote(note);
        //this.notes.add(note);
    }

    public List<Note> getNotes(Integer userId) {
        return this.noteMapper.getNotes(userId);
        //return this.notes;
    }

    public int deleteNote(Integer noteId, Integer userId) {
        return this.noteMapper.deleteById(noteId, userId);
    }

    public int updateNote(String noteTitle, String noteDescription, Integer noteId, Integer userId) {
        return this.noteMapper.updateNote(noteTitle, noteDescription, noteId, userId);
    }
}
