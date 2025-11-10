package redart15.commandly.treecapitator;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.IPaintable;
import org.jetbrains.annotations.NotNull;

public class TreeCapitator {
	public static boolean canTreecapitated(@NotNull Block<?> block){
		BlockLogic logic = block.getLogic();
		return logic instanceof BlockLogicLog && !(logic instanceof IPaintable);
	}
}
