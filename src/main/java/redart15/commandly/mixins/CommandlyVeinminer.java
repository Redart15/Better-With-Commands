package redart15.commandly.mixins;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import redart15.commandly.CommandlyConfig;

import java.util.List;
import java.util.Set;

public class CommandlyVeinminer implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {
		CommandlyConfig.init();
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return CommandlyConfig.SMART_VEINMINER;
	}

	@Override public String getRefMapperConfig() {return "";}
	@Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {/* no need */}
	@Override public List<String> getMixins() {return List.of();}
	@Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {/* no need */}
	@Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { /* no need */}
}
