package com.sam.utilityclient.module;

public enum ModuleCategory {
    MOVEMENT("Movement"), COMBAT("Combat"), PLAYER("Player"), WORLD("World"), RENDER("Render");

    private final String label;
    ModuleCategory(String label) { this.label = label; }
    public String label() { return label; }
}
