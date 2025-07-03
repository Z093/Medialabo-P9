package com.example.notesservice.controller;

import com.example.notesservice.model.Note;
import com.example.notesservice.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Note>> getByPatient(@PathVariable int patientId) {
        return ResponseEntity.ok(service.getNotesByPatient(patientId));
    }

    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Note note) {
        return ResponseEntity.ok(service.addNote(note));
    }


}
