package redart15.commandly.mixins.veinminer;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicOreNetherCoal;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import static redart15.commandly.CommandlyConfig.SMART_VALUE;

@Mixin(value = BlockLogicOreNetherCoal.class, remap = false)
public class BlockLogicOreNetherCoalMixinSmartPlace extends BlockLogic {

	public BlockLogicOreNetherCoalMixinSmartPlace(Block<?> block, Material material) {
		super(block, material);
	}

	@Override
	public int getPlacedBlockMetadata(@Nullable Player player, ItemStack stack, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		return SMART_VALUE;
	}
}
