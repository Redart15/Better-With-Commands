package redart15.commandly.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.command.CommandManager;

public class CommandlyCommands {

	public enum ReturnValues{
		FAIL(0),
		OK(1),
		CANNOT(2);
		private final int code;
		ReturnValues(int code){this.code = code;}
		public static int code(ReturnValues v){return v.code;}
	}

	@Environment(EnvType.CLIENT)
	public static void registerClientCommands() {
		CommandManager.registerCommand(new CommandGrow());
		CommandManager.registerCommand(new CommandProtect());
		CommandManager.registerCommand(new CommandAscend());
		CommandManager.registerCommand(new CommandDescend());
	}

	@Environment(EnvType.SERVER)
	public static void registerServerCommands() {
		CommandManager.registerServerCommand(new CommandGrow());
		CommandManager.registerServerCommand(new CommandProtect());
	}
}
