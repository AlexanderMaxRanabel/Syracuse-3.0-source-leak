package org.mapleir.dot4j.systems.module.impl.client;

import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "Spoofer", description = "REQUIRES RELOGGING", category = Category.CLIENT)
public class Spoofer extends Module {

    ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Fabric", "Lunar", "Feather Fabric", "Forge", "Geyser");

    public Spoofer() {
        addSettings(mode);
    }
    // Done in ClientBrandRetrieverMixin
}
