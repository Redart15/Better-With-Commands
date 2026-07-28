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
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static redart15.commandly.CommandlyConfig.SMART_VALUE;

@Mixin(value = BlockLogicOreRedstone.class, remap = false)
public abstract class BlockLogicOreRedstoneMixinSmartPlace extends BlockLogic {


	private BlockLogicOreRedstoneMixinSmartPlace(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public int getPlacedData(@Nullable Player player, @NotNull ItemStack itemStack, @NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		return SMART_VALUE;
	}

	@WrapOperation(method = "lightRedstone", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockTypeNotify(Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/block/Block;)Z"))
	public boolean setLightRedstoneMetadata(World instance, @NotNull TilePosc tilePosc, @NotNull Block<?> block, Operation<Boolean> original) {
		int meta = instance.getBlockData(tilePosc);
		return instance.setBlockTypeDataNotify(tilePosc, block, meta);
	}


	@WrapOperation(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockTypeNotify(Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/block/Block;)Z"))
	public boolean updateRedstoneMetadata(World instance, @NotNull TilePosc tilePosc, @NotNull Block<?> block, Operation<Boolean> original) {
		return this.setLightRedstoneMetadata(instance, tilePosc, block, original);
	}

}
