package com.sam.utilityclient.module;

public final class EspModule extends Module {
    private double range = 64.0;
    public EspModule() { super("ESP", "Draws boxes around nearby entities.", ModuleCategory.RENDER); }
    public double range() { return range; }
    public void setRange(double value) { range = Math.clamp(value, 16.0, 160.0); }
}
