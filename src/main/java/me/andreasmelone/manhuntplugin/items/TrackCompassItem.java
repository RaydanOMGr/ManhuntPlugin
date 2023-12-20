package me.andreasmelone.manhuntplugin.items;

import me.andreasmelone.manhuntplugin.ManhuntPlugin;
import me.andreasmelone.manhuntplugin.items.abstracts.SpecialItem;
import me.andreasmelone.manhuntplugin.util.FullEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Recipe;

public class TrackCompassItem extends SpecialItem {
    @Override
    public String getId() {
        return "trackcompass";
    }

    @Override
    public int getModelDataId() {
        return -1;
    }

    @Override
    public String getUnformattedName() {
        return ManhuntPlugin.getInstance().getI18n().get("track_compass_name");
    }

    @Override
    public String getLore() {
        return null;
    }

    @Override
    public Material getMaterial() {
        return Material.COMPASS;
    }

    @Override
    public Class<? extends Recipe> getRecipeType() {
        return null;
    }

    @Override
    public Material[] getRecipeMaterials() {
        return new Material[0];
    }

    @Override
    public FullEnchantment[] getEnchantments() {
        return new FullEnchantment[] {
                new FullEnchantment() {
                    @Override
                    public Enchantment getEnchantment() {
                        return Enchantment.DURABILITY;
                    }

                    @Override
                    public int getLevel() {
                        return 1;
                    }
                }
        };
    }

    @Override
    public ItemFlag[] getItemFlags() {
        return new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS };
    }
}
