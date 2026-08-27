package redart15.commandly.api;

import redart15.commandly.veincapitator.OreGroups;
import redart15.commandly.veincapitator.PickAxeRegister;

@Deprecated(since = "1.1.7", forRemoval = true)
public interface CommandlyPlugin {
	void registerOreGroups(OreGroups registry);
	void registerPickaxe(PickAxeRegister register);
}
