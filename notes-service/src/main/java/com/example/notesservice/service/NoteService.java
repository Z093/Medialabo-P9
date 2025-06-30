package com.example.notesservice.service;

import com.example.notesservice.model.Note;
import com.example.notesservice.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository repository;

    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public List<Note> getNotesByPatient(String patientId) {
        return repository.findByPatientId(patientId);
    }

    public Note addNote(Note note) {
        return repository.save(note);
    }
}
