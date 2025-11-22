package redart15.commandly.veincapitator;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PickAxeRegister {
	protected static PickAxeRegister instance;
	protected static final Map<Integer, ToolMaterial> toolSet = new HashMap<>();

	public static void init() {
		if (instance == null) {
			instance = new PickAxeRegister();
		}
		instance.load();
	}

	private void load() {
		// nothign to load
	}

	protected PickAxeRegister() {
	}

	public static PickAxeRegister getInstance() {
		return instance;
	}

	public static boolean containsID(int id) {
		return toolSet.containsKey(id);
	}

	public static boolean register(@NotNull Item pickaxe) {
		if (pickaxe instanceof ItemTool) {
			toolSet.put(pickaxe.id, ((ItemTool) pickaxe).getMaterial());
		}
		try {
			Method method = pickaxe.getClass().getMethod("getMaterial");
			ToolMaterial toolMaterial = (ToolMaterial) method.invoke(pickaxe);
			toolSet.put(pickaxe.id, toolMaterial);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean register(@NotNull Item pickaxe, @NotNull ToolMaterial material) {
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
