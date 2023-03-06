package org.mapleir.dot4j.systems.module.impl.client;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.mapleir.dot4j.gui.HUDModule;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.systems.module.core.ModuleManager;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Arraylist extends HUDModule {
    public BooleanSetting show = new BooleanSetting("Enabled", true);
    public ModeSetting mode = new ModeSetting("Mode", "Asolfo", "Asolfo", "White");
    public NumberSetting rainbowspeed = new NumberSetting("Rainbow speed", 1, 8, 4, 1);
    public Arraylist() {
        super("Arraylist","Enabled modules", Category.CLIENT, 69,69,69,69);
        addSettings(show, mode, rainbowspeed);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void draw(MatrixStack matrices) {
        int xOffset = -5;
        int yOffset = 5;

        int index = 0;
        int sWidth = mc.getWindow().getScaledWidth();

        List<Module> enabled = ModuleManager.INSTANCE.getEnabledModules();
        enabled.sort(Comparator.comparingInt(m -> (int) mc.textRenderer.getWidth(((Module) m).getDisplayName())).reversed());

        for (Module mod : enabled) {
            int fWidth = mc.textRenderer.getWidth(mod.getDisplayName());
            int lastWidth;
            int fHeight = mc.textRenderer.fontHeight;
            // big black juicy dudes are oily
            int fromX = sWidth - fWidth - 5;
            int fromY = (fHeight - 1) * (index) + 1;
            int toX = sWidth - 2;
            int toY = (fHeight - 1) * (index) + fHeight;

            if (index - 1 >= 0) lastWidth = mc.textRenderer.getWidth(enabled.get(index - 1).getDisplayName());
            else lastWidth = sWidth;

            DrawableHelper.fill(matrices, fromX + xOffset, fromY + 1 + yOffset, sWidth + xOffset, toY + 1 + yOffset, 0x60000000);


            DrawableHelper.fill(matrices, fromX + xOffset, fromY + 1 + yOffset, fromX - 1 + xOffset, toY + 1 + yOffset, astolfoColors(1, 4, index * 150));
            DrawableHelper.fill(matrices, toX + 2 + xOffset, fromY + 1 + yOffset, toX + 1 + xOffset, toY + 1 + yOffset, astolfoColors(1, 4, index * 150));

            if (index == enabled.size() - 1) {
                DrawableHelper.fill(matrices, fromX + xOffset, toY + yOffset, sWidth + xOffset, toY + 1 + yOffset, astolfoColors(1, 4, index * 150));
            }
            if (index == enabled.size() - enabled.size()) {
                DrawableHelper.fill(matrices, fromX - 1 + xOffset, fromY + yOffset, sWidth + xOffset, fromY + 1 + yOffset, astolfoColors(1, 4, index * 150));
            } else {
                DrawableHelper.fill(matrices, fromX + xOffset, fromY + yOffset, toX - lastWidth - 4 + xOffset, fromY + 1 + yOffset, astolfoColors(1, 4, index * 150));
            }

            mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), sWidth - fWidth + xOffset, (fHeight - 1) * (index) + yOffset, astolfoColors(1, 3, index * 150));
            index++;
        }
    }

    public static int astolfoColors(int yOffset, int yTotal, long index) {
        float speed = (float) 40;
        float hue = (float) ((System.currentTimeMillis() + index) % (int) speed) + ((yTotal - yOffset) * 9);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = (0.5f - (hue - 0.5f));
        }
        hue += 0.5;
            return Color.HSBtoRGB(hue, 0.4f, 1f);
    }
}

