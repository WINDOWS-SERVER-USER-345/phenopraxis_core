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
| 赤铁矿 (Hematite) | `phenopraxis_core:hematite_ore` | ❌ 未注册（配置已有，但方块未注册，会导致崩溃！） |

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

## 🔧 技术信息

- **Minecraft 版本**: 1.20.1
- **Forge 版本**: 47.4.20
- **Java 版本**: 17
- **依赖模组**: Patchouli, KubeJS, Rhino, Architectury

## 📁 项目结构

