package redart15.commandly.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeEntity;
import net.minecraft.core.world.World;

import static redart15.commandly.command.CommandlyCommands.ReturnValues.*;

@SuppressWarnings("ALL") //cause this drives me nuts
public class CommandAscend implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> command =
			(ArgumentBuilderLiteral) ((ArgumentBuilderLiteral) ArgumentBuilderLiteral.literal("ascend")
				.requires((t) -> ((CommandSource) t).hasAdmin())
				.executes(CommandAscend::ascend)
				.then(ArgumentBuilderRequired.argument("player", ArgumentTypeEntity.username())
					.executes(CommandAscend::ascend)));
		CommandNode<CommandSource> registeredCommand = dispatcher.register(command);
		dispatcher.register((ArgumentBuilderLiteral)((ArgumentBuilderLiteral)ArgumentBuilderLiteral.literal("up")
			.requires((t) -> ((CommandSource) t).hasAdmin())
			.redirect((CommandNode<Object>)(CommandNode<?>) registeredCommand)));
	}

	private static int ascend(CommandContext<Object> context) {
		CommandSource source = (CommandSource) context.getSource();
		Player player;
		try {
			player = context.getArgument("player", Player.class);
		} catch (IllegalArgumentException e) {
			player = source.getSender();
		}
		return ascend(source, player);
	}


	private static int ascend(CommandSource source, Player player) {
		World world = player.world;
		for (double y = player.y; y <= world.getHeightBlocks(); y++) {
			if (canPlacePlayer(world, player.x, y, player.z)) {
				source.teleportPlayerToPos(player, player.x, (int) Math.ceil(y) + 1, player.z);
				source.sendTranslatableMessage("commandly.ascend.up");
				return code(OK);
			}
		}
		source.sendTranslatableMessage("commandly.ascend.fail");
		return code(FAIL);
	}

	private static boolean canPlacePlayer(World world, double dx, double dy, double dz) {
		int x = (int) Math.floor(dx);
		int z = (int) Math.floor(dz);
		int y = (int) Math.ceil(dy);

		return world.isAirBlock(x, y, z)
			&& world.isAirBlock(x, y - 1, z)
			&& world.isBlockNormalCube(x, y - 2, z);
	}
}
