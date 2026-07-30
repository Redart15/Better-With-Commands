package redart15.commandly.mixins.mixin.smart.veinminer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.block.BlockLogicOreIron;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;

import static redart15.commandly.CommandlyConfig.*;

@Mixin(value = BlockLogicOreIron.class, remap = false)
public abstract class BlockLogicOreIronMixinSmartPlace extends BlockLogicMixinSmartPlace{

	@Override
	protected int getSmartPlacementData(
		Player player, ItemStack itemStack,
		World world, TilePosc tilePos,
		Side side, double xHit, double yHit,
		Operation<Integer> original
	) {
		return getPlacedMetadata(SMART_VEINMINER);
	}
}
