package com.example.assessmentservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssessmentResult {
    private String patientId;
    private String riskLevel;
}


