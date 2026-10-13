package com.sam.utilityclient.module;

public final class HitboxesModule extends Module {
    private double size = .3;
    public HitboxesModule() { super("Hitboxes", "Expands client-side entity targeting boxes.", ModuleCategory.COMBAT); }
    public double size() { return size; }
    public void setSize(double value) { size = Math.clamp(value, .1, 4.0); }
}
