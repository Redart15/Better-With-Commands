package redart15.commandly.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Axis;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockLogicAxisAligned.class, remap = false)
public abstract class BlockLogicAxisAlignedMixinFix extends BlockLogic {
	protected BlockLogicAxisAlignedMixinFix(Block<?> block, Material material) {
		super(block, material);
	}

	@WrapMethod(method = "metaToAxis")
	private static Axis correctMetaToAxis(int metadata, Operation<Axis> original) {
		int boundedMetadata = metadata & 3;
		return original.call(boundedMetadata);
	}
}
