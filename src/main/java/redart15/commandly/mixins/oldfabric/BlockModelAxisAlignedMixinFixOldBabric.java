package redart15.commandly.mixins.oldfabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.block.model.BlockModelAxisAligned;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockModelAxisAligned.class, remap = false)
public class BlockModelAxisAlignedMixinFixOldBabric {

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMetadata(III)I"))
	public int fixedMetadata(WorldSource instance, int x, int y, int z, Operation<Integer> original){
		int metadata = original.call(instance, x, y, z);
		return metadata & 3;
	}
}
