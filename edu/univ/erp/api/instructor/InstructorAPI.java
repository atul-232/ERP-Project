package edu.univ.erp.api.instructor;

import edu.univ.erp.service.*;
import java.util.List;
import edu.univ.erp.api.ApiException;

public class InstructorAPI {

    private final InstructorService instructorService;

    public InstructorAPI() {
        this.instructorService = new InstructorService();
        System.out.println("[DEBUG] InstructorAPI initialized");
    }

    public List<SectionInfoDTO> getMySections(int instructorUserId) {
        System.out.println("[DEBUG] getMySections called -> instructorUserId=" + instructorUserId);
        try {
            List<SectionInfoDTO> list = instructorService.getMySections(instructorUserId);
            System.out.println("[DEBUG] getMySections success -> count=" + list.size());
            return list;
        } catch (Exception e) {
            System.out.println("[DEBUG] getMySections FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve instructor sections: " + e.getMessage(), e);
        }
    }

    public GradebookDTO getGradebookData(int sectionId) {
        System.out.println("[DEBUG] getGradebookData called -> sectionId=" + sectionId);
        try {
            GradebookDTO dto = instructorService.getGradebookData(sectionId);
            System.out.println("[DEBUG] getGradebookData success -> dto=" + (dto != null));
            return dto;
        } catch (Exception e) {
            System.out.println("[DEBUG] getGradebookData FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve gradebook: " + e.getMessage(), e);
        }
    }

    public String submitGrade(int enrollmentId, String component, double score) {
        System.out.println("[DEBUG] submitGrade called -> enrollmentId=" + enrollmentId + ", component=" + component + ", score=" + score);
        try {
            instructorService.submitGrade(enrollmentId, component, score);
            String msg = "Grade submitted successfully";
            System.out.println("[DEBUG] submitGrade success -> " + msg);
            return msg;
        } catch (IllegalArgumentException e) {
            System.out.println("[DEBUG] submitGrade FAILED -> " + e.getMessage());
            throw new ApiException("Invalid grade data: " + e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("[DEBUG] submitGrade ERROR -> " + e.getMessage());
            throw new ApiException("Failed to submit grade: " + e.getMessage(), e);
        }
    }

    public String calculateFinalGradesForSection(int sectionId) {
        System.out.println("[DEBUG] calculateFinalGradesForSection called -> sectionId=" + sectionId);
        try {
            instructorService.calculateFinalGradesForSection(sectionId);
            String msg = "Final grades calculated successfully for section " + sectionId;
            System.out.println("[DEBUG] calculateFinalGradesForSection success -> " + msg);
            return msg;
        } catch (Exception e) {
            System.out.println("[DEBUG] calculateFinalGradesForSection FAILED -> " + e.getMessage());
            throw new ApiException("Failed to calculate final grades: " + e.getMessage(), e);
        }
    }

    public SectionStatsDTO getSectionStatistics(int sectionId) {
        System.out.println("[DEBUG] getSectionStatistics called -> sectionId=" + sectionId);
        try {
            SectionStatsDTO dto = instructorService.getSectionStatistics(sectionId);
            System.out.println("[DEBUG] getSectionStatistics success -> dto=" + (dto != null));
            return dto;
        } catch (Exception e) {
            System.out.println("[DEBUG] getSectionStatistics FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve section statistics: " + e.getMessage(), e);
        }
    }
}
