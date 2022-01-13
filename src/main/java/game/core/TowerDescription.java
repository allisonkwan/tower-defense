package game.core;

import game.types.TowerDefenceType;

public class TowerDescription {

    private TowerDefenceType type;
    private int id;
    private int cost;
    private int range;
    private int fireRate;
    private String description;

    public TowerDescription(TowerDefenceType type, int id, int cost,
                            int range, int fireRate, String description) {
        this.type = type;
        this.id = id;
        this.cost = cost;
        this.range = range;
        this.fireRate = fireRate;
        this.description = description;
    }


    public TowerDefenceType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public int getRange() {
        return range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public String getDescription() {
        return description;
    }

}
