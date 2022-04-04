package net.j4c0b3y.ajlook.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConfigFile {
    File file;
    YamlConfiguration config;
    String notFound;
    JavaPlugin plugin;
    String filename;

    public Object get(String key) {
        Object value = this.config.get(key);
        if (value == null) {
            this.plugin.getLogger().severe(this.notFound.replace("{KEY}", key).replace("{TYPE}", "value"));
        }

        return value;
    }

    public List<ConfigurationSection> getSections(String path) {
        List<ConfigurationSection> sections = new ArrayList<>();

        for (String section : this.config.getConfigurationSection(path).getKeys(false)) {
            sections.add(this.config.getConfigurationSection(path + "." + section));
        }

        return sections;
    }

    public ConfigurationSection getSection(String path){
        return this.config.getConfigurationSection(path);
    }

    public Integer getInt(String key) {
        int value = this.config.getInt(key, -28026594);
        if (value == -28026594) {
            this.plugin.getLogger().severe(this.notFound.replace("{KEY}", key).replace("{TYPE}", "integer"));
        }

        return value;
    }

    public Double getDouble(String key) {
        double value = this.config.getDouble(key, -28026594);
        if (value == -28026594) {
            this.plugin.getLogger().severe(this.notFound.replace("{KEY}", key).replace("{TYPE}", "double"));
        }

        return value;
    }

    public String getString(String key) {
        String value = this.config.getString(key);
        if (value == null) {
            this.plugin.getLogger().severe(this.notFound.replace("{KEY}", key).replace("{TYPE}", "string"));
        }

        return value;
    }

    public List<String> getStringList(String key) {
        List<String> value = this.config.getStringList(key);
        if (value == null) {
            this.plugin.getLogger().severe(this.notFound.replace("{KEY}", key).replace("{TYPE}", "list"));
        }

        return value;
    }

    public boolean getBoolean(String key) {
        return this.config.getBoolean(key);
    }

    public String getDefaultConfig() throws IOException {
        BufferedReader stream = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/" + this.filename))));
        StringBuilder configFile = new StringBuilder();

        String line;
        while ((line = stream.readLine()) != null) {
            configFile.append(line).append("\n");
        }

        return configFile.toString();
    }

    public ConfigFile(JavaPlugin plugin, String fileName) {
        this.filename = fileName;
        this.notFound = "Could not find {TYPE}: {KEY} in " + fileName + ", try restarting your server or deleting the config so the plugin can recreate it.";
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        int newVer;
        if (!this.file.exists()) {
            if (!Files.exists(this.plugin.getDataFolder().toPath())) {
                try {
                    Files.createDirectory(this.plugin.getDataFolder().toPath());
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }

            try {
                PrintWriter writer = new PrintWriter(this.plugin.getDataFolder() + File.separator + fileName, "UTF-8");
                String[] lines = this.getDefaultConfig().split("\n");

                for (newVer = 0; newVer < lines.length; ++newVer) {
                    String line = lines[newVer];
                    writer.println(line);
                }

                writer.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
        } else {
            try {
                this.file = new File(this.plugin.getDataFolder(), fileName);
                YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(this.file);
                String newConfig = this.getDefaultConfig();
                int oldVer = oldConfig.getInt("config-version", 0);
                String stringVersion = newConfig.split("config-version: ")[1].split("\n")[0];
                newVer = Integer.parseInt(stringVersion);
                if (oldVer < newVer) {
                    this.plugin.getLogger().info("Starting config converter");
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                    String strDate = dateFormat.format(date);
                    duplicateFile(this.file, new File(this.plugin.getDataFolder(), fileName + ".old." + strDate));
                    this.file = new File(this.plugin.getDataFolder(), fileName);
                    this.config = YamlConfiguration.loadConfiguration(this.file);
                    Iterator<String> oldIterator = oldConfig.getKeys(true).iterator();

                    String[] keyParts;
                    while (oldIterator.hasNext()) {
                        String key = oldIterator.next();
                        this.plugin.getLogger().info("Key: " + key);
                        keyParts = key.split("\\.");
                        String keySec = keyParts[keyParts.length - 1];
                        this.plugin.getLogger().info("keySec: " + keySec);
                        StringBuilder newVal = new StringBuilder(newConfig.split(keySec)[1].split("\n")[0]);
                        int i = 0;
                        String[] newConfigStrings = newConfig.split(keySec)[1].split("\n");

                        for (String line : newConfigStrings) {
                            if (i == 0) {
                                ++i;
                            } else {
                                ++i;
                                this.plugin.getLogger().info("Scanning: " + line);
                                if (!line.startsWith("-")) {
                                    break;
                                }

                                newVal.append("\n").append(line);
                            }
                        }

                        String find = keySec + newVal;
                        String replace = keySec + ": " + oldConfig.get(key);
                        replace = replace.replaceAll("\\[", "\n- ");
                        replace = replace.replaceAll(", ", "\n- ");
                        replace = replace.replaceAll("]", "");
                        this.plugin.getLogger().info("Find: " + find + " Replace with: " + replace);
                        if (!keySec.equals("config-version")) {
                            newConfig = newConfig.replaceAll("\\Q" + find + "\\E", replace);
                        }
                    }

                    PrintWriter writer = new PrintWriter(this.file, "UTF-8");
                    String[] lines = newConfig.split("\n");
                    keyParts = lines;

                    for (int j = 0; j < lines.length; ++j) {
                        String line = keyParts[j];
                        writer.println(line);
                    }

                    writer.close();
                }
            } catch (IOException error) {
                Bukkit.getLogger().severe("Couldn't load default config: " + error.getMessage() + "\n" + Arrays.toString(error.getStackTrace()));
            }
        }

        this.file = new File(this.plugin.getDataFolder(), fileName);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    private static void duplicateFile(File source, File destination) throws IOException {
        InputStream is = null;
        FileOutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];

            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            assert is != null;
            is.close();
            assert os != null;
            os.close();
        }
    }
}