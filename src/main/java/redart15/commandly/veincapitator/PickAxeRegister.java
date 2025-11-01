package redart15.commandly.veincapitator;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PickAxeRegister {
	protected static final Map<Integer, ToolMaterial> toolSet = new HashMap();


	public static boolean containsID(int id) {
		return toolSet.containsKey(id);
	}

	public static boolean addPickaxe(Item pickaxe) {
		try {
			Method method = pickaxe.getClass().getMethod("getMaterial");
			ToolMaterial toolMaterial = (ToolMaterial) method.invoke(pickaxe);
			toolSet.put(pickaxe.id, toolMaterial);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static ToolMaterial getMaterial(Item item){
		if(item instanceof ItemTool){
			return ((ItemTool) item).getMaterial();
		}
		return PickAxeRegister.getMaterial(item.id);
	}

	private static ToolMaterial getMaterial(int id){
		return toolSet.getOrDefault(id, ToolMaterial.wood);
	}

	public static int getMiningLevel(Item item){
		return PickAxeRegister.getMaterial(item).getMiningLevel();
	}

	private static int getMiningLevel(int id){
		return PickAxeRegister.getMaterial(id).getMiningLevel();
	}

}
