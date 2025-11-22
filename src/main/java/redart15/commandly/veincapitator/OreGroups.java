package redart15.commandly.veincapitator;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.collection.NamespaceID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/// Ore -> Group -> List of Group members
public class OreGroups {
	protected static final OreGroups instance = new OreGroups();
	protected static final Map<String, HashSet<NamespaceID>> GROUP_TO_BLOCKS = new HashMap<>();
	protected static final Map<NamespaceID, String> BLOCKS_TO_GROUP = new HashMap<>();

	public static class OreGroupBuilder {
		private final String oreGroupName;

		private OreGroupBuilder(String oreGroupName) {
			this.oreGroupName = oreGroupName;
		}

		/**
		 * @implNote Returns a builder to allow chain add.
		 * */
		public static OreGroupBuilder register(String oreGroup) {
			return new OreGroupBuilder(oreGroup);
		}

		/**
		 * @implNote Add ore and its variation to a lookup table.
		 * */
		public OreGroupBuilder addOre(Block<?> block) {
			instance.addOreGroup(this.oreGroupName, block);
			return this;
		}
	}

	public static void init() {
		// no need
	}

	public static OreGroups getInstance(){
		return instance;
	}

	public static OreGroupBuilder register(String string){
		return new OreGroupBuilder(string);
	}

	protected OreGroups() {
		this.load();
	}

	private void load() {
		OreGroups.register("coal")
			.addOre(Blocks.ORE_COAL_BASALT)
			.addOre(Blocks.ORE_COAL_GRANITE)
			.addOre(Blocks.ORE_COAL_LIMESTONE)
			.addOre(Blocks.ORE_COAL_STONE)
			.addOre(Blocks.ORE_COAL_PERMAFROST);

		OreGroups.register("diamond")
			.addOre(Blocks.ORE_DIAMOND_STONE)
			.addOre(Blocks.ORE_DIAMOND_BASALT)
			.addOre(Blocks.ORE_DIAMOND_GRANITE)
			.addOre(Blocks.ORE_DIAMOND_LIMESTONE)
			.addOre(Blocks.ORE_DIAMOND_PERMAFROST);

		OreGroups.register("gold")
			.addOre(Blocks.ORE_GOLD_STONE)
			.addOre(Blocks.ORE_GOLD_BASALT)
			.addOre(Blocks.ORE_GOLD_GRANITE)
			.addOre(Blocks.ORE_GOLD_LIMESTONE)
			.addOre(Blocks.ORE_GOLD_PERMAFROST);

		OreGroups.register("iron")
			.addOre(Blocks.ORE_IRON_STONE)
			.addOre(Blocks.ORE_IRON_BASALT)
			.addOre(Blocks.ORE_IRON_GRANITE)
			.addOre(Blocks.ORE_IRON_LIMESTONE)
			.addOre(Blocks.ORE_IRON_PERMAFROST);

		OreGroups.register("lapis")
			.addOre(Blocks.ORE_LAPIS_STONE)
			.addOre(Blocks.ORE_LAPIS_BASALT)
			.addOre(Blocks.ORE_LAPIS_GRANITE)
			.addOre(Blocks.ORE_LAPIS_LIMESTONE)
			.addOre(Blocks.ORE_LAPIS_PERMAFROST);

		OreGroups.register("nether_coal")
			.addOre(Blocks.ORE_NETHERCOAL_NETHERRACK);

		OreGroups.register("lapis")
			.addOre(Blocks.ORE_REDSTONE_STONE)
			.addOre(Blocks.ORE_REDSTONE_BASALT)
			.addOre(Blocks.ORE_REDSTONE_GRANITE)
			.addOre(Blocks.ORE_REDSTONE_LIMESTONE)
			.addOre(Blocks.ORE_REDSTONE_PERMAFROST)
			.addOre(Blocks.ORE_REDSTONE_GLOWING_STONE)
			.addOre(Blocks.ORE_REDSTONE_GLOWING_BASALT)
			.addOre(Blocks.ORE_REDSTONE_GLOWING_GRANITE)
			.addOre(Blocks.ORE_REDSTONE_GLOWING_LIMESTONE)
			.addOre(Blocks.ORE_REDSTONE_GLOWING_PERMAFROST);
	}

	private boolean addOreGroup(String groupName, Block<?> block) {
		HashSet<NamespaceID> groupToBlockSet = GROUP_TO_BLOCKS.computeIfAbsent(groupName, key -> new HashSet<>());
		if (!groupToBlockSet.add(block.namespaceId())) return false;
		BLOCKS_TO_GROUP.put(block.namespaceId(), groupName);
		block.withTags(OreTags.ORE);
		return true;
	}

	public String getOreGroupName(Block<?> block) {
		return BLOCKS_TO_GROUP.getOrDefault(block.namespaceId(), "");
	}

	public Set<NamespaceID> getOreGroup(String name) {
		return GROUP_TO_BLOCKS.getOrDefault(name, new HashSet<>());
	}

	public Set<NamespaceID> getOreGroupFromMember(Block<?> block) {
		return getOreGroup(getOreGroupName(block));
	}
}
