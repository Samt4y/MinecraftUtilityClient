package com.sam.utilityclient.module;

/** Controls client target-selection reach; servers still verify interactions. */
public final class ReachModule extends Module {
    private double range = 4.5;
    public ReachModule() { super("Reach", "Extends client targeting range.", ModuleCategory.COMBAT); }
    public double range() { return range; }
    public void setRange(double value) { range = Math.clamp(value, 3.0, 6.0); }
}
