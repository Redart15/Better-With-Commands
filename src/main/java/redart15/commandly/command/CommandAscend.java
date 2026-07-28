package redart15.commandly.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;

import static redart15.commandly.command.CommandlyCommands.ReturnValues.*;

@SuppressWarnings("ALL") //cause this drives me nuts
public class CommandAscend implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register((ArgumentBuilderLiteral) ((ArgumentBuilderLiteral) ArgumentBuilderLiteral.literal("ascend")
				.requires((t) -> ((CommandSource) t).hasAdmin())
				.executes(ctx -> CendUtil.cend(ctx, CommandAscend::ascend))
				.then(ArgumentBuilderRequired.argument("player", ArgumentTypeEntity.username())
					.executes(ctx -> CendUtil.cend(ctx, CommandAscend::ascend)))));
	}

	private static int ascend(CommandSource source, Player player) {
		World world = player.world;
		for (double y = player.y; y <= world.getHeightBlocks(); y++) {
			ChunkCoordinates telePos = CendUtil.canPlacePlayer(world, player.x, y, player.z);
			if (telePos != null) {
				source.teleportPlayerToPos(player, telePos.x, telePos.y + 1.0f, telePos.z);
				source.sendTranslatableMessage("commandly.ascend.up");
				return code(OK);
			}
		}
		source.sendTranslatableMessage("commandly.ascend.fail");
		return code(FAIL);
	}
}
