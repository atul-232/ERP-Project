package edu.univ.erp.service;

import edu.univ.erp.domain.Section;

public class SectionInfoDTO {
    private final Section section;
    private final String displayName;

    public SectionInfoDTO(Section section, String displayName) {
        System.out.println("[SectionInfoDTO] Constructor called");

        if (section == null) {
            System.out.println("[SectionInfoDTO] section is null");
        }
        this.section = section;

        if (displayName == null || displayName.isBlank()) {
            System.out.println("[SectionInfoDTO] displayName is null or empty, setting default");
            this.displayName = "Unknown Section";
        } else {
            this.displayName = displayName;
        }

        System.out.println("[SectionInfoDTO] Created -> displayName: " + this.displayName);
    }

    public Section getSection() {
        System.out.println("[SectionInfoDTO] getSection called");
        return section;
    }

    @Override
    public String toString() {
        System.out.println("[SectionInfoDTO] toString() called -> " + displayName);
        return displayName;
    }
}
