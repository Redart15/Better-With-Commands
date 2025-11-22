package redart15.commandly.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.block.BlockLogicGrass;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import redart15.commandly.CommandlyMod;

@Mixin(value = BlockLogicGrass.class, remap = false)
public abstract class StopGrassSpreadingMixin{
	@WrapOperation(
		method = "updateTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/world/World;getBlockLightValue(III)I",
			ordinal = 2
		)
	)
	public int allowGrassUpdates(World instance, int x, int y, int z, Operation<Integer> original){
		if(Boolean.TRUE.equals(instance.getGameRuleValue(CommandlyMod.GRASS_SPREADING))){
			return original.call(instance, x, y, z);
		}
		return 0;
	}
}
