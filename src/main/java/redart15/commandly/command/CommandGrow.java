package redart15.commandly.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IBonemealable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.*;
import net.minecraft.core.world.pos.ChunkPos;
import net.minecraft.core.world.pos.TilePos;

import java.util.*;

@SuppressWarnings("ALL") //cause this drives me nuts
public class CommandGrow implements CommandManager.CommandRegistry {
	private static int worldHeight = 256;
	private static byte radius = 3;

	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register((ArgumentBuilderLiteral) ArgumentBuilderLiteral.literal("grow")
			.requires(c -> ((CommandSource) c).hasAdmin()).executes(c -> {
				return growCommand(c);
			}));
	}

	private int growCommand(CommandContext<Object> c) {
		CommandSource source = (CommandSource) c.getSource();
		Player player = source.getSender();
		World world = source.getWorld();
		Set<ChunkCoordinate> loaded = CommandGrow.getLoadedChunks(world, player.chunkCoordX, player.chunkCoordY, player.chunkCoordZ);
		List<TilePos> bonemealTargets = getAllBonemeableBlocks(world, loaded);
		source.sendTranslatableMessage("commandly.command.grow", new Object[]{grow(world, bonemealTargets)});
		return 1;
	}

	public int grow(World world, List<TilePos> targets) {
		int count = 0;
		for (TilePos tilePos : targets) {
			Block<?> block = world.getBlockType(tilePos);
			BlockLogic logic = block.getLogic();
			if (block.getLogic() instanceof IBonemealable) {
				((IBonemealable) logic).onBonemealUsed(new ItemStack(Items.DYE), (Player) null, world, tilePos, Side.TOP, 0, 0);
				count++;
			}
		}
		return count;
	}

	public List<TilePos> getAllBonemeableBlocks(World world, Set<ChunkCoordinate> loaded) {
		List<TilePos> bonemeableBlocks = new ArrayList<>();
		for (ChunkCoordinate coords : loaded) {
			bonemeableBlocks.addAll(getBonemeableBlocks(world, coords));
			if (bonemeableBlocks.size() >= 2 << 16) {
				break;
			}
		}
		return bonemeableBlocks;
	}

	public List<TilePos> getBonemeableBlocks(World world, ChunkCoordinate chunkCoordinate) {
		List<TilePos> bonemealableBlock = new ArrayList<>();
		int ix = chunkCoordinate.x * 16;
		int iz = chunkCoordinate.z * 16;
		for (int y = 0; y <= worldHeight; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int curX = ix + x;
					int curZ = iz + z;
					TilePos tilePos = new TilePos(curX, y, curZ);
					Block<?> block = world.getBlockType(tilePos);
					if (block.id() == 0) continue;
					if (block.getLogic() instanceof IBonemealable) {
						bonemealableBlock.add(tilePos);
					}
				}
			}
		}
		return bonemealableBlock;
	}

	public static Set<ChunkCoordinate> getLoadedChunks(World world, int playerChunkX, int playerChunkY, int playerChunkZ) {
		Set<ChunkCoordinate> loadedChunks = new HashSet<>();
		for (int x = -radius; x <= radius; ++x) {
			for (int z = -radius; z <= radius; ++z) {
				int chunkCoordX = x + playerChunkX;
				int chunkCoordZ = z + playerChunkZ;
				if (world.isChunkLoaded(new ChunkPos(chunkCoordX, chunkCoordZ))) {
					loadedChunks.add(new ChunkCoordinate(chunkCoordX, chunkCoordZ));
				}
			}
		}
		return loadedChunks;
	}
}

