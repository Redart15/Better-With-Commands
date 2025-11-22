package redart15.commandly.veincapitator;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PickAxeRegister {
	protected static final PickAxeRegister instance = new PickAxeRegister();
	protected static final Map<Integer, ToolMaterial> toolSet = new HashMap<>();

	public static void init() {
		// loads the class
	}

	protected PickAxeRegister() {
	}

	public static PickAxeRegister getInstance() {
		return instance;
	}

	public static boolean containsID(int id) {
		return toolSet.containsKey(id);
	}

	public static boolean register(ItemTool pickaxe) {
		if (pickaxe == null) {
			return false;
		}
		toolSet.put(pickaxe.id, pickaxe.getMaterial());
		return true;
	}

	public static boolean register(Item pickaxe, ToolMaterial material) {
		if (pickaxe == null || material == null) {
			return false;
		}
		toolSet.put(pickaxe.id, material);
		return true;
	}

	public static ToolMaterial getMaterial(Item item) {
		if (item instanceof ItemTool) {
			return ((ItemTool) item).getMaterial();
		}
		return PickAxeRegister.getMaterial(item.id);
	}

	private static ToolMaterial getMaterial(int id) {
		return toolSet.getOrDefault(id, ToolMaterial.wood);
	}

	public static int getMiningLevel(Item item) {
		return PickAxeRegister.getMaterial(item).getMiningLevel();
	}

	private static int getMiningLevel(int id) {
		return PickAxeRegister.getMaterial(id).getMiningLevel();
	}

}
