package me.sk8ingduck.battleships.villagershop;

public class GuiManager {

    public static MainMenuGui mainMenuGui;
    public static WeaponsGui weaponsGui;
    public static FoodGui foodGui;
    public static MiningGui miningGui;
    public static ArmorGui armorGui;
    public static HealingGui healingGui;
    public static UpgradesGui upgradesGui;
    public static ExtrasGui extrasGui;

    public static void init() {
        mainMenuGui = new MainMenuGui();
        weaponsGui = new WeaponsGui();
        foodGui = new FoodGui();
        miningGui = new MiningGui();
        armorGui = new ArmorGui();
        healingGui = new HealingGui();
        upgradesGui = new UpgradesGui();
        extrasGui = new ExtrasGui();
    }
}
