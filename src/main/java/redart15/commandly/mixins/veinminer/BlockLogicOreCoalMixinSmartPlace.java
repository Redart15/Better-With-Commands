package redart15.commandly.mixins.veinminer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicOreCoal;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import static redart15.commandly.CommandlyConfig.SMART_VALUE;

@Mixin(value = BlockLogicOreCoal.class, remap = false)
public abstract class BlockLogicOreCoalMixinSmartPlace extends BlockLogicMixinSmartPlace{

	@Override
	protected int getSmartPlacementData(
		Player player, ItemStack itemStack,
		World world, TilePosc tilePos,
		Side side, double xHit, double yHit,
		Operation<Integer> original
	) {
		return SMART_VALUE;
	}
}
