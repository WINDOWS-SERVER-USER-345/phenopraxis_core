package org.bandeng.phenopraxis_core.content.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

/**
 * 通用浓度矿石方块
 *
 * <p>只有一个 BlockState 属性 {@code concentration}（0=低, 1=中, 2=高），决定贴图。
 * 挖掘掉落时，根据浓度等级随机生成所含度，写入铁锭 NBT。</p>
 *
 * <h3>掉落规则：</h3>
 * <ul>
 *   <li>低浓度 → 掉落铁锭 NBT{content: 1~16} → 所含度 16.7%~33.3%</li>
 *   <li>中浓度 → 掉落铁锭 NBT{content: 17~33} → 所含度 33.3%~50%</li>
 *   <li>高浓度 → 掉落铁锭 NBT{content: 34~50} → 所含度 50%~66.7%</li>
 * </ul>
 */
@ParametersAreNonnullByDefault
public class ConcentrationOreBlock extends Block {

    /** 浓度等级：0=低浓度, 1=中浓度, 2=高浓度 */
    public static final IntegerProperty CONCENTRATION = IntegerProperty.create("concentration", 0, 2);

    public ConcentrationOreBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONCENTRATION, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONCENTRATION);
    }

    /** 玩家放置时默认中浓度 */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(CONCENTRATION, 1);
    }

    /**
     * 根据浓度等级设置方块状态
     * @param tier 0=低, 1=中, 2=高
     */
    public BlockState withTier(int tier) {
        return this.defaultBlockState().setValue(CONCENTRATION, Math.max(0, Math.min(2, tier)));
    }

    /**
     * 挖掘掉落 —— 掉落带有所含度 NBT 的铁锭
     * <p>NBT 格式：{@code {phenopraxis:{content:<1~50>, tier:<0|1|2>}}}</p>
     *
     * <p>注意：此方法在 MC 1.20.1 中被标记为已过时，
     * 但 1.20.1 没有可用的替代方法，因此必须使用此方式实现自定义掉落。</p>
     */
    @SuppressWarnings({"deprecation", "removal"})
    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (!(state.getBlock() instanceof ConcentrationOreBlock)) {
            return Collections.emptyList();
        }

        int tier = state.getValue(CONCENTRATION);
        // 根据浓度等级确定所含度范围（1~50）
        int[] range = switch (tier) {
            case 0  -> new int[]{1, 16};    // 低浓度
            case 2  -> new int[]{34, 50};   // 高浓度
            default -> new int[]{17, 33};   // 中浓度
        };
        int minContent = range[0];
        int maxContent = range[1];

        int content = minContent + builder.getLevel().getRandom().nextInt(maxContent - minContent + 1);

        ItemStack ingot = new ItemStack(Items.IRON_INGOT);
        CompoundTag tag = new CompoundTag();
        CompoundTag phenopraxisTag = new CompoundTag();
        phenopraxisTag.putInt("content", content);
        phenopraxisTag.putInt("tier", tier);
        tag.put("phenopraxis", phenopraxisTag);
        ingot.setTag(tag);

        return List.of(ingot);
    }
}
