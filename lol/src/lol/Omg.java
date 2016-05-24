package lol;
 
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import lol.JoinListener;
 
public class Omg extends JavaPlugin {
 
    @Override
    public void onEnable() {
		
		getLogger().info("OK this works");
		new JoinListener(this);
    }
   
    @Override
    public void onDisable() {
       
    }
   
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
    	
    	if (cmd.getName().equalsIgnoreCase("test") && sender instanceof Player) {
    		
    		Player player = (Player) sender;
    		
    		player.sendMessage("Yay it worked! Congrats" + player.getName());
        }
        return false;
    }
   
}