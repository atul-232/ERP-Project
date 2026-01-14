package edu.univ.erp.domain;

public class Settings {

    private String settingKey;
    private String settingValue;

    public Settings() {
        System.out.println("[DEBUG] Settings() default constructor called");
    }

    public Settings(String settingKey, String settingValue) {
        System.out.println("[DEBUG] Settings(full) constructor called -> key=" + settingKey + ", value=" + settingValue);
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        System.out.println("[DEBUG] setSettingKey -> " + settingKey);
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        System.out.println("[DEBUG] setSettingValue -> " + settingValue);
        this.settingValue = settingValue;
    }
}
