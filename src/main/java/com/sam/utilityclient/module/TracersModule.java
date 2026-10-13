package com.sam.utilityclient.module;

public final class TracersModule extends Module {
    private double range = 96.0;
    public TracersModule() { super("Tracers", "Draws lines from you to nearby entities.", ModuleCategory.RENDER); }
    public double range() { return range; }
    public void setRange(double value) { range = Math.clamp(value, 16.0, 160.0); }
}
