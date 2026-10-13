package com.sam.utilityclient.gui;

import com.sam.utilityclient.UtilityClient;
import com.sam.utilityclient.module.*;
import com.sam.utilityclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/** Category-based click GUI: toggle cards with left-click and edit settings with right-click. */
public final class ClickGuiScreen extends Screen {
    private static final int GUI_W = 760, GUI_H = 420, SIDE_W = 154;
    private final @Nullable Screen parent;
    private ModuleCategory category = ModuleCategory.MOVEMENT;
    private @Nullable Module selected;
    private int x, y;
    public ClickGuiScreen(@Nullable Screen parent) { super(Text.literal("Utility Client")); this.parent = parent; }
    @Override protected void init() { x = (width - GUI_W) / 2; y = (height - GUI_H) / 2; }

    @Override public void render(DrawContext c, int mx, int my, float delta) {
        c.fill(0, 0, width, height, 0xA0000000); c.fill(x - 5, y - 5, x + GUI_W + 5, y + GUI_H + 5, 0x55000000);
        c.fill(x, y, x + GUI_W, y + GUI_H, 0xFF111722); c.fill(x, y, x + SIDE_W, y + GUI_H, 0xFF161E2B);
        c.fill(x, y, x + SIDE_W, y + 72, 0xFF1C2A3D); c.fill(x + SIDE_W, y, x + GUI_W, y + 52, 0xFF161E2B);
        c.drawTextWithShadow(textRenderer, "UTILITY", x + 18, y + 18, 0xFF74BEFF); c.drawTextWithShadow(textRenderer, "CLIENT", x + 18, y + 33, 0xFFF4F7FC);
        c.drawTextWithShadow(textRenderer, "v1.0.0", x + 18, y + 53, 0xFF7F8B9E);
        c.drawTextWithShadow(textRenderer, category.label().toUpperCase(), x + SIDE_W + 22, y + 19, 0xFFF4F7FC);
        c.drawTextWithShadow(textRenderer, "Left-click: toggle   •   Right-click: configure", x + SIDE_W + 22, y + 34, 0xFF8290A5);
        int catY = y + 92;
        for (ModuleCategory value : ModuleCategory.values()) {
            boolean active = value == category; if (active) c.fill(x + 10, catY - 6, x + SIDE_W - 10, catY + 20, 0xFF274C71);
            c.drawTextWithShadow(textRenderer, value.label(), x + 24, catY, active ? 0xFFFFFFFF : 0xFF9BA9BB); catY += 37;
        }
        c.drawTextWithShadow(textRenderer, "RIGHT SHIFT to close", x + 17, y + GUI_H - 24, 0xFF69778C);
        List<Module> modules = modules();
        for (int i = 0; i < modules.size(); i++) card(c, modules.get(i), x + SIDE_W + 20 + (i % 2) * 192, y + 70 + (i / 2) * 88, mx, my);
        inspector(c);
    }
    private List<Module> modules() { return UtilityClient.MODULES.all().stream().filter(m -> m.category() == category).toList(); }
    private void card(DrawContext c, Module m, int cx, int cy, int mx, int my) {
        boolean hover = inside(mx,my,cx,cy,176,74); c.fill(cx,cy,cx+176,cy+74,m.enabled()?0xFF193C36:0xFF1B2432);
        if (hover) c.fill(cx,cy,cx+3,cy+74,0xFF74BEFF);
        c.drawTextWithShadow(textRenderer,m.name(),cx+12,cy+12,0xFFF4F7FC); wrapped(c,m.description(),cx+12,cy+29,145,0xFF93A2B5);
        c.fill(cx+134,cy+11,cx+163,cy+24,m.enabled()?0xFF3DBE83:0xFF3B4657); c.drawCenteredTextWithShadow(textRenderer,m.enabled()?"ON":"OFF",cx+148,cy+14,0xFFFFFFFF);
    }
    private void inspector(DrawContext c) {
        int ix=x+SIDE_W+412; c.fill(ix,y+70,x+GUI_W-18,y+GUI_H-20,0xFF171F2C); c.drawTextWithShadow(textRenderer,"MODULE SETTINGS",ix+14,y+86,0xFF74BEFF);
        if(selected==null){ c.drawTextWithShadow(textRenderer,"Right-click a module",ix+14,y+125,0xFFF4F7FC); c.drawTextWithShadow(textRenderer,"to configure it.",ix+14,y+142,0xFF93A2B5); return; }
        c.drawTextWithShadow(textRenderer,selected.name(),ix+14,y+118,0xFFF4F7FC); wrapped(c,selected.description(),ix+14,y+137,156,0xFF93A2B5);
        if(selected instanceof FlyModule m) setting(c,ix,"Flight speed",String.format("%.1fx",m.speed()));
        else if(selected instanceof SpeedModule m) setting(c,ix,"Speed multiplier",String.format("%.1fx",m.multiplier()));
        else if(selected instanceof ReachModule m) setting(c,ix,"Target range",String.format("%.1f blocks",m.range()));
        else if(selected instanceof AutoClickModule m) setting(c,ix,"Click rate",m.clicksPerSecond()+" CPS");
        else c.drawTextWithShadow(textRenderer,"No adjustable settings.",ix+14,y+196,0xFF93A2B5);
    }
    private void setting(DrawContext c,int ix,String label,String value){ int ry=y+196; c.drawTextWithShadow(textRenderer,label,ix+14,ry,0xFFF4F7FC); c.drawTextWithShadow(textRenderer,value,ix+14,ry+19,0xFF74BEFF); c.fill(ix+14,ry+50,ix+76,ry+78,0xFF273449); c.fill(ix+92,ry+50,ix+154,ry+78,0xFF273449); c.drawCenteredTextWithShadow(textRenderer,"−",ix+45,ry+59,0xFFF4F7FC); c.drawCenteredTextWithShadow(textRenderer,"+",ix+123,ry+59,0xFFF4F7FC); }
    @Override public boolean mouseClicked(Click click,boolean doubled){ double mx=click.x(),my=click.y();
        for(int i=0;i<ModuleCategory.values().length;i++) if(inside(mx,my,x+10,y+86+i*37,SIDE_W-20,30)){ category=ModuleCategory.values()[i]; selected=null; return true; }
        List<Module> modules=modules(); for(int i=0;i<modules.size();i++){ int cx=x+SIDE_W+20+(i%2)*192,cy=y+70+(i/2)*88; if(inside(mx,my,cx,cy,176,74)){ if(click.button()==0)modules.get(i).toggle(MinecraftClient.getInstance()); if(click.button()==1)selected=modules.get(i); return true; }}
        int ix=x+SIDE_W+412,sy=y+246; if(selected!=null&&inside(mx,my,ix+14,sy,140,28)){ boolean plus=mx>=ix+92; if(selected instanceof FlyModule m)m.setSpeed(m.speed()+(plus?.1:-.1)); else if(selected instanceof SpeedModule m)m.setMultiplier(m.multiplier()+(plus?.1:-.1)); else if(selected instanceof ReachModule m)m.setRange(m.range()+(plus?.5:-.5)); else if(selected instanceof AutoClickModule m)m.setClicksPerSecond(m.clicksPerSecond()+(plus?1:-1)); return true;} return super.mouseClicked(click,doubled); }
    @Override public void close(){ if(client!=null)client.setScreen(parent); } @Override public boolean shouldPause(){return false;}
    private static boolean inside(double mx,double my,int px,int py,int w,int h){return mx>=px&&mx<px+w&&my>=py&&my<py+h;}
    private void wrapped(DrawContext c,String s,int px,int py,int w,int color){List<net.minecraft.text.OrderedText> lines=textRenderer.wrapLines(Text.literal(s),w);for(int i=0;i<Math.min(2,lines.size());i++)c.drawTextWithShadow(textRenderer,lines.get(i),px,py+i*10,color);}
}
