
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;

public final class Control extends JavaPlugin implements Listener {
	private HashMap<Player,ControllableMob<Zombie>> zombieMap;
	@Override
	public void onDisable() {
		for(ControllableMob<Zombie> controlledZombie: this.zombieMap.values()) {
			controlledZombie.getActions().die();
		}
		this.zombieMap.clear();
		this.zombieMap = null;
	}
	private void cleanZombie(Player owner) {
		if(this.zombieMap.containsKey(owner)) {
			ControllableMob<Zombie> controlledZombie = this.zombieMap.get(owner);
			controlledZombie.getActions().die();
			this.zombieMap.remove(owner);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		this.cleanZombie(event.getPlayer());
	}
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.zombieMap = new HashMap<Player,ControllableMob<Zombie>>();
	}
	
	private void spawnZombie(Player owner, Location spawnLocation) {
		this.cleanZombie(owner);
		Zombie zombie = spawnLocation.getWorld().spawn(spawnLocation, Zombie.class);
		@SuppressWarnings("deprecation")
		ControllableMob<Zombie> controlledZombie = ControllableMobs.assign(zombie, true);
		this.zombieMap.put(owner, controlledZombie);
	}
	
	@EventHandler
	public void onBlockRightClick(PlayerInteractEvent event) {
		if(event.getAction()==Action.RIGHT_CLICK_BLOCK) {
			if(event.getPlayer().getItemInHand().getType()==Material.ROTTEN_FLESH) {
				int amount = event.getPlayer().getItemInHand().getAmount();
				if(amount==1) {
					event.getPlayer().getInventory().removeItem(event.getPlayer().getInventory().getItemInHand());
				} else {
					event.getPlayer().getItemInHand().setAmount(amount-1);
				}				
				this.spawnZombie(event.getPlayer(), event.getClickedBlock().getLocation().add(0, 1, 0));
			}
		}
	}

}