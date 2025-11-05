package redart15.commandly;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.*;

import static redart15.commandly.CommandlyMod.LOGGER;

public class CommandlyMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {
		CommandlyConfig.init(); // need to be present before any mixins are done
		if(CommandlyConfig.SMART_VEINMINER){
			Mixins.addConfiguration("addon/veinminer.mixin.json");
			LOGGER.info("Smart Veinmeining loaded.");
		}
		if(CommandlyConfig.SMART_TREECAPITATOR){
			Mixins.addConfiguration("addon/treecapitator.mixin.json");
			LOGGER.info("Smart Treecapitator loaded.");
		}
		LOGGER.info("Commandly MixinPlugin initialized.");
	}

	// not needed
	@Override public String getRefMapperConfig() {return null;}
	@Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {return true;}
	@Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {} // no exception to accept
	@Override public List<String> getMixins() {return null;} // using json
	@Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
	@Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
