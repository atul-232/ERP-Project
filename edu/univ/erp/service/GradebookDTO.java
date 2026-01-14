package edu.univ.erp.service;

import java.util.List;

public class GradebookDTO {
    private final List<String> assessmentComponents;
    private final List<StudentGradeDTO> studentGrades;

    public GradebookDTO(List<String> assessmentComponents, List<StudentGradeDTO> studentGrades) {
        System.out.println("[GradebookDTO] Constructor called");

        if (assessmentComponents == null) {
            System.out.println("[GradebookDTO] assessmentComponents is null, initializing empty list");
        }
        if (studentGrades == null) {
            System.out.println("[GradebookDTO] studentGrades is null, initializing empty list");
        }

        this.assessmentComponents = assessmentComponents != null ? assessmentComponents : List.of();
        this.studentGrades = studentGrades != null ? studentGrades : List.of();

        System.out.println("[GradebookDTO] GradebookDTO created -> assessments=" 
                + this.assessmentComponents.size() + ", students=" + this.studentGrades.size());
    }

    public List<String> getAssessmentComponents() { 
        System.out.println("[GradebookDTO] getAssessmentComponents called"); 
        return assessmentComponents; 
    }

    public List<StudentGradeDTO> getStudentGrades() { 
        System.out.println("[GradebookDTO] getStudentGrades called"); 
        return studentGrades; 
    }
}
