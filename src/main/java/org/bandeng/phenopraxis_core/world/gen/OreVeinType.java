package org.bandeng.phenopraxis_core.world.gen;

import java.util.Random;

public enum OreVeinType {
    IRON("iron_vein", 0.30, 0.70),
    COPPER("copper_vein", 0.40, 0.90),
    TIN("tin_vein", 0.30, 0.60),
    GOLD("gold_vein", 0.10, 0.40),
    SILVER("silver_vein", 0.20, 0.50),
    COAL("coal_vein", 0.50, 0.90);

    public final String name;
    public final double minGrade;
    public final double maxGrade;

    OreVeinType(String name, double minGrade, double maxGrade) {
        this.name = name;
        this.minGrade = minGrade;
        this.maxGrade = maxGrade;
    }

    public double randomGrade(Random rand) {
        return minGrade + rand.nextDouble() * (maxGrade - minGrade);
    }
}