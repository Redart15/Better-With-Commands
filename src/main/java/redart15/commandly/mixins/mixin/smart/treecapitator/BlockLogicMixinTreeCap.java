package redart15.commandly.mixins.mixin.smart.treecapitator;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLogicAxisAligned.class)
public class BlockLogicMixinTreeCap {

	@WrapMethod(method = "onPlacedByMob")
	public void onPlacedByMobSmart(World world, TilePosc tilePos, Side side, Mob mob, double xHit, double yHit, Operation<Void> original) {
		original.call(world, tilePos, side, mob, xHit, yHit);
	}

	@WrapMethod(method = "onPlacedOnSide")
	public void onPlacedOnSideSmart(World world, TilePosc tilePos, Side side, double xHit, double yHit, Operation<Void> original) {
		original.call(world, tilePos, side, xHit, yHit);
	}
}
