# Phenopraxis Core

> 一个让 Minecraft 矿业更科学的 Forge 模组

## 📖 简介

Phenopraxis Core 是一个 Minecraft 1.20.1 的 Forge 模组，核心目标是打造**更科学、更真实的矿业科技线**。
模组引入了**椭球体矿脉生成系统**，矿物不再随机散布，而是像现实地质一样形成有规律的矿脉——中心密集、边缘稀疏，并且会在地表留下线索供玩家勘探。

## 🎮 当前功能

### 1. 科学矿脉生成
- 矿脉呈**椭球体**形状，核心区矿石密集，边缘区逐渐稀疏
- 每种矿物有独立的配置：生成高度、核心/边缘半径和密度、品位范围
- 矿脉之间保持安全距离，不会互相重叠

### 2. 地表勘探线索
- 矿脉正上方的地表会自动生成**标识物**（散落的碎屑 + 中心标记）
- 玩家可以通过观察地表来发现地下的矿脉

### 3. 数据驱动配置
- 所有矿物类型通过 JSON 文件配置，无需修改代码
- 全局参数（间距、概率等）通过 TOML 配置文件调整

## 📦 已注册的矿物

| 矿物 | ID | 状态 |
|------|-----|------|
| 磁铁矿 (Magnetite) | `phenopraxis_core:magnetite_ore` | ✅ 已注册 |
| 赤铁矿 (Hematite) | `phenopraxis_core:hematite_ore` | ✅ 已注册（仅限山脉和河流生物群系生成） |

## ⚙️ 配置说明

### 全局配置（TOML）
配置文件路径：`config/phenopraxis_core/vein_gen.toml`

| 参数 | 说明 | 默认值 | 范围 |
|------|------|--------|------|
| `spacing` | 矿脉网格间距（格） | 400 | 100 ~ 1000 |
| `min_vein_distance` | 两个矿脉之间的最小距离（格） | 300 | 100 ~ 800 |
| `vein_generation_chance` | 每个网格点生成矿脉的概率 | 0.35 | 0.01 ~ 1.0 |

### 矿物类型配置（JSON）
配置文件路径：`data/phenopraxis_core/config/vein_types.json`

每种矿物的字段说明：

| 字段 | 说明 | 类型 |
|------|------|------|
| `id` | 矿物唯一标识 | 字符串 |
| `ore_block` | 矿石方块 ID | 字符串 |
| `weight` | 随机选择权重（越大越常见） | 整数 |
| `min_height` | 最低生成高度 | 整数 |
| `max_height` | 最高生成高度 | 整数 |
| `core_radius` | 核心区半径（格） | 整数 |
| `core_density` | 核心区填充密度 | 0~1 小数 |
| `edge_radius` | 边缘区半径（格） | 整数 |
| `edge_density` | 边缘区填充密度 | 0~1 小数 |
| `min_grade` | 最低品位 | 0~1 小数 |
| `max_grade` | 最高品位 | 0~1 小数 |
| `surface_indicator` | 地表标识物方块 | 字符串 |
| `center_indicator` | 中心标识物方块 | 字符串 |
| `color` | 颜色标识 | 字符串 |
| `enabled` | 是否启用 | 布尔值 |
| `biome_tags` | 生物群系标签过滤（可选，为空则不限制） | 字符串数组 |

## 🔧 技术信息

- **Minecraft 版本**: 1.20.1
- **Forge 版本**: 47.4.20
- **Java 版本**: 17
- **映射表**: Parchment 2023.06.26-1.20.1
- **依赖模组**: Patchouli, KubeJS, Rhino, Architectury API

## 🏗️ 技术架构

### 三层结构：Feature → Placer → Config

```
数据包 JSON                     Java 代码
─────────────                  ──────────
ore_vein.json (ConfiguredFeature)
  ↓ 引用
ore_vein_placed.json (PlacedFeature)
  ↓ 引用
add_ore_vein.json (BiomeModifier) → 决定在哪些生物群系生成
```

**运行时流程：**
1. Minecraft 世界生成器触发 `OreVeinFeature.place()`
2. `OreVeinFeature` 委托给 `OreVeinPlacer.placeVeins()`
3. `OreVeinPlacer` 从 `VeinTypeConfig` 读取 JSON 配置，随机选择矿脉类型
4. 生成椭球体矿脉，根据距离分配浓度等级（高/中/低）
5. `SurfaceIndicator` 在地表放置标识物
6. `VeinRegistry` 记录矿脉位置（防重叠 + 命令查询）

### 代码模块一览

| 包/类 | 职责 |
|-------|------|
| `Phenopraxis.java` | 模组主入口，注册所有组件 |
| `config/VeinGenConfig.java` | TOML 全局配置（间距、概率） |
| `config/VeinTypeConfig.java` | JSON 矿脉类型配置（资源重载监听） |
| `content/block/ConcentrationOreBlock.java` | 浓度矿石方块（3 级浓度） |
| `init/ModBlocks.java` | 方块注册表 |
| `init/ModItems.java` | 物品注册表 |
| `init/ModFeatures.java` | Feature 注册表 |
| `init/ModCreativeModeTabs.java` | 创造模式标签页 |
| `world/gen/OreVeinFeature.java` | 自定义 Feature 入口 |
| `world/gen/OreVeinPlacer.java` | 矿脉放置主逻辑（椭球体生成） |
| `world/gen/SurfaceIndicator.java` | 地表标识物生成 |
| `world/gen/VeinRegistry.java` | 矿脉注册表（内存缓存 + 查询） |
| `core/command/FindVeinCommand.java` | /findvein 命令 |

## 📁 项目结构

```
src/main/
├── java/org/bandeng/phenopraxis_core/
│   ├── Phenopraxis.java              # 模组主入口
│   ├── config/
│   │   ├── VeinGenConfig.java        # TOML 全局配置
│   │   └── VeinTypeConfig.java       # JSON 矿脉类型 + 资源重载
│   ├── content/block/
│   │   └── ConcentrationOreBlock.java # 浓度矿石方块
│   ├── core/command/
│   │   └── FindVeinCommand.java       # /findvein 命令
│   ├── init/
│   │   ├── ModBlocks.java            # 方块注册
│   │   ├── ModItems.java             # 物品注册
│   │   ├── ModFeatures.java          # Feature 注册
│   │   └── ModCreativeModeTabs.java  # 创造标签页
│   └── world/gen/
│       ├── OreVeinFeature.java       # 自定义 Feature
│       ├── OreVeinPlacer.java        # 矿脉放置逻辑
│       ├── SurfaceIndicator.java     # 地表标识
│       └── VeinRegistry.java         # 矿脉注册表
└── resources/
    ├── META-INF/mods.toml
    ├── data/
    │   ├── minecraft/tags/blocks/     # 方块标签（需要钻石工具、可挖掘）
    │   ├── phenopraxis_core/
    │   │   ├── config/vein_types.json # ★ 矿脉类型配置（数据驱动核心）
    │   │   ├── forge/biome_modifier/  # 生物群系修改器
    │   │   └── worldgen/              # 世界生成配置
    │   └── ...
    └── assets/phenopraxis_core/
        ├── blockstates/               # 方块状态定义
        ├── models/                    # 方块/物品模型
        ├── textures/                  # 贴图
        └── lang/                      # 语言文件
```

## 🔍 调试命令

| 命令 | 说明 |
|------|------|
| `/findvein magnetite` | 查找最近的磁铁矿脉 |
| `/findvein hematite` | 查找最近的赤铁矿脉 |

### 日志查看
- 模组日志前缀：`Phenopraxis Core`
- 关键日志位置：`run/logs/latest.log`
- 矿脉生成日志：搜索 `"Placed"` 关键字
- 配置加载日志：搜索 `"Loaded"` + `"vein types"`

## 🚀 如何新增矿脉类型

> **核心原则：只改 JSON，不动 Java 代码！**

### 步骤：

**1. 在 `vein_types.json` 中添加新条目：**

```json
{
  "id": "chalcopyrite",
  "ore_block": "phenopraxis_core:chalcopyrite_ore",
  "weight": 2,
  "min_height": 5,
  "max_height": 40,
  "core_radius": 8,
  "core_density": 0.75,
  "edge_radius": 15,
  "edge_density": 0.25,
  "min_grade": 0.40,
  "max_grade": 0.65,
  "surface_indicator": "minecraft:yellow_concrete",
  "center_indicator": "minecraft:yellow_wool",
  "color": "yellow",
  "enabled": true
}
```

**2. 在 Java 代码中注册对应方块：**

在 `ModBlocks.java` 中添加：
```java
public static final RegistryObject<ConcentrationOreBlock> CHALCOPYRITE_ORE = 
    BLOCKS.register("chalcopyrite_ore",
        () -> new ConcentrationOreBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .strength(3.0f, 3.0f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
        )
    );
```

**3. 在 `ModItems.java` 中注册物品：**
```java
public static final RegistryObject<BlockItem> CHALCOPYRITE_ORE_ITEM = 
    ITEMS.register("chalcopyrite_ore",
        () -> new BlockItem(ModBlocks.CHALCOPYRITE_ORE.get(), new Item.Properties())
    );
```

**4. 添加资源文件：**
- `blockstates/chalcopyrite_ore.json` — 方块状态
- `models/block/chalcopyrite_ore_low.json` — 低浓度模型
- `models/block/chalcopyrite_ore_medium.json` — 中浓度模型
- `models/block/chalcopyrite_ore_high.json` — 高浓度模型
- `models/item/chalcopyrite_ore.json` — 物品模型
- `textures/block/chalcopyrite_*.png` — 贴图

**5. 在 `ModCreativeModeTabs.java` 中添加到创造标签页**

**6. 重启游戏或执行 `/reload` 使配置生效**

## ⚠️ 已知问题

1. ~~`OreVeinPlacer.java` 有硬编码的生物群系过滤~~ ✅ 已修复，改为 JSON `biome_tags` 字段驱动
2. ~~`vein_gen.json` 文件放在了 Java 源码目录中~~ ✅ 已删除遗留文件
3. ~~`ConcentrationOreBlock` 掉落物 NBT 未使用命名空间前缀~~ ✅ 已修复为 `{phenopraxis:{content, tier}}`

## 📝 开发备忘

- 构建命令：`./gradlew build`
- 运行客户端：`./gradlew runClient`
- 数据生成：`./gradlew runData`
- 配置文件支持热重载：修改 `vein_gen.toml` 后无需重启
- JSON 配置需要 `/reload` 或重新进入世界才能生效