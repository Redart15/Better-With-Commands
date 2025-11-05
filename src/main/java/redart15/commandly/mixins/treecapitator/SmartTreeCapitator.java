package redart15.commandly.mixins.treecapitator;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.IPaintable;
import net.minecraft.core.data.gamerule.TreecapitatorHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import redart15.commandly.CommandlyConfig;

import static redart15.commandly.veincapitator.VeinMining.MASK;

@Mixin(value = TreecapitatorHelper.class, remap = false)
public class SmartTreeCapitator {

	@Shadow
	@Final
	public World world;

	@WrapOperation(method = "chopTree", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/data/gamerule/TreecapitatorHelper;isLog(Lnet/minecraft/core/block/Block;)Z"))
	public boolean isSmartLog(
		TreecapitatorHelper instance, Block<?> block, Operation<Boolean> original
	) {
		TreecapitatorHelper asThis = (TreecapitatorHelper) (Object) this;
		ChunkPosition p = asThis.basePosition;
		int metadata = asThis.world.getBlockMetadata(p.x, p.y, p.z);
		return block != null && !(block.getLogic() instanceof IPaintable) && (metadata >> MASK) == 0 &&  original.call(instance, block);
	}


	@Unique
	private int getBlockWrapper(World instance, int x, int y, int z, Operation<Integer> original) {
		int blockID = original.call(instance, x, y, z);
		Block<?> block = Blocks.getBlock(blockID);
		if (block != null && block.getLogic() instanceof BlockLogicLog){
			int metadata = world.getBlockMetadata(x, y, z);
			if(metadata >> 7 == 1){
				return 0;
			}
		}
		return blockID;
	}

	@WrapOperation(method = {"chopTree"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockId(III)I"))
	private int getBlockID_addLogsAroundBlock(World instance, int x, int y, int z, Operation<Integer> original) {
		return getBlockWrapper(instance, x, y, z, original);
	}
}
