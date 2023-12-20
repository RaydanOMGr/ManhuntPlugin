package me.andreasmelone.manhuntplugin.items.abstracts;

import me.andreasmelone.manhuntplugin.items.TrackCompassItem;

import java.util.Arrays;
import java.util.List;

public class SpecialItems {
    public static final SpecialItem TRACK_COMPASS = new TrackCompassItem();

    /**
     * Returns all the special items instances.
     * @return All the special items instances.
     */
    public static SpecialItem[] getAll() {
        return new SpecialItem[] {TRACK_COMPASS };
    }

    /**
     * Returns all the special items instances as a list.
     * @return All the special items instances as a list.
     */
    public static List<SpecialItem> getAllList() {
        return Arrays.asList(getAll());
    }

    /**
     * Returns all the special items ids.
     * @return All the special items ids.
     */
    public static String[] getAllIds() {
        String[] ids = new String[getAll().length];
        for(int i = 0; i < getAll().length; i++) {
            ids[i] = getAll()[i].getId();
        }
        return ids;
    }

    /**
     * Returns all the special items ids as a list.
     * @return All the special items ids as a list.
     */
    public static List<String> getAllIdsList() {
        return Arrays.asList(getAllIds());
    }

    /**
     * Returns a special item instance by its id.
     * @param id The id of the special item.
     * @return The special item instance.
     */
    public static SpecialItem getById(String id) {
        for(SpecialItem item : getAll()) {
            if(item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static void registerAllRecipes() {
        for(SpecialItem item : getAll()) {
            item.registerRecipe();
        }
    }
}
