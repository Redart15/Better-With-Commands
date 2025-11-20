package redart15.commandly.mixins.veinminer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicOreRedstone;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static redart15.commandly.CommandlyConfig.SMART_VALUE;

@Mixin(value = BlockLogicOreRedstone.class, remap = false)
public abstract class BlockLogicOreRedstoneMixinSmartPlace extends BlockLogic {


	protected BlockLogicOreRedstoneMixinSmartPlace(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public int getPlacedBlockMetadata(@Nullable Player player, ItemStack stack, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		return SMART_VALUE;
	}

	@WrapOperation(method = "lightRedstone", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockWithNotify(IIII)Z"))
	public boolean setLightRedstoneMetadata(World instance, int x, int y, int z, int id, Operation<Boolean> original) {
		int meta = instance.getBlockMetadata(x, y, z);
		return instance.setBlockAndMetadataWithNotify(x, y, z, id, meta);
	}


	@WrapOperation(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockWithNotify(IIII)Z"))
	public boolean updateRedstoneMetadata(World instance, int x, int y, int z, int id, Operation<Boolean> original) {
		int meta = instance.getBlockMetadata(x, y, z);
		return instance.setBlockAndMetadataWithNotify(x, y, z, id, meta);
	}

}
