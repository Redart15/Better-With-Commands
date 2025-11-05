package redart15.commandly.mixins.treecapitator;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import redart15.commandly.CommandlyConfig;

@Mixin(value = BlockLogicLog.class, remap = false)
public class BlockLogLogicMixinSmartPlace extends BlockLogicAxisAligned {
	public BlockLogLogicMixinSmartPlace(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		super.onBlockPlacedByMob(world, x, y, z, side, mob, xPlaced, yPlaced);
		int metadata = world.getBlockMetadata(x,y,z);
		world.setBlockMetadata(x,y,z, metadata | CommandlyConfig.SMART_VALUE);
	}

	@Override
	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		super.onBlockPlacedOnSide(world, x, y, z, side, xPlaced, yPlaced);
		int metadata = world.getBlockMetadata(x,y,z);
		world.setBlockMetadata(x,y,z, metadata | CommandlyConfig.SMART_VALUE);
	}


}
