package com.cyprias.railxp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cyprias.railxp.Config.heightInfo;

public class YML {
	HashMap<String, File> Files = new HashMap<String, File>();
	HashMap<String, FileConfiguration> FileConfigs = new HashMap<String, FileConfiguration>();
	private RailXP plugin;
	
	public YML(RailXP plugin) {
		this.plugin = plugin;
		
		//ConfigurationSection section = config.getConfigurationSection("taxHeight");
		ConfigurationSection section = getYMLConfig("heights.yml").getConfigurationSection("taxHeight"); 
		
		int value;
		for (String key : section.getKeys(false)) {
			value = section.getInt(key);
		//	plugin.info("tax " + key + ": " + value);
			RailXP.taxHeights.add(new heightInfo(Integer.valueOf(key), value));
		}

		section = getYMLConfig("heights.yml").getConfigurationSection("grantHeight"); 
		for (String key : section.getKeys(false)) {
			value = section.getInt(key);
		//	plugin.info("grant " + key + ": " + value);
			RailXP.grantHeights.add(new heightInfo(Integer.valueOf(key), value));
		}
	}
	public boolean reloadYMLConfig(String file) {
		if (!FileConfigs.containsKey(file)){
			FileConfigs.put(file, new YamlConfiguration());
		}
		
		try {
			Files.put(file, new File(plugin.getDataFolder(), file));

			if (!Files.get(file).exists()) {
				InputStream r = plugin.getResource(file);
				if (r == null)
					return false;
				
				Files.get(file).getParentFile().mkdirs();
				copy(plugin.getResource(file), Files.get(file));
			}

			FileConfigs.get(file).load(Files.get(file));
		} catch (Exception e) {e.printStackTrace();
		}
		return true;
	}
	
	public static void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getYMLConfig(String file) {
		if (!Files.containsKey(file)) {
			if (reloadYMLConfig(file) == false)
				return null;
		}
		
		return FileConfigs.get(file);
	}

	
	public void saveYMLFile(String fileName) {
		if (!FileConfigs.containsKey(fileName)) {
			FileConfigs.put(fileName, new YamlConfiguration());
		}

		try {
			FileConfigs.get(fileName).save(Files.get(fileName));
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
}
