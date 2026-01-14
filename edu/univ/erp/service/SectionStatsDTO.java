package edu.univ.erp.service;

import java.util.Map;

public class SectionStatsDTO {
    private final int totalStudents;
    private final Map<String, Double> averageScores;

    public SectionStatsDTO(int totalStudents, Map<String, Double> averageScores) {
        System.out.println("[SectionStatsDTO] Constructor called");
        this.totalStudents = totalStudents;

        if (averageScores == null || averageScores.isEmpty()) {
            System.out.println("[SectionStatsDTO] averageScores is null or empty");
            this.averageScores = Map.of();
        } else {
            this.averageScores = averageScores;
        }

        System.out.println("[SectionStatsDTO] Created -> totalStudents=" 
                + this.totalStudents + ", components=" + this.averageScores.size());
    }

    public int getTotalStudents() {
        System.out.println("[SectionStatsDTO] getTotalStudents called");
        return totalStudents;
    }

    public Map<String, Double> getAverageScores() {
        System.out.println("[SectionStatsDTO] getAverageScores called");
        return averageScores;
    }
}
