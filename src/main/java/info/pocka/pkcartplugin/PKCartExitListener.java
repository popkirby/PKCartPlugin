package info.pocka.pkcartplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;


class PKCartExitListener implements Listener {

    @EventHandler
    public void exitCart(VehicleExitEvent event) {
        Vehicle vehicle = event.getVehicle();
        LivingEntity player = event.getExited();

        // check vehicle is normal minecart and exited entity is player
        if (vehicle instanceof RideableMinecart &&
                player instanceof Player) {

            // search where Chest exists in 5*5*3 blocks around MineCart
            Location cartLocation = vehicle.getLocation();
            int cartX = cartLocation.getBlockX();
            int cartY = cartLocation.getBlockY();
            int cartZ = cartLocation.getBlockZ();

            World world = cartLocation.getWorld();

            for (int xPoint = cartX - 2; xPoint <= cartX + 2; xPoint++) {
                for (int zPoint = cartZ - 2; zPoint <= cartZ + 2; zPoint++) {
                    for (int yPoint = cartY - 1; yPoint <= cartY + 1; yPoint++) {
                        Block checkingBlock = world.getBlockAt(xPoint, yPoint, zPoint);

                        if (checkingBlock.getType() == Material.CHEST) {
                            Chest chest = (Chest) checkingBlock.getState();

                            // if chest is full, continue searching chests.
                            // (MineCart can't stack so no need for check stacks)
                            if (chest.getBlockInventory().firstEmpty() < 0) {
                                continue;
                            }

                            //　remove MineCart from world and add it to chest
                            vehicle.remove();
                            chest.getBlockInventory().addItem(new ItemStack(Material.MINECART));

                            // if added, finish this event
                            return;
                        }
                    }
                }
            }


        }
    }

}
