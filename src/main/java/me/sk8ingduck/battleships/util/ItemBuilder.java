package me.sk8ingduck.battleships.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material, 1);
        itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
    }

    public ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
    }

    public ItemBuilder setDisplayName(String displayName) {
        assert itemMeta != null;
        itemMeta.setDisplayName("§r" + displayName);
        return this;
    }

    public ItemBuilder setLores(String... lores) {
        itemMeta.setLore(Arrays.asList(lores));
        return this;
    }

    public ItemBuilder setPotionColor(Color color) {
        PotionMeta potionmeta = (PotionMeta) itemMeta;
        potionmeta.setColor(color);
        return this;
    }

    public ItemBuilder setPotionEffect(PotionEffect effect) {
        PotionMeta potionmeta = (PotionMeta) itemMeta;
        potionmeta.addCustomEffect(effect, false);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setGlowing() {
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
