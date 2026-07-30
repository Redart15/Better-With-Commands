package redart15.commandly.mixins.mixin.gamerules;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.block.*;
import net.minecraft.core.data.gamerule.TreecapitatorHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import redart15.commandly.CommandlyMod;


@Mixin(value = TreecapitatorHelper.class, remap = false)
public abstract class SmartTreeCapitator {

	@Shadow
	@Final
	public World world;

	@WrapOperation(method = "chopTree", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/data/gamerule/TreecapitatorHelper;isLog(Lnet/minecraft/core/block/Block;)Z"))
	public boolean isSmartLog(
		TreecapitatorHelper instance, Block<?> block, Operation<Boolean> original
	) {
		TreecapitatorHelper asThis = (TreecapitatorHelper) (Object) this;
		ChunkPosition p = asThis.basePosition;
		int metadata = asThis.world.getBlockData(new TilePos(p.x, p.y, p.z));
		return !(block.getLogic() instanceof IPaintable) && (metadata >> CommandlyMod.getMask()) == 0 &&  original.call(instance, block);
	}


	@Unique
	private int getBlockWrapper(World instance, int x, int y, int z, Operation<Integer> original) {
		int blockID = original.call(instance, x, y, z);
		Block<?> block = Blocks.getBlock(blockID);
		int metadata = world.getBlockData(new TilePos(x, y, z));
		if(isSmartTreecapitator(block.getLogic(), metadata)){
			return 0;
		}
		return blockID;
	}

	@WrapOperation(method = {"chopTree"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockId(III)I"))
	private int getBlockIDAddLogsAroundBlock(World instance, int x, int y, int z, Operation<Integer> original) {
		return getBlockWrapper(instance, x, y, z, original);
	}

	@Unique
	private static boolean isSmartTreecapitator(@NotNull BlockLogic logic, int metadata) {
		return logic instanceof IPaintable || (metadata >> CommandlyMod.getMask()) == 1;
	}
}
