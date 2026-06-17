package com.gdgocbu.tabulation.backend.models;

public enum PageantStatus {
    PREPARATION("#FFC107"),
    ONGOING("#2196F3"),
    FINALIZING("#FF9800"),
    CLOSED("#4CAF50");

    private final String color;

    PageantStatus(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
