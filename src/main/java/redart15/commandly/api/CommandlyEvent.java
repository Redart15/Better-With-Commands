package redart15.commandly.api;

import redart15.commandly.veincapitator.OreGroups;
import redart15.commandly.veincapitator.PickAxeRegister;
import turniplabs.halplibe.event.impl.SortedSingleEvent;

import java.util.function.Consumer;

public class CommandlyEvent {
	public static final SortedSingleEvent<Consumer<OreGroups>> REGISTER_ORE = new SortedSingleEvent<>("commandly:ore_groups");
	public static final SortedSingleEvent<Consumer<PickAxeRegister>> REGISTER_PICKAXE = new SortedSingleEvent<>("commandly:custom_pickaxe");

}
