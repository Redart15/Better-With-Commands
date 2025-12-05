package redart15.commandly.command.cend;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.block.material.MaterialDecoration;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;

import java.util.function.BiFunction;

public class CendUtil {
	private CendUtil() {}

	public static ChunkCoordinates canPlacePlayer(World world, double dx, double dy, double dz) {
		int y = (int) Math.ceil(dy);
		int minX = (int) Math.floor(dx);
		int maxX = (int) Math.ceil(dx);
		int minZ = (int) Math.floor(dz);
		int maxZ = (int) Math.ceil(dz);

		for (; minX < maxX; minX++) {
			for (; minZ < maxZ; minZ++) {
				if (world.isAirBlock(minX, y, minZ) && world.isAirBlock(minX, y - 1, minZ) && canTeleportTo(world, minX, y, minZ)) {
					return new ChunkCoordinates(minX, y, minZ);
				}
			}
		}
		return null;
	}

	private static boolean canTeleportTo(World world, int x, int y, int z) {
		if (world.getBlockMaterial(x, y - 2, z).isSolid()) {
			return true;
		}
		return world.getBlockMaterial(x, y - 2, z) instanceof MaterialDecoration && world.getBlockMaterial(x, y - 3, z).isSolid();
	}

	protected static int cend(CommandContext<Object> context, BiFunction<CommandSource, Player, Integer> cend) {
		CommandSource source = (CommandSource) context.getSource();
		Player player;
		try {
			player = context.getArgument("player", Player.class);
		} catch (IllegalArgumentException e) {
			player = source.getSender();
		}
		return cend.apply(source, player);
	}
}
