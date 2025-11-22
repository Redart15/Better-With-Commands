package redart15.commandly.mixins.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.BlockLogicMoss;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import redart15.commandly.CommandlyMod;

@Mixin(value = BlockLogicMoss.class, remap = false)
public abstract class StopMossSpreading {

	@ModifyReturnValue(method = "canMossSpread", at = @At("RETURN"))
	public boolean mossSpread(boolean original, World world){
		if(Boolean.TRUE.equals(world.getGameRuleValue(CommandlyMod.MOSS_SPREADING))){
			return original;
		}
		return false;
	}
}
