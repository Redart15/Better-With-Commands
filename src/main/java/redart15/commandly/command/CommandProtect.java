package redart15.commandly.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeVec3;
import net.minecraft.core.net.command.helpers.DoubleCoordinate;
import net.minecraft.core.net.command.helpers.DoubleCoordinates;
import net.minecraft.core.world.World;
import redart15.commandly.CommandlyConfig;
import redart15.commandly.CommandlyMod;
import redart15.commandly.treecapitator.TreeCapitator;
import redart15.commandly.veincapitator.VeinMining;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.function.Predicate;

import static redart15.commandly.command.CommandlyCommands.ReturnValues.*;

@SuppressWarnings("ALL") //cause this drives me nuts
public class CommandProtect implements CommandManager.CommandRegistry {

	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> command =
			(ArgumentBuilderLiteral) ((ArgumentBuilderLiteral) ArgumentBuilderLiteral.literal("protect")
				.requires((t) -> ((CommandSource) t).hasAdmin())
				.then(ArgumentBuilderLiteral.literal("chunk")
					.executes(CommandProtect::chunk)
					.then(ArgumentBuilderRequired.argument("point", ArgumentTypeVec3.vec3d())
						.executes(CommandProtect::chunk)))
				.then(ArgumentBuilderLiteral.literal("radius")
					.then(ArgumentBuilderRequired.argument("radius", ArgumentTypeInteger.integer())
						.executes(CommandProtect::radius))
					.then(ArgumentBuilderRequired.argument("radius", ArgumentTypeInteger.integer())
						.then(ArgumentBuilderRequired.argument("Point", ArgumentTypeVec3.vec3d())
							.executes(CommandProtect::radius))))
				.then(ArgumentBuilderLiteral.literal("points")
					.then(ArgumentBuilderRequired.argument("second point", ArgumentTypeVec3.vec3d())
						.executes(CommandProtect::points))
					.then(ArgumentBuilderRequired.argument("first point", ArgumentTypeVec3.vec3d())
						.then(ArgumentBuilderRequired.argument("second point", ArgumentTypeVec3.vec3d())
							.executes(CommandProtect::points)))));
		dispatcher.register(command);
	}


	private static int chunk(CommandContext<Object> context) {
		if (!CommandlyConfig.SMART_VEINMINER && !CommandlyConfig.SMART_TREECAPITATOR) {
			((CommandSource) context.getSource()).sendTranslatableMessage("commandly.all.inactive");
			return code(CANNOT);
		}
		Predicate<Block<?>> treecapitator = (block) -> false;
		Predicate<Block<?>> veinmining = (block) -> false;
		if (CommandlyConfig.SMART_TREECAPITATOR) {
			treecapitator = (block) -> TreeCapitator.canTreecapitated(block);
		}
		if (CommandlyConfig.SMART_VEINMINER) {
			veinmining = (block) -> VeinMining.canBeVeinMinedCommand(block);
		}
		CommandSource source = (CommandSource) context.getSource();
		World world = source.getWorld();
		DoubleCoordinates p1;
		try {
			p1 = context.getArgument("point", DoubleCoordinates.class);
		} catch (IllegalArgumentException noargs) {
			Player player = source.getSender();
			DoubleCoordinate x = new DoubleCoordinate(false, player.x);
			DoubleCoordinate y = new DoubleCoordinate(false, player.y);
			DoubleCoordinate z = new DoubleCoordinate(false, player.z);
			p1 = new DoubleCoordinates(x, y, z);
		}
		return chunks_protect(source, world, treecapitator, veinmining, p1);
	}

	private static int chunks_protect(CommandSource source, World world, Predicate<Block<?>> treecapitator, Predicate<Block<?>> veinmining, DoubleCoordinates p1) {
		int fx, fy, fz;
		try {
			fx = (int) Math.round(p1.getX(source));
			fy = (int) Math.round(p1.getY(source, EnvironmentHelper.isServerEnvironment()));
			fz = (int) Math.round(p1.getZ(source));
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		fx = Math.floorDiv(fx, 16) * 16;
		fz = Math.floorDiv(fz, 16) * 16;
		int count_protected = 0;
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y <= world.getHeightBlocks(); y++) {
				for (int z = 0; z < 16; z++) {
					Block<?> block = world.getBlock(fx + x, y, fz + z);
					if (block == null) continue;
					if (treecapitator.test(block) || veinmining.test(block)) {
						int metadata = world.getBlockMetadata(fx + x, y, fz + z);
						world.setBlockMetadata(fx + x, y, fz + z, (1 << CommandlyMod.getMask()) | metadata);
						count_protected++;
					}
				}
			}
		}
		if (count_protected == 0) {
			source.sendTranslatableMessage("commandly.protected.fail");
			return code(FAIL);
		}
		source.sendTranslatableMessage("commandly.protected.active", count_protected);
		return code(OK);
	}

	private static int radius(CommandContext<Object> context) {
		if (!CommandlyConfig.SMART_VEINMINER && !CommandlyConfig.SMART_TREECAPITATOR) {
			((CommandSource) context.getSource()).sendTranslatableMessage("commandly.all.inactive");
			return code(CANNOT);
		}
		Predicate<Block<?>> treecapitator = (block) -> false;
		Predicate<Block<?>> veinmining = (block) -> false;
		if (CommandlyConfig.SMART_TREECAPITATOR) {
			treecapitator = (block) -> TreeCapitator.canTreecapitated(block);
		}
		if (CommandlyConfig.SMART_VEINMINER) {
			veinmining = (block) -> VeinMining.canBeVeinMinedCommand(block);
		}
		CommandSource source = (CommandSource) context.getSource();
		World world = source.getWorld();
		DoubleCoordinates p1;
		try {
			p1 = context.getArgument("point", DoubleCoordinates.class);
		} catch (IllegalArgumentException noargs) {
			Player player = source.getSender();
			DoubleCoordinate x = new DoubleCoordinate(false, player.x);
			DoubleCoordinate y = new DoubleCoordinate(false, player.y);
			DoubleCoordinate z = new DoubleCoordinate(false, player.z);
			p1 = new DoubleCoordinates(x, y, z);
		}
		int radius = context.getArgument("radius", Integer.class);
		return radius_protect(source, world, treecapitator, veinmining, radius, p1);
	}

	private static int radius_protect(
		CommandSource source, World world,
		Predicate<Block<?>> treecapitator,
		Predicate<Block<?>> veinmining,
		int radius,
		DoubleCoordinates p1
	) {
		int fx, fy, fz;
		try {
			fx = (int) Math.round(p1.getX(source));
			fy = (int) Math.round(p1.getY(source, EnvironmentHelper.isServerEnvironment()));
			fz = (int) Math.round(p1.getZ(source));
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}

		if ((int) Math.floor(1.25 * Math.PI * Math.pow(radius, 3)) > CommandlyMod.getMaxBlockCount()) {
			source.sendTranslatableMessage("commadly.protected.toolarge");
			return code(CANNOT);
		}
		int count_protected = 0;
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (x * x + y * y + z * z >= radius * radius) continue;
					Block<?> block = world.getBlock(fx + x, fy + y, fz + z);
					if (block == null) continue;
					if (treecapitator.test(block) || veinmining.test(block)) {
						int metadata = world.getBlockMetadata(fx + x, y, fz + z);
						world.setBlockMetadata(fx + x, y, fz + z, (1 << CommandlyMod.getMask()) | metadata);
						count_protected++;
					}
				}
			}
		}
		if (count_protected == 0) {
			source.sendTranslatableMessage("commandly.protected.fail");
			return code(FAIL);
		}
		source.sendTranslatableMessage("commandly.protected.active", count_protected);
		return code(OK);
	}

	private static int points(CommandContext<Object> context) {
		if (!CommandlyConfig.SMART_VEINMINER && !CommandlyConfig.SMART_TREECAPITATOR) {
			((CommandSource) context.getSource()).sendTranslatableMessage("commandly.all.inactive");
			return code(CANNOT);
		}
		Predicate<Block<?>> treecapitator = (block) -> false;
		Predicate<Block<?>> veinmining = (block) -> false;
		if (CommandlyConfig.SMART_TREECAPITATOR) {
			treecapitator = (block) -> TreeCapitator.canTreecapitated(block);
		}
		if (CommandlyConfig.SMART_VEINMINER) {
			veinmining = (block) -> VeinMining.canBeVeinMinedCommand(block);
		}
		CommandSource sauce = (CommandSource) context.getSource();
		World world = sauce.getWorld();
		DoubleCoordinates p1;
		try {
			p1 = context.getArgument("first point", DoubleCoordinates.class);
		} catch (IllegalArgumentException noargs) {
			Player player = sauce.getSender();
			DoubleCoordinate x = new DoubleCoordinate(false, player.x);
			DoubleCoordinate y = new DoubleCoordinate(false, player.y);
			DoubleCoordinate z = new DoubleCoordinate(false, player.z);
			p1 = new DoubleCoordinates(x, y, z);
		}
		DoubleCoordinates p2 = context.getArgument("second point", DoubleCoordinates.class);
		return point_protect(sauce, world, p1, p2, treecapitator, veinmining);
	}

	public static int point_protect(
		CommandSource source, World world, DoubleCoordinates p1, DoubleCoordinates p2,
		Predicate<Block<?>> treecapitator,
		Predicate<Block<?>> veinmining
	) {
		int fx, fy, fz, sx, sy, sz;
		try {
			fx = (int) Math.round(p1.getX(source));
			fy = (int) Math.round(p1.getY(source, EnvironmentHelper.isServerEnvironment()));
			fz = (int) Math.round(p1.getZ(source));

			sx = (int) Math.round(p2.getX(source));
			sy = (int) Math.round(p2.getY(source, EnvironmentHelper.isServerEnvironment()));
			sz = (int) Math.round(p2.getZ(source));
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		if (Math.abs(fx - sx) * Math.abs(fy - sy) * Math.abs(fz - sz) > CommandlyMod.getMaxBlockCount()) {
			source.sendTranslatableMessage("commadly.protected.toolarge");
			return code(CANNOT);
		}

		int count_protected = 0;
		for (int x = Math.min(fx, sx); x <= Math.max(fx, sx); x++) {
			for (int y = Math.min(fy, sy); y <= Math.max(fy, sy); y++) {
				for (int z = Math.min(fz, sz); z <= Math.max(fz, sz); z++) {
					Block<?> block = world.getBlock(x, y, z);
					if (block == null) continue;
					if (treecapitator.test(block) || veinmining.test(block)) {
						int metadata = world.getBlockMetadata(fx + x, y, fz + z);
						world.setBlockMetadata(fx + x, y, fz + z, (1 << CommandlyMod.getMask()) | metadata);
						count_protected++;
					}
				}
			}
		}
		if (count_protected == 0) {
			source.sendTranslatableMessage("commandly.protected.fail");
			return code(FAIL);
		}
		source.sendTranslatableMessage("commandly.protected.active", count_protected);
		return code(OK);
	}
}
