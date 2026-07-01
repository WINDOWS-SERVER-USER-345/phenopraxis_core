package org.bandeng.phenopraxis_core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.bandeng.phenopraxis_core.Phenopraxis;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class VeinTypeConfig implements ResourceManagerReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<VeinTypeData> VEIN_TYPES = new ArrayList<>();

    // ===== 公开访问方法 =====

    /** 获取所有启用的矿脉类型 */
    public static List<VeinTypeData> getVeinTypes() {
        return new ArrayList<>(VEIN_TYPES);
    }

    /** 根据 ID 获取特定矿脉类型 */
    public static VeinTypeData getVeinType(String id) {
        return VEIN_TYPES.stream()
                .filter(t -> t.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    /** 根据权重随机选择一种矿脉类型 */
    public static VeinTypeData selectRandomType(RandomSource random) {
        if (VEIN_TYPES.isEmpty()) {
            return null;
        }
        int totalWeight = VEIN_TYPES.stream().mapToInt(t -> t.weight).sum();
        int r = random.nextInt(totalWeight);
        int cumulative = 0;
        for (VeinTypeData type : VEIN_TYPES) {
            cumulative += type.weight;
            if (r < cumulative) {
                return type;
            }
        }
        return VEIN_TYPES.get(0);
    }

    // ===== 资源重载监听 =====

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        VEIN_TYPES.clear();
        try {
            var resource = resourceManager.getResource(
                    new ResourceLocation(Phenopraxis.MODID, "config/vein_types.json")
            );
            if (resource.isPresent()) {
                try (var reader = new InputStreamReader(resource.get().open())) {
                    VeinTypeList list = GSON.fromJson(reader, VeinTypeList.class);
                    for (VeinTypeData data : list.vein_types) {
                        if (data.enabled) {
                            VEIN_TYPES.add(data);
                        }
                    }
                }
                Phenopraxis.LOGGER.info("Loaded {} vein types", VEIN_TYPES.size());
            } else {
                Phenopraxis.LOGGER.warn("vein_types.json not found, using empty list");
            }
        } catch (Exception e) {
            Phenopraxis.LOGGER.error("Failed to load vein_types.json", e);
        }
    }

    // ===== 数据类 =====

    public static class VeinTypeList {
        public List<VeinTypeData> vein_types = new ArrayList<>();
    }

    public static class VeinTypeData {
        public String id;
        @SerializedName("ore_block")
        public String oreBlock;
        public int weight;
        @SerializedName("min_height")
        public int minHeight;
        @SerializedName("max_height")
        public int maxHeight;
        @SerializedName("core_radius")
        public int coreRadius;
        @SerializedName("core_density")
        public double coreDensity;
        @SerializedName("edge_radius")
        public int edgeRadius;
        @SerializedName("edge_density")
        public double edgeDensity;
        @SerializedName("min_grade")
        public double minGrade;
        @SerializedName("max_grade")
        public double maxGrade;
        @SerializedName("surface_indicator")
        public String surfaceIndicator;
        @SerializedName("center_indicator")
        public String centerIndicator;
        public String color;
        public boolean enabled;
        /** 生物群系标签过滤（可选），为空则不限制生物群系。例如 ["minecraft:is_mountain", "minecraft:is_river"] */
        @SerializedName("biome_tags")
        public List<String> biomeTags = Collections.emptyList();

        // ===== 便捷方法 =====

        public Block getOreBlock() {
            ResourceLocation loc = ResourceLocation.tryParse(oreBlock);
            if (loc == null) return null;
            return ForgeRegistries.BLOCKS.getValue(loc);
        }

        public Block getSurfaceIndicatorBlock() {
            ResourceLocation loc = ResourceLocation.tryParse(surfaceIndicator);
            if (loc == null) return null;
            return ForgeRegistries.BLOCKS.getValue(loc);
        }

        public Block getCenterIndicatorBlock() {
            ResourceLocation loc = ResourceLocation.tryParse(centerIndicator);
            if (loc == null) return null;
            return ForgeRegistries.BLOCKS.getValue(loc);
        }

        public double randomGrade(RandomSource random) {
            return minGrade + random.nextDouble() * (maxGrade - minGrade);
        }

        /** 检查所有方块是否有效 */
        public boolean isValid() {
            return getOreBlock() != null && getSurfaceIndicatorBlock() != null && getCenterIndicatorBlock() != null;
        }
    }
}