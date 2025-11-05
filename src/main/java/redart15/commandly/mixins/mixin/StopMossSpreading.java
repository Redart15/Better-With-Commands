package redart15.commandly.mixins.mixin;

import net.minecraft.core.block.BlockLogicMoss;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import redart15.commandly.CommandlyMod;

@Mixin(value = BlockLogicMoss.class, remap = false)
public class StopMossSpreading {

	@Inject(method = "canMossSpread(Lnet/minecraft/core/world/World;III)Z", at=@At("HEAD"), cancellable = true)
	public void allowedMossSpread(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir){
		if(!world.isClientSide && !world.getGameRuleValue(CommandlyMod.MOSS_SPREADING)){
			cir.setReturnValue(false);
		}
	}
}
