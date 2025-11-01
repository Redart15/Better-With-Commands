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
import redart15.commandly.util.Point;

import java.util.*;

@SuppressWarnings("ALL") //cause this drives me nuts
public class CommandGrow implements CommandManager.CommandRegistry {
	private static int worldHeight = 256;
	private static byte radius = 9;

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
		List<Point> bonemealTargets = getAllBonemeableBlocks(world, loaded);
		grow(world, bonemealTargets);
		return 1;
	}

	public void grow(World world, List<Point> targets){
		Random rand = new Random();
		for(Point p : targets){
			int x = p.getIntX();
			int y = p.getIntY();
			int z = p.getIntZ();
			Block<?> block = world.getBlock(x,y,z);
			if(block == null) continue;
			BlockLogic logic = block.getLogic();
			if (block.getLogic() instanceof IBonemealable) {
				((IBonemealable)logic).onBonemealUsed(new ItemStack(Items.DYE), (Player) null, world, x,y,z, Side.TOP, 0,0);
			}
		}
	}

	public List<Point> getAllBonemeableBlocks(World world,Set<ChunkCoordinate> loaded) {
		List<Point> bonemeableBlocks = new ArrayList<Point>();
		for(ChunkCoordinate coords: loaded){
			bonemeableBlocks.addAll(getBonemeableBlocks(world, coords));
		}
		return bonemeableBlocks;
	}

	public List<Point> getBonemeableBlocks(World world,ChunkCoordinate chunkCoordinate) {
		List<Point> bonemealableBlock = new ArrayList<Point>();
		int ix = chunkCoordinate.x * 16;
		int iz = chunkCoordinate.z * 16;
		for (int y = 0; y <= worldHeight; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int curX = ix + x;
					int curZ = iz + z;
					Block<?> block = world.getBlock(curX, y, curZ);
					if (block == null) continue;
					if (block.id() == 0) continue;
					if (block.getLogic() instanceof IBonemealable) {
						bonemealableBlock.add(new Point(curX, y, curZ));
					}
				}
			}
		}
		return bonemealableBlock;
	}

	public static Set<ChunkCoordinate> getLoadedChunks(World world,int playerChunkX, int playerChunkY, int playerChunkZ) {
		Set<ChunkCoordinate> loadedChunks = new HashSet<ChunkCoordinate>();
		for (int x = -radius; x <= radius; ++x) {
			for (int z = -radius; z <= radius; ++z) {
				int chunkCoordX = x + playerChunkX;
				int chunkCoordZ = z + playerChunkZ;
				if (world.isChunkLoaded(chunkCoordX, chunkCoordZ)) {
					loadedChunks.add(new ChunkCoordinate(chunkCoordX, chunkCoordZ));
				}
			}
		}
		return loadedChunks;
	}
}

