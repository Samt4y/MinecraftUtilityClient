package com.sam.utilityclient.gui;

import com.sam.utilityclient.UtilityClient;
import com.sam.utilityclient.module.AutoClickModule;
import com.sam.utilityclient.module.FlyModule;
import com.sam.utilityclient.module.SpeedModule;
import com.sam.utilityclient.module.ReachModule;
import com.sam.utilityclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/** A compact click GUI: left-click modules to toggle, right-click to configure. */
public final class ClickGuiScreen extends Screen {
    private static final int PANEL_X = 24;
    private static final int PANEL_Y = 24;
    private static final int PANEL_WIDTH = 220;
    private static final int ROW_HEIGHT = 30;
    private final @Nullable Screen parent;
    private @Nullable Module selected;

    public ClickGuiScreen(@Nullable Screen parent) {
        super(Text.literal("Utility Client"));
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x9A090C12);
        context.fill(PANEL_X, PANEL_Y, PANEL_X + PANEL_WIDTH, PANEL_Y + 34, 0xFF171C27);
        context.drawTextWithShadow(textRenderer, "UTILITY CLIENT", PANEL_X + 12, PANEL_Y + 12, 0xFF78B9FF);

        int y = PANEL_Y + 38;
        for (Module module : UtilityClient.MODULES.all()) {
            boolean hovered = inside(mouseX, mouseY, PANEL_X, y, PANEL_WIDTH, ROW_HEIGHT);
            int color = module.enabled() ? 0xFF214B39 : 0xFF202633;
            if (hovered) color += 0x0A0A0A;
            context.fill(PANEL_X, y, PANEL_X + PANEL_WIDTH, y + ROW_HEIGHT - 2, color);
            context.drawTextWithShadow(textRenderer, module.name(), PANEL_X + 10, y + 7, 0xFFF2F5FA);
            context.drawTextWithShadow(textRenderer, module.enabled() ? "ON" : "OFF", PANEL_X + 184, y + 7,
                    module.enabled() ? 0xFF72E0A2 : 0xFF9AA4B2);
            y += ROW_HEIGHT;
        }

        context.drawTextWithShadow(textRenderer, "Left-click: toggle    Right-click: settings", PANEL_X, y + 10, 0xFF9AA4B2);
        if (selected != null) renderSettings(context, mouseX, mouseY);
    }

    private void renderSettings(DrawContext context, int mouseX, int mouseY) {
        int x = PANEL_X + PANEL_WIDTH + 18;
        int y = PANEL_Y;
        context.fill(x, y, x + 250, y + 132, 0xFF171C27);
        context.drawTextWithShadow(textRenderer, selected.name() + " settings", x + 12, y + 12, 0xFFF2F5FA);
        context.drawTextWithShadow(textRenderer, selected.description(), x + 12, y + 34, 0xFF9AA4B2);

        if (selected instanceof FlyModule fly) {
            settingRow(context, x, y + 62, "Speed", String.format("%.1fx", fly.speed()));
        } else if (selected instanceof SpeedModule speed) {
            settingRow(context, x, y + 62, "Multiplier", String.format("%.1fx", speed.multiplier()));
        } else if (selected instanceof ReachModule reach) {
            settingRow(context, x, y + 62, "Range", String.format("%.1f blocks", reach.range()));
        } else if (selected instanceof AutoClickModule autoClick) {
            settingRow(context, x, y + 62, "Clicks per second", autoClick.clicksPerSecond() + " CPS");
        }
        context.drawTextWithShadow(textRenderer, "Use − / + to adjust", x + 12, y + 108, 0xFF9AA4B2);
    }

    private void settingRow(DrawContext context, int x, int y, String label, String value) {
        context.drawTextWithShadow(textRenderer, label, x + 12, y + 9, 0xFFF2F5FA);
        context.drawTextWithShadow(textRenderer, value, x + 105, y + 9, 0xFF78B9FF);
        context.fill(x + 174, y, x + 204, y + 28, 0xFF283142);
        context.fill(x + 210, y, x + 240, y + 28, 0xFF283142);
        context.drawCenteredTextWithShadow(textRenderer, "−", x + 189, y + 9, 0xFFF2F5FA);
        context.drawCenteredTextWithShadow(textRenderer, "+", x + 225, y + 9, 0xFFF2F5FA);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        int y = PANEL_Y + 38;
        for (Module module : UtilityClient.MODULES.all()) {
            if (inside(mouseX, mouseY, PANEL_X, y, PANEL_WIDTH, ROW_HEIGHT)) {
                if (button == 0) module.toggle(MinecraftClient.getInstance());
                if (button == 1) selected = module;
                return true;
            }
            y += ROW_HEIGHT;
        }

        int settingsX = PANEL_X + PANEL_WIDTH + 18;
        int settingY = PANEL_Y + 62;
        if (selected != null && inside(mouseX, mouseY, settingsX + 174, settingY, 66, 28)) {
            boolean increase = mouseX >= settingsX + 210;
            if (selected instanceof FlyModule fly) fly.setSpeed(fly.speed() + (increase ? 0.1 : -0.1));
            if (selected instanceof SpeedModule speed) speed.setMultiplier(speed.multiplier() + (increase ? 0.1 : -0.1));
            if (selected instanceof ReachModule reach) reach.setRange(reach.range() + (increase ? 0.5 : -0.5));
            if (selected instanceof AutoClickModule autoClick)
                autoClick.setClicksPerSecond(autoClick.clicksPerSecond() + (increase ? 1 : -1));
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public void close() {
        if (client != null) client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() { return false; }

    private static boolean inside(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
    }
}
