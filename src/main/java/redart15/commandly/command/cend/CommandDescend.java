package redart15.commandly.command.cend;

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
public class CommandDescend implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register((ArgumentBuilderLiteral) ((ArgumentBuilderLiteral) ArgumentBuilderLiteral.literal("descend")
				.requires((t) -> ((CommandSource) t).hasAdmin())
				.executes(ctx -> CendUtil.cend(ctx, CommandDescend::descend))
				.then(ArgumentBuilderRequired.argument("player", ArgumentTypeEntity.username())
					.executes(ctx -> CendUtil.cend(ctx, CommandDescend::descend)))));
	}

	private static int descend(CommandSource source, Player player) {
		World world = player.world;
		for (double y = player.y - player.bbHeight; y > 0; y--) {
			ChunkCoordinates telePos = CendUtil.canPlacePlayer(world, player.x, y, player.z);
			if (telePos != null) {
				source.teleportPlayerToPos(player, telePos.x, telePos.y + 1.0f, telePos.z);
				source.sendTranslatableMessage("commandly.descend.down");
				return code(OK);
			}
		}
		source.sendTranslatableMessage("commandly.descend.fail");
		return code(FAIL);
	}

}
