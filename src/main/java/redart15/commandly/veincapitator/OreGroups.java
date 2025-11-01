package redart15.commandly.veincapitator;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.collection.NamespaceID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/// Ore -> Group -> List of Group members
public class OreGroups {
	public static final OreGroups instance = new OreGroups();
	public Map<String, HashSet<NamespaceID>> GROUP_TO_BLOCKS = new HashMap<>();
	public Map<NamespaceID, String> BLOCKS_TO_GROUP = new HashMap<>();

	public static void init(){}

	protected OreGroups() {
		this.register();
	}

	private void register() {
		this.addOreGroups("coal", Blocks.ORE_COAL_BASALT);
		this.addOreGroups("coal", Blocks.ORE_COAL_GRANITE);
		this.addOreGroups("coal", Blocks.ORE_COAL_LIMESTONE);
		this.addOreGroups("coal", Blocks.ORE_COAL_STONE);
		this.addOreGroups("coal", Blocks.ORE_COAL_PERMAFROST);

		this.addOreGroups("diamond", Blocks.ORE_DIAMOND_STONE);
		this.addOreGroups("diamond",Blocks.ORE_DIAMOND_BASALT);
		this.addOreGroups("diamond",Blocks.ORE_DIAMOND_GRANITE);
		this.addOreGroups("diamond",Blocks.ORE_DIAMOND_LIMESTONE);
		this.addOreGroups("diamond", Blocks.ORE_DIAMOND_PERMAFROST);

		this.addOreGroups("gold",Blocks.ORE_GOLD_STONE);
		this.addOreGroups("gold",Blocks.ORE_GOLD_BASALT);
		this.addOreGroups("gold",Blocks.ORE_GOLD_GRANITE);
		this.addOreGroups("gold",Blocks.ORE_GOLD_LIMESTONE);
		this.addOreGroups("gold", Blocks.ORE_GOLD_PERMAFROST);

		this.addOreGroups("iron",Blocks.ORE_IRON_STONE);
		this.addOreGroups("iron",Blocks.ORE_IRON_BASALT);
		this.addOreGroups("iron",Blocks.ORE_IRON_GRANITE);
		this.addOreGroups("iron",Blocks.ORE_IRON_LIMESTONE);
		this.addOreGroups("iron",Blocks.ORE_IRON_PERMAFROST);

		this.addOreGroups("nether_coal",Blocks.ORE_NETHERCOAL_NETHERRACK);

		this.addOreGroups("lapis",Blocks.ORE_LAPIS_STONE);
		this.addOreGroups("lapis",Blocks.ORE_LAPIS_BASALT);
		this.addOreGroups("lapis",Blocks.ORE_LAPIS_GRANITE);
		this.addOreGroups("lapis",Blocks.ORE_LAPIS_LIMESTONE);
		this.addOreGroups("lapis",Blocks.ORE_LAPIS_PERMAFROST);

		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_STONE);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_BASALT);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_GRANITE);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_LIMESTONE);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_PERMAFROST);

		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_GLOWING_STONE);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_GLOWING_BASALT);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_GLOWING_GRANITE);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_GLOWING_LIMESTONE);
		this.addOreGroups("redstone",Blocks.ORE_REDSTONE_GLOWING_PERMAFROST);
	}

	public boolean addOreGroups(String groupName, Block<?> block) {
		HashSet<NamespaceID> group_to_block_set = this.GROUP_TO_BLOCKS.computeIfAbsent(groupName, key -> new HashSet<>());
		if(!group_to_block_set.add(block.namespaceId())) return false;
		this.BLOCKS_TO_GROUP.put(block.namespaceId(), groupName);
		block.withTags(OreTags.ORE);
		return true;
	}

	public String getOreGroupName(Block<?> block){
		return this.BLOCKS_TO_GROUP.getOrDefault(block.namespaceId(), "");
	}

	public HashSet<NamespaceID> getOreGroup(String name){
		return this.GROUP_TO_BLOCKS.getOrDefault(name, new HashSet<>());
	}

	public HashSet<NamespaceID> getOreGroupFromMember(Block<?> block){
		return getOreGroup(getOreGroupName(block));
	}

	public Map<String, HashSet<NamespaceID>> getGROUP_TO_BLOCKS(){
		return  this.GROUP_TO_BLOCKS;
	}

	public Map<NamespaceID, String> getBLOCKS_TO_GROUP(){
		return  this.BLOCKS_TO_GROUP;
	}
}
