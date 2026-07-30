package redart15.commandly.mixins.mixin.smart.veinminer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockLogic.class, remap = false)
public abstract class BlockLogicMixinSmartPlace {

	@WrapMethod(method = "getPlacedData")
	protected int getSmartPlacementData(
		Player player, ItemStack itemStack,
		World world, TilePosc tilePos,
		Side side, double xHit, double yHit,
		Operation<Integer> original){
		return original.call(player, itemStack, world, tilePos, side, xHit, yHit);
	}
}
