package redart15.commandly;

import static redart15.commandly.CommandlyConfig.StrBuilder.str;
import static redart15.commandly.CommandlyMod.LOGGER;
import static redart15.commandly.CommandlyMod.MOD_ID;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.IOException;

public class CommandlyConfig {
	public static int SMART_VALUE = 0b1000_0000;
	public static boolean SMART_VEINMINER = false;
	public static boolean SMART_TREECAPITATOR = false;

	public static class StrBuilder{

		public static StringBuilder str(){
			return new StringBuilder();
		}

		public static StringBuilder str(String string){
			return new StringBuilder(string);
		}
	}


	public static void init() {
		Toml properties = new Toml("Commandly Config");
		properties.addCategory("Smart Commands")
			.addEntry("SMART_VEINMINER", SMART_VEINMINER)
			.addEntry("SMART_TREECAPITATOR", SMART_TREECAPITATOR);

		TomlConfigHandler config = new TomlConfigHandler(MOD_ID, properties);
		if(config.getConfigFile().exists()){
			config.loadConfig();
		}else{
			try{
				if(config.getConfigFile().createNewFile()){
					LOGGER.info("Commandly Config initialized.");
				}
			} catch (IOException e) {
				LOGGER.error("Commandly Config failed to generate, deleted the config and try again.");
				throw new RuntimeException(e);
			}
		}
		config.writeConfig();
		SMART_VEINMINER = config.getBoolean(key("Smart Commands", "SMART_VEINMINER"));
		SMART_TREECAPITATOR = config.getBoolean(key("Smart Commands", "SMART_TREECAPITATOR"));
		LOGGER.info("Commandly Config loaded.");
	}

	private static String key(String category, String key) {
		return str(category).append(".").append(key).toString();
	}

}
