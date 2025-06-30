package com.example.notesservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String patientId;
    private String content;

    public Note() {}

    public Note(String patientId, String content) {
        this.patientId = patientId;
        this.content = content;
    }
}