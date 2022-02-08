package ru.codeon.pvpitemleave.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import ru.codeon.pvpitemleave.PvPItemLeavePlugin;
import ru.codeon.pvpitemleave.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PvPItemLeaveHandler implements Listener {

    private final List<Player> usersList = new ArrayList<>();


    public static ItemStack setUpItemLeave() {

        Material leaveMaterial = Material.getMaterial(PvPItemLeavePlugin.dataConfig.getItemData()
                .getString("material").toUpperCase());

        short leaveData = (short) PvPItemLeavePlugin.dataConfig.getItemData().getInt("data");

        String leaveTitle = StringUtils.convertString(PvPItemLeavePlugin.dataConfig.getItemData().getString("title"));

        List<String> leaveLore = PvPItemLeavePlugin.dataConfig.getItemData().getStringList("lore").stream()
                .map(string -> string.replace('&', '§')).collect(Collectors.toList());

        ItemStack leaveItem = new ItemStack(leaveMaterial, 1, leaveData);
        ItemMeta leaveItemMeta = leaveItem.getItemMeta();

        leaveItemMeta.setDisplayName(leaveTitle);
        leaveItemMeta.setLore(leaveLore);
        leaveItemMeta.addItemFlags(PvPItemLeavePlugin.dataConfig.getItemData().getStringList("flags").stream()
                .map(ItemFlag::valueOf).toArray(ItemFlag[]::new));

        if (PvPItemLeavePlugin.dataConfig.getItemData().getBoolean("isGlowing"))
            leaveItemMeta.addEnchant(Enchantment.DURABILITY, 1, false);

        leaveItem.setItemMeta(leaveItemMeta);

        return leaveItem;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void interactEvent(PlayerInteractEvent event) {

        ItemStack leaveItem = setUpItemLeave();

        if (!event.getPlayer().getInventory().getItemInMainHand().isSimilar(leaveItem) &&
        !event.getPlayer().getInventory().getItemInOffHand().isSimilar(leaveItem)) return;

        Player eventPlayer = event.getPlayer();
        Vector velocity = eventPlayer.getLocation().getDirection();

        event.setCancelled(true);

        if (event.getHand().equals(EquipmentSlot.HAND) &&
            eventPlayer.getInventory().getItemInMainHand().isSimilar(leaveItem)) eventPlayer.getInventory().setItemInMainHand(null);

        if (event.getHand().equals(EquipmentSlot.OFF_HAND) &&
            eventPlayer.getInventory().getItemInMainHand().isSimilar(leaveItem)) eventPlayer.getInventory().setItemInOffHand(null);

        else {

            for (ItemStack itemStack : eventPlayer.getInventory()) {

                if (itemStack.isSimilar(leaveItem)) itemStack.setAmount(itemStack.getAmount() - 1);
                break;

            }

        }

        // velocity.setY(0).normalize().multiply(4).setY(1.5); - по изогнутой линии

        velocity.setY(0).setY(PvPItemLeavePlugin.dataConfig.getVelocityValue());
        eventPlayer.setVelocity(velocity);

        eventPlayer.sendMessage(StringUtils.convertString(PvPItemLeavePlugin.dataConfig
                .getSuccessMessage()));

        List<String> executingCommands = PvPItemLeavePlugin.dataConfig.getItemData().getStringList("executing_commands");

        if (executingCommands != null) {

            executingCommands.forEach(command ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.convertString(command)));

        }

        this.usersList.add(eventPlayer);


    }

    @EventHandler
    public void damageEvent(EntityDamageEvent event) {

        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && event.getEntity() instanceof Player) {

            Player eventPlayer = (Player) event.getEntity();

            if (this.usersList.contains(eventPlayer)) {

                event.setCancelled(true);
                this.usersList.remove(eventPlayer);
            }

        }

    }

}
