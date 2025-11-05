package redart15.commandly.mixins.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicGrass;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import redart15.commandly.CommandlyMod;

import java.util.Random;
@Mixin(value = BlockLogicGrass.class, remap = false)
public abstract class StopGrassSpreadingMixin{

	@Inject(method = "updateTick", at = @At("HEAD"), cancellable = true)
	public void updateTick(World world, int x, int y, int z, Random rand, CallbackInfo ci) {

		if (world.isClientSide) {
			return;
		}

		BlockLogicGrass self = (BlockLogicGrass)(Object)this;

		Block<?> dirt = self.dirt;
		Block<?> block = self.block;

		if (revertToDirt(world, x, y, z, rand, dirt)) return;
		if (world.getBlockLightValue(x, y + 1, z) < 9) {
			return;
		}
		spreadGrass(world, x, y, z, rand, dirt, block);
		spreadsFlowers(world, x, y, z, rand);
		ci.cancel();
	}

	private void spreadGrass(World world, int x, int y, int z, Random rand, Block<?> dirt, Block<?> block) {
		if(!world.getGameRuleValue(CommandlyMod.GRASS_SPREADING)) {
			return;
		}
		for(int i = 0; i < 4; ++i) {
			int x1 = x + rand.nextInt(3) - 1;
			int y1 = y + rand.nextInt(5) - 3;
			int z1 = z + rand.nextInt(3) - 1;
			if (world.getBlockId(x1, y1, z1) == dirt.id() && world.getBlockLightValue(x1, y1 + 1, z1) >= 4 && Blocks.lightBlock[world.getBlockId(x1, y1 + 1, z1)] <= 2) {
				world.setBlockWithNotify(x1, y1, z1, block.id());
			}
		}
	}

	private boolean revertToDirt(World world, int x, int y, int z, Random rand, Block<?> dirt) {
		if (world.getBlockLightValue(x, y + 1, z) < 4 && Blocks.lightBlock[world.getBlockId(x, y + 1, z)] > 2) {
			if (rand.nextInt(4) == 0) {
				world.setBlockWithNotify(x, y, z, dirt.id());
			}
			return true;
		}
		return false;
	}

	private void spreadsFlowers(World world, int x, int y, int z, Random rand) {
		if (!((Boolean) world.getGameRuleValue(GameRules.DO_SEASONAL_GROWTH))
				|| world.getBlockId(x, y + 1, z) != 0
				|| world.getSeasonManager().getCurrentSeason() == null
				|| !world.getSeasonManager().getCurrentSeason().growFlowers
				|| rand.nextInt(256) != 0
		) return;
		int flowerID = determineFlower(world,x,y,z,rand);
		world.setBlockWithNotify(x, y + 1, z, flowerID);
	}

	private int determineFlower(World world, int x,int y, int z, Random rand){
		int r = rand.nextInt(400);
		if (r < 26) {
			return Blocks.FLOWER_RED.id();
		}
		if (r < 41) {
			return Blocks.FLOWER_YELLOW.id();
		}
		if (r < 60) {
			Biome biome = world.getBlockBiome(x, y + 1, z);
			if (biome == Biomes.OVERWORLD_BIRCH_FOREST || biome == Biomes.OVERWORLD_SEASONAL_FOREST) {
				return Blocks.FLOWER_PINK.id();
			}
			if (biome == Biomes.OVERWORLD_MEADOW || biome == Biomes.OVERWORLD_BOREAL_FOREST || biome == Biomes.OVERWORLD_SHRUBLAND) {
				return Blocks.FLOWER_PURPLE.id();
			}
			if (biome == Biomes.OVERWORLD_FOREST || biome == Biomes.OVERWORLD_SWAMPLAND || biome == Biomes.OVERWORLD_RAINFOREST || biome == Biomes.OVERWORLD_CAATINGA) {
				return Blocks.FLOWER_LIGHT_BLUE.id();
			}
		}
		if (rand.nextInt(8) == 0) {
			return Blocks.TALLGRASS_FERN.id();
		}
		return Blocks.TALLGRASS.id();
	}
}
