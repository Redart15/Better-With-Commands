package redart15.commandly.api;

import redart15.commandly.veincapitator.OreGroups;
import redart15.commandly.veincapitator.PickAxeRegister;

public interface CommandlyPlugin {
	void registerOreGroups(OreGroups registry);
	void registerPickaxe(PickAxeRegister register);
}
