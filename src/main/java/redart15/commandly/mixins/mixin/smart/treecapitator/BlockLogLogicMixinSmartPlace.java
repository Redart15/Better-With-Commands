package redart15.commandly.mixins.mixin.smart.treecapitator;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import redart15.commandly.CommandlyConfig;

import static redart15.commandly.CommandlyConfig.getPlacedMetadata;

@Mixin(value = BlockLogicLog.class, remap = false)
public abstract class BlockLogLogicMixinSmartPlace extends BlockLogicAxisAligned {
	protected BlockLogLogicMixinSmartPlace(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public void onPlacedByMob(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, @NotNull Mob mob, double xHit, double yHit) {
		super.onPlacedByMob(world, tilePos, side, mob, xHit, yHit);
		int metadata = world.getBlockData(tilePos);
		world.setBlockData(tilePos, metadata | getPlacedMetadata(CommandlyConfig.SMART_TREECAPITATOR));
	}

	@Override
	public void onPlacedOnSide(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		super.onPlacedOnSide(world, tilePos, side, xHit, yHit);
		int metadata = world.getBlockData(tilePos);
		world.setBlockData(tilePos, metadata | getPlacedMetadata(CommandlyConfig.SMART_TREECAPITATOR));
	}
}
