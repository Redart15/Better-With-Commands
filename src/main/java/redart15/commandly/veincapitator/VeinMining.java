package redart15.commandly.veincapitator;

import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import redart15.commandly.CommandlyConfig;
import redart15.commandly.CommandlyMod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static net.minecraft.core.world.LevelListener.EVENT_BLOCK_BREAK;
import static redart15.commandly.veincapitator.OreTags.ORE;

public class VeinMining {
	private static final int MAX_VEIN_SIZE = 64;
	private final World world;
	private final ItemStack tool;
	private final TilePosc point;
	private final Player player;
	private Set<Tag<Block<?>>> miningTags = new HashSet<>();
	private int radius;

	private Set<NamespaceID> miningGroup;
	private boolean onlyThisID = false;
	private ItemList clumpingList;
	private EnumDropCause dropCause;


	public VeinMining(World world, ItemStack itemStack, TilePosc tilePos, Player player) {
		this.world = world;
		this.tool = itemStack;
		this.point = tilePos;
		this.player = player;
		this.radius = 1;
		this.miningTags.add(BlockTags.MINEABLE_BY_PICKAXE);
		ToolMaterial material = PickAxeRegister.getMaterial(tool.getItem());
		this.dropCause = material.isSilkTouch() ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL;
	}

	public static VeinMining veinMining(World world, ItemStack itemStack, TilePosc tilePos, Player player) {
		return new VeinMining(world, itemStack, tilePos, player);
	}

	public VeinMining setDropCause(EnumDropCause dropCause) {
		this.dropCause = dropCause;
		return this;
	}

	@SafeVarargs
	public final VeinMining setMiningTags(Tag<Block<?>>... miningTags) {
		if (miningTags.length > 0) {
			this.miningTags = new HashSet<>(Arrays.asList(miningTags));
		}
		return this;
	}

	public VeinMining setRadius(int radius) {
		this.radius = radius;
		return this;
	}

	public boolean mine(Block<?> block, Side side) {
		if (!this.canBeVeinMined(block)) {
			return false;
		}
		this.miningGroup = this.getGroup(block);
		boolean itemStackDamageable = tool.isItemStackDamageable();

		int veinSize = MAX_VEIN_SIZE;
		if (itemStackDamageable) {
			int durabilityLeft = tool.getMaxDamage() - tool.getMetadata();
			veinSize = Math.min(durabilityLeft, MAX_VEIN_SIZE);
		}

		Set<TilePosc> toBeMined = this.findAllOreBlocks(veinSize);
		if (EntityItem.enableItemClumping) {
			this.clumpingList = new ItemList(world, point);
		}

		for (TilePosc pos : toBeMined) {
			if (!this.breakBlock(pos)) continue;
			if (itemStackDamageable) {
				tool.damageItem(1, player);
				if (tool.stackSize <= 0) {
					this.player.destroyCurrentEquippedItem();
				}
			}
		}

		if (EntityItem.enableItemClumping) {
			this.clumpingList.dropAllItems();
		}
		return true;
	}

	private boolean canBeVeinMined(Block<?> block) {
		Block<?> theBlock = world.getBlockType(point);
		if (theBlock.id() != block.id()) {
			return false;
		}
		if (!this.hasTag(block)) {
			return false;
		}

		if (this.tool == null) {
			return false;
		}
		Item toolItem = this.tool.getItem();
		if (!(toolItem instanceof ItemToolPickaxe) && !PickAxeRegister.containsID(toolItem.id)) return false;

		int toolMiningLevel = PickAxeRegister.getMiningLevel(toolItem);
		int blockMiningLevel = ItemToolPickaxe.miningLevels.getOrDefault(block, 0);
		if (blockMiningLevel > toolMiningLevel) return false;

		return block.hasTag(ORE) || this.languageKeyOre(block);
	}


	private boolean hasTag(Block<?> block) {
		for (Tag<Block<?>> tag : this.miningTags) {
			if (block.hasTag(tag)) {
				return true;
			}
		}
		return false;
	}

	public static boolean canBeVeinMinedCommand(@NotNull Block<?> block) {
		return (block.hasTag(ORE) || languageKeyOreCheck(block)) && !(block.getLogic() instanceof IPaintable);
	}

	private static boolean isSmartMiner(@NotNull Block<?> block, int metadata) {
		if (!CommandlyConfig.SMART_VEINMINER) {
			return false;
		}
		return block.getLogic() instanceof IPaintable || (metadata >> CommandlyMod.getMask()) == 1;
	}

	private boolean languageKeyOre(Block<?> block) {
		String languageKey = block.getLanguageKey(0);
		String[] substrings = languageKey.split("\\.");
		for (String str : substrings) {
			if (str.equalsIgnoreCase("ore")) {
				this.onlyThisID = true;
				return true;
			}
		}
		return false;
	}

	private static boolean languageKeyOreCheck(Block<?> block) {
		String languageKey = block.getLanguageKey(0);
		String[] substrings = languageKey.split("\\.");
		for (String str : substrings) {
			if (str.equalsIgnoreCase("ore")) {
				return true;
			}
		}
		return false;
	}

	private Set<NamespaceID> getGroup(Block<?> block) {
		if (onlyThisID) {
			this.onlyThisID = false;
			HashSet<NamespaceID> set = new HashSet<>();
			set.add(block.namespaceId());
			return set;
		}
		return OreGroups.instance.getOreGroupFromMember(block);
	}

	private Set<TilePosc> findAllOreBlocks(int veinSize) {
		Queue<TilePosc> queue = new ArrayDeque<>();
		Set<TilePosc> visited = new LinkedHashSet<>();

		queue.add(this.point);
		visited.add(this.point);

		while (!queue.isEmpty() && veinSize > 0) {
			TilePosc from = queue.poll();
			for (int offX = -radius; offX <= radius; offX++) {
				for (int offY = -radius; offY <= radius; offY++) {
					for (int offZ = -radius; offZ <= radius; offZ++) {
						if ((offX == 0 && offZ == 0 && offY == 0)) continue;
						TilePos to = new TilePos(from.x() + offX, from.y() + offY, from.z() + offZ);
						if (visited.contains(to)) continue;
						Block<?> nextBlock = this.world.getBlockType(to);
						if (!this.miningGroup.contains(nextBlock.namespaceId())) continue;
						if (isSmartMiner(nextBlock, world.getBlockData(to))) continue;
						visited.add(to);
						queue.add(to);
						veinSize--;
						if (veinSize <= 0) {
							return visited;
						}
					}
				}
			}
		}
		return visited;
	}

	private boolean breakBlock(TilePosc pos) {
		Block<?> block = this.world.getBlockType(pos);
		int meta = this.world.getBlockData(pos);
		if (!this.world.setBlockData(pos, 0)) {
			return false;
		}
		if (player.getGamemode().hasBlockConsumption()) {
			if (EntityItem.enableItemClumping) {
				ItemStack[] drops = this.getBreakResult(block, this.world, dropCause, pos, meta, null);
				this.clumpingList.addAllItems(drops);
			} else {
				block.dropWithCause(world, this.dropCause, pos, meta, null, this.player);
			}
			player.addStat(block.getStat("stat_mined"), 1);
		}
		this.world.setBlockTypeNotify(pos, Blocks.AIR);
		this.world.playBlockEvent(this.player, pos, EVENT_BLOCK_BREAK, block.id());
		return true;
	}

	private ItemStack[] getBreakResult(@NotNull Block<?> block, World world, EnumDropCause dropCause, TilePosc tilePos, int meta, TileEntity tileEntity) {
		ItemStack[] result = block.getBreakResult(world, dropCause, tilePos, meta, tileEntity);
		return this.getAdditionalBreakResult(world, result, meta, block);
	}

	private ItemStack[] getAdditionalBreakResult(World world, ItemStack[] result, int meta, Block<?> block) {
		try {
			BlockLogic logic = block.getLogic();
			Method method = logic.getClass().getMethod("getAdditionalBreakResult", World.class, Item.class, ItemStack[].class, Integer.class);
			return (ItemStack[]) method.invoke(logic, world, tool.getItem(), result, meta);
		} catch (NoSuchMethodException e) {
			return result;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public class ItemList {
		private final World world;
		private final TilePosc point;
		private final Map<ItemStack, Integer> itemList = new HashMap<>();

		public ItemList(World world, TilePosc point) {
			this.world = world;
			this.point = point;
		}

		private void dropAllItems() {
			for (Map.Entry<ItemStack, Integer> entry : itemList.entrySet()) {
				ItemStack itemStack = entry.getKey();
				int count = entry.getValue();
				while (entry.getKey().stackSize > 0) {
					world.dropItem(point, itemStack.splitStack(Math.min(count, itemStack.getMaxStackSize())));
				}
			}
		}

		private void addAllItems(ItemStack[] itemStacks) {
			for (ItemStack item : itemStacks) {
				this.itemList.merge(item, 1, Integer::sum);
			}
		}
	}
}
