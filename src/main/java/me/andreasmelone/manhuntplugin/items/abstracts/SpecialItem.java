package me.andreasmelone.manhuntplugin.items.abstracts;

import me.andreasmelone.manhuntplugin.ManhuntPlugin;
import me.andreasmelone.manhuntplugin.util.FullEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public abstract class SpecialItem {
    /**
     * Returns the id of the item.
     * @return The id of the item, a string used to get the item from the item give command.
     */
    
    public abstract String getId(); // Can't be null, this is the ID used to get the item from the item give command

    /**
     * Returns the custom model data number of the item.
     * @return The custom model data number of the item.
     */
    public abstract int getModelDataId(); // Return -1 if you don't want to set custom model data

    /**
     * Returns the unformatted name of the item.
     * Example: "&r&6&lCool&r Item"
     * @return The unformatted name of the item.
     */
    
    public abstract String getUnformattedName(); // Return null if you don't want to set a name

    /**
     * Returns the lore of the item.
     * Example: "This is a cool item.\nIt is very cool."
     * @return The lore of the item.
     */
    
    public abstract String getLore(); // Return null if you don't want to set a lore

    /**
     * Returns the material of the item.
     * Example: Material.DIAMOND_CHESTPLATE
     * @return The material of the item.
     */
    
    public abstract Material getMaterial(); // Can't be null, this is the material of the item

    public abstract Class<? extends Recipe> getRecipeType();

    public abstract Material[] getRecipeMaterials();

    public abstract FullEnchantment[] getEnchantments();

    /**
     * Returns the item flags of the item.
     * @return The item flags of the item.
     */
    public abstract ItemFlag[] getItemFlags(); // Return null if you don't want to set item flags

    /**
     * Returns the formatted name of the item.
     * Example: getUnformattedName() -> "&r&6&lCool&r Item", getName() -> "§r§6§lCool§r Item"
     * @return The formatted name of the item.
     */
    
    public String getName() {
        if(getUnformattedName() == null) return null;
        return ChatColor.translateAlternateColorCodes('&', getUnformattedName());
    }

    /**
     * Returns the ItemStack, so just the item you can give to a player or put in a chest.
     * @return The ItemStack.
     */
    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(getMaterial());
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(ManhuntPlugin.getInstance(), "item_" + getId());
        container.set(key, PersistentDataType.INTEGER, 1);
        if(getModelDataId() != -1) meta.setCustomModelData(getModelDataId());
        if(getName() != null) meta.setDisplayName(getName());
        if(getLore() != null) meta.setLore(Arrays.asList(getLore().split("\n")));
        if(getEnchantments() != null) {
            for(FullEnchantment enchantment : getEnchantments()) {
                meta.addEnchant(enchantment.getEnchantment(), enchantment.getLevel(), true);
            }
        }
        if(getItemFlags() != null) {
            for(ItemFlag itemFlag : getItemFlags()) {
                meta.addItemFlags(itemFlag);
            }
        }
        meta.setUnbreakable(true);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public void registerRecipe() {
        if(getRecipeType() == null) return;
        NamespacedKey key = new NamespacedKey(ManhuntPlugin.getInstance(), getId() + "_recipe");

        ShapelessRecipe recipe = null;
        if (getRecipeType() == ShapelessRecipe.class) {
            recipe = new ShapelessRecipe(key, getItemStack());
            for (Material material : getRecipeMaterials()) {
                recipe.addIngredient(material);
            }
        } else {
            System.out.println("Recipe type " + getRecipeType().getSimpleName() + " not supported!");
        }

        for (Material material : getRecipeMaterials()) {
            System.out.println(material.name());
        }
        ManhuntPlugin.getInstance().getServer().addRecipe(recipe);
    }

    public boolean isSameSpecialItem(ItemStack item) {
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        NamespacedKey key = new NamespacedKey(ManhuntPlugin.getInstance(), "item_" + getId());
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
    }
}
