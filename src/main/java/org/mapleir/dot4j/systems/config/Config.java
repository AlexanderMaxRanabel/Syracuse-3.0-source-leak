package org.mapleir.dot4j.systems.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.mapleir.dot4j.ClientMain;
import org.mapleir.dot4j.systems.auth.API;
import org.mapleir.dot4j.systems.module.core.Module;
import org.mapleir.dot4j.systems.module.core.ModuleManager;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.ModeSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.gui.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.Map;

public class Config {

    private String name, description;
    private File configFile;

    private File ROOT_DIR;
    private File configFolder;

    /**
     * Used for loading Configs
     * @param name The Config's name
     * @param description A Description of the config
     * @param file File to load the config from
     */
    public Config(String name, String description, File file) {
        this.name = name;
        this.description = description;
        this.configFile = file;

        // Main Folder
        ROOT_DIR = new File(FabricLoader.getInstance().getGameDirectory(), ClientMain.getName());
        if(!ROOT_DIR.exists()) ROOT_DIR.mkdir();

        // Configs folder
        configFolder = new File(ROOT_DIR, "Configs");
        if(!configFolder.exists()) configFolder.mkdir();
    }

    /**
     * Used for creating a Config
     * @param name The Config's name
     * @param description A Description of the config
     */
    public Config(String name, String description) {
        this.name = name;
        this.description = description;

        // Main Folder
        ROOT_DIR = new File(FabricLoader.getInstance().getGameDirectory(), ClientMain.getINSTANCE().getName());
        if(!ROOT_DIR.exists()) ROOT_DIR.mkdir();

        // Configs folder
        configFolder = new File(ROOT_DIR, "Configs");
        if(!configFolder.exists()) configFolder.mkdir();

        // Create new config file
        configFile = new File(configFolder, name + ".json");
    }


    public void save() throws IOException {
        if (!API.validate()) {
            JsonObject json = new JsonObject();
            json.addProperty("description", description);

            for (Module module : ModuleManager.INSTANCE.getModules()) {
                JsonObject jsonMod = new JsonObject();

                jsonMod.addProperty("enabled", module.isEnabled());
                jsonMod.addProperty("key", module.getKey());

                json.add(module.getName(), jsonMod);
                for (Setting setting : module.getSettings()) {
                    if (setting instanceof ModeSetting s) {
                        jsonMod.addProperty(s.getName(), s.getMode());
                    }

                    if (setting instanceof BooleanSetting s) {
                        jsonMod.addProperty(s.getName(), s.isEnabled());
                    }

                    if (setting instanceof NumberSetting s) {
                        jsonMod.addProperty(s.getName(), s.getValue());
                    }
                }
            }
            PrintWriter writer = new PrintWriter(configFile);
            writer.println(JsonUtils.prettyGson.toJson(json));
            writer.close();
        }
    }

    public void load() throws IOException {
        if (!API.validate()) {
            BufferedReader load = new BufferedReader(new FileReader(configFile));
            JsonObject json = (JsonObject) JsonUtils.jsonParser.parse(load);
            load.close();

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                Module module = ModuleManager.INSTANCE.getModuleByName(entry.getKey());

                if (module == null) continue;

                JsonObject jsonModule = (JsonObject) entry.getValue();
                module.setEnabled(jsonModule.get("enabled").getAsBoolean());

                int key = jsonModule.get("key").getAsInt();
                module.setKey(key);

                for (Setting setting : module.getSettings()) {
                    if (setting instanceof ModeSetting s) {
                        String mode = jsonModule.get(s.getName()).getAsString();
                        s.setMode(mode);
                    }

                    if (setting instanceof BooleanSetting s) {
                        boolean bool = jsonModule.get(s.getName()).getAsBoolean();
                        s.setEnabled(bool);
                    }

                    if (setting instanceof NumberSetting s) {
                        double value = jsonModule.get(s.getName()).getAsDouble();
                        s.setValue(value);
                    }
                }
            }
        }
    }

    public void setName(String name) {
        this.name = name;
        if(configFile != null) {
            File copy = new File(configFile.getAbsolutePath(), name);
            configFile.renameTo(copy);
        }
    }

    public void setDescription(String description) {
        this.description = description;

        try {
            BufferedReader load = new BufferedReader(new FileReader(configFile));
            JsonObject json = (JsonObject) JsonUtils.jsonParser.parse(load);
            load.close();
            json.remove("description");
            json.addProperty("description", description);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        ConfigLoader.getConfigs().remove(this);
        configFile.delete();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
