package redart15.commandly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.IOException;

public class CommandlyConfig {
	public static final String MOD_ID = "commandly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static int SMART_VALUE = 0b1000_0000;
	public static boolean SMART_VEINMINER = false;
	public static boolean SMART_TREECAPITATOR = false;
	public static boolean init = false;

	public static void init() {
		if(init) return;
		init = true;
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
		return new StringBuilder(category).append(".").append(key).toString();
	}

}
