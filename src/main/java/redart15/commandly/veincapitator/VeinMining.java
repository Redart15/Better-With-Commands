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
import net.minecraft.core.world.chunk.ChunkPosition;
import org.jetbrains.annotations.NotNull;
import redart15.commandly.CommandlyConfig;
import redart15.commandly.CommandlyMod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static redart15.commandly.veincapitator.OreTags.ORE;

public class VeinMining {
	private static final int MAX_VEIN_SIZE = 64;
	private final World world;
    private final ItemStack tool;
    private final ChunkPosition point;
    private final Player player;
	private Set<Tag<Block<?>>> miningTags = new HashSet<>();
    private int radius;

    private Set<NamespaceID> miningGroup;
    private boolean onlyThisID = false;
    private ItemList clumpingList;
	private EnumDropCause dropCause;


	public VeinMining(World world, ItemStack itemStack, int x, int y, int z, Player player) {
        this.world = world;
        this.tool = itemStack;
        this.point = new ChunkPosition(x, y, z);
        this.player = player;
        this.radius = 1;
		this.miningTags.add(BlockTags.MINEABLE_BY_PICKAXE);
		ToolMaterial material = PickAxeRegister.getMaterial(tool.getItem());
		this.dropCause = material.isSilkTouch() ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL;
    }

    public static VeinMining veinMining(World world, ItemStack itemStack, int x, int y, int z, Player player) {
        return new VeinMining(world, itemStack, x, y, z, player);
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

    public boolean mine(int blockId, Side side) {
        Block<?> block = Blocks.getBlock(blockId);
        if (block == null || !this.canBeVeinMined(block)) {
            return false;
        }
        this.miningGroup = this.getGroup(block);
        boolean itemStackDamageable = tool.isItemStackDamageable();

        int veinSize = MAX_VEIN_SIZE;
        if (itemStackDamageable) {
            int durabilityLeft = tool.getMaxDamage() - tool.getMetadata();
            veinSize = Math.min(durabilityLeft, MAX_VEIN_SIZE);
        }

        Set<ChunkPosition> toBeMined = this.findAllOreBlocks(veinSize);
        if (EntityItem.enableItemClumping) {
            this.clumpingList = new ItemList(world, point);
        }

        for (ChunkPosition pos : toBeMined) {
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
        Block<?> theBlock = world.getBlock(point.x, point.y, point.z);
        if (theBlock == null || theBlock.id() != block.id()) {
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
		for(Tag<Block<?>> tag : this.miningTags){
			if(block.hasTag(tag)){
				return true;
			}
		}
		return false;
	}

	public static boolean canBeVeinMinedCommand(@NotNull Block<?> block) {
		return (block.hasTag(ORE) || languageKeyOreCheck(block)) && !(block.getLogic() instanceof IPaintable);
	}

	private static boolean isSmartMiner(@NotNull Block<?> block, int metadata) {
		if(!CommandlyConfig.SMART_VEINMINER){
			return false;
		}
		return block.getLogic() instanceof IPaintable || (metadata >> CommandlyMod.getMask()) == 1;
	}

	private boolean languageKeyOre(Block<?> block) {
        String language_key = block.getLanguageKey(0);
        String[] substrings = language_key.split("\\.");
        for (String str : substrings) {
            if (str.equalsIgnoreCase("ore")) {
                this.onlyThisID = true;
                return true;
            }
        }
        return false;
    }

	private static boolean languageKeyOreCheck(Block<?> block) {
		String language_key = block.getLanguageKey(0);
		String[] substrings = language_key.split("\\.");
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

    private Set<ChunkPosition> findAllOreBlocks(int veinSize) {
        Queue<ChunkPosition> queue = new ArrayDeque<>();
        Set<ChunkPosition> visited = new LinkedHashSet<>();

        queue.add(this.point);
        visited.add(this.point);

        while (!queue.isEmpty() && veinSize > 0) {
            ChunkPosition from = queue.poll();
            for (int offX = -radius; offX <= radius; offX++) {
                for (int offY = -radius; offY <= radius; offY++) {
                    for (int offZ = -radius; offZ <= radius; offZ++) {
                        if ((offX == 0 && offZ == 0 && offY == 0)) continue;
                        ChunkPosition to = new ChunkPosition(from.x + offX, from.y + offY, from.z + offZ);
                        if (visited.contains(to)) continue;
                        Block<?> nextBlock = this.world.getBlock(to.x, to.y, to.z);
                        if (nextBlock == null || !this.miningGroup.contains(nextBlock.namespaceId())) continue;
						if (isSmartMiner(nextBlock, world.getBlockMetadata(to.x, to.y, to.z))) continue;
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

    private boolean breakBlock(ChunkPosition pos) {
        Block<?> block = this.world.getBlock(pos.x, pos.y, pos.z);
        int meta = this.world.getBlockMetadata(pos.x, pos.y, pos.z);
        if (block == null) {
            return false;
        }
        if (!this.world.setBlockWithNotify(pos.x, pos.y, pos.z, 0)) {
            return false;
        }
        if (player.getGamemode().dropBlockOnBreak()) {
			ItemStack[] drops = this.getBreakResult(block, this.world, dropCause, pos.x, pos.y, pos.z, meta, (TileEntity) null);
			if (EntityItem.enableItemClumping) {
				this.clumpingList.addAllItems(drops);
            } else {
                this.dropItems(drops, pos);
            }
			player.addStat(block.getStat("stat_mined"), 1);
        }
        this.world.playBlockEvent(this.player, 2001, pos.x, pos.y, pos.z, block.id());
        return true;
    }

    protected void dropItems(ItemStack[] items, ChunkPosition pos) {
        if (items == null) {
            return;
        }
		for (ItemStack stack : items) {
			while (stack.stackSize > 0) {
				this.world.dropItem(pos.x, pos.y, pos.z, stack.splitStack(1));
			}
		}
    }

	private ItemStack[] getBreakResult(@NotNull Block<?> block, World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        ItemStack[] result = block.getBreakResult(world, dropCause, x, y, z, meta, tileEntity);
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
        private final ChunkPosition point;
		private final Map<ItemStack, Integer> itemList = new HashMap<>();

        public ItemList(World world, ChunkPosition point){
            this.world = world;
            this.point = point;
        }

        private void dropAllItems() {
            for(Map.Entry<ItemStack, Integer> entry : itemList.entrySet()){
                ItemStack itemStack = entry.getKey();
                int count  = entry.getValue();
                while(entry.getKey().stackSize > 0) {
                    world.dropItem(point.x, point.y, point.z, itemStack.splitStack(Math.min(count, itemStack.getMaxStackSize())));
                }
            }
        }

        private void addAllItems(ItemStack[] itemStacks) {
            for(ItemStack item : itemStacks){
                this.itemList.merge(item, 1, Integer::sum);
            }
        }
    }
}
