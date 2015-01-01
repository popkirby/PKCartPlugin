package info.pocka.pkcartplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


class PKPlayerInteractListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void enterCart(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        Material clickedBlockType = clickedBlock.getType();

        // check if player right-clicks rails with bare hand, and not in vehicles.
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() == null && !event.getPlayer().isInsideVehicle() &&
                (clickedBlockType == Material.RAILS || clickedBlockType == Material.ACTIVATOR_RAIL ||
                        clickedBlockType == Material.DETECTOR_RAIL || clickedBlockType == Material.POWERED_RAIL)) {

            Location clickedBlockLocation = clickedBlock.getLocation();
            int blockX = clickedBlockLocation.getBlockX();
            int blockY = clickedBlockLocation.getBlockY();
            int blockZ = clickedBlockLocation.getBlockZ();

            World world = clickedBlock.getWorld();


            // search chest around rails (5*3*5)
            for (int xPoint = blockX - 2; xPoint <= blockX + 2; xPoint++) {
                for (int zPoint = blockZ - 2; zPoint <= blockZ + 2; zPoint++) {
                    for (int yPoint = blockY - 1; yPoint <= blockZ + 1; yPoint++) {
                        Block checkingBlock = world.getBlockAt(xPoint, yPoint, zPoint);

                        if (checkingBlock.getType() == Material.CHEST) {
                            Chest chest = (Chest) checkingBlock.getState();
                            Inventory chestInventory = chest.getBlockInventory();

                            // check if chest contains minecart
                            if (chestInventory.contains(Material.MINECART)) {
                                // remove a minecart from chest
                                chestInventory.removeItem(
                                        new ItemStack(Material.MINECART, 1));

                                // spawn minecart on a rail
                                Location cartLocation = new Location(world, blockX + 0.5, blockY + 0.1, blockZ + 0.5);
                                Minecart spawnedCart = world.spawn(cartLocation, Minecart.class);

                                // set the player as a passenger
                                spawnedCart.setPassenger(event.getPlayer());

                                // finish this event.
                                return;

                            }

                        }
                    }
                }
            }
        }
    }

}
