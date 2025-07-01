package com.example.notesservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private int patientId;
    private String content;

    public Note() {}

    public Note(int patientId, String content) {
        this.patientId = patientId;
        this.content = content;
    }
}