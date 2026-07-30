package redart15.commandly.mixins.mixin.fix;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import redart15.commandly.CommandlyMod;
import redart15.commandly.veincapitator.VeinMining;

@Mixin(value = ItemToolPickaxe.class, remap = false)
public abstract class ItemToolPickaxeMixinVeinMining extends ItemTool {

	protected ItemToolPickaxeMixinVeinMining(String name, String namespaceId, int id, int damageDealt, ToolMaterial toolMaterial, Tag<Block<?>> tagEffectiveAgainst) {
		super(name, namespaceId, id, damageDealt, toolMaterial, tagEffectiveAgainst);
	}


	@Override
	public boolean beforeBlockDestroyed(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player, @NotNull Block<?> block, @NotNull TilePosc blockPos, @NotNull Side side) {
		if (!world.isClientSide && world.getGameRuleValue(CommandlyMod.VEINMINING) && !player.isSneaking()) {
			return !VeinMining.veinMining(world, selfStack, blockPos, player).mine(block, side);
		}
		return true;

	}
}
