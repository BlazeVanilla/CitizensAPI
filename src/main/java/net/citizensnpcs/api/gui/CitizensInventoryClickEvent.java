package net.citizensnpcs.api.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.citizensnpcs.api.util.Colorizer;

public class CitizensInventoryClickEvent extends InventoryClickEvent {
    private final InventoryClickEvent event;
    private final ItemStack result;

    public CitizensInventoryClickEvent(InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction(),
                event.getHotbarButton());
        this.event = event;
        this.result = getResult(event);
    }

    @Override
    public ItemStack getCurrentItem() {
        return event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR ? null
                : event.getCurrentItem();
    }

    public ItemStack getCurrentItemNonNull() {
        return event.getCurrentItem() == null ? new ItemStack(Material.AIR, 0) : event.getCurrentItem();
    }

    @Override
    public ItemStack getCursor() {
        return event.getCursor() == null || event.getCursor().getType() == Material.AIR ? null : event.getCursor();
    }

    public ItemStack getCursorNonNull() {
        return event.getCursor() == null ? new ItemStack(Material.AIR, 0) : event.getCursor();
    }

    private ItemStack getResult(InventoryClickEvent event) {
        ItemStack stack = event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR
                ? event.getCursor().clone()
                : event.getCurrentItem().clone();
        switch (event.getAction()) {
            case PICKUP_ONE:
                stack.setAmount(stack.getAmount() - 1);
                break;
            case PICKUP_HALF:
                stack.setAmount((int) Math.floor(stack.getAmount() / 2.0));
                break;
            case PICKUP_ALL:
                stack = null;
                break;
            case PLACE_ALL:
                stack.setAmount(
                        Math.min(stack.getAmount() + event.getCursor().getAmount(), stack.getType().getMaxStackSize()));
                break;
            case PLACE_SOME:
                stack.setAmount(Math.min(stack.getAmount(), stack.getType().getMaxStackSize()));
                break;
            case PLACE_ONE:
                stack.setAmount(stack.getAmount() + 1);
                break;
            default:
                event.setCancelled(true);
                event.setResult(Result.DENY);
                return null;
        }
        return stack;
    }

    public ItemStack getResultItem() {
        return result;
    }

    public ItemStack getResultItemNonNull() {
        return result == null ? new ItemStack(Material.AIR, 0) : result;
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

    @Override
    public void setCurrentItem(ItemStack item) {
        event.setCurrentItem(item);
    }

    public void setCurrentItemDescription(String description) {
        ItemMeta meta = getCurrentItem().getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(Arrays.asList(Colorizer.parseColors(description).split("\n")));
        event.getCurrentItem().setItemMeta(meta);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setCursor(ItemStack cursor) {
        event.setCursor(cursor);
    }

    @Override
    public void setResult(Result result) {
        event.setResult(result);
    }
}
