package com.example.hw05;

public class Cloud {
    String cloudiness;

    public String getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(String cloudiness) {
        this.cloudiness = cloudiness;
    }

    @Override
    public String toString() {
        return "Cloud{" +
                "cloudiness='" + cloudiness + '\'' +
                '}';
    }
}
