package org.bandeng.phenopraxis_core.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.bandeng.phenopraxis_core.config.VeinTypeConfig;
import org.bandeng.phenopraxis_core.world.gen.VeinRegistry;
import org.bandeng.phenopraxis_core.world.gen.VeinRegistry.VeinEntry;

import java.util.List;

/**
 * /findvein <矿脉类型> —— 寻找最近的指定类型矿脉
 *
 * <p>用法：{@code /findvein magnetite} 或 {@code /findvein hematite}</p>
 * <p>会显示最近矿脉的坐标、距离和方向。</p>
 */
public class FindVeinCommand {

    /** 自动补全：从 vein_types.json 中读取所有已启用的矿脉 ID */
    private static final SuggestionProvider<CommandSourceStack> VEIN_TYPE_SUGGESTION =
            (context, builder) -> {
                List<String> ids = VeinTypeConfig.getVeinTypes().stream()
                        .map(t -> t.id)
                        .toList();
                return SharedSuggestionProvider.suggest(ids, builder);
            };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("findvein")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .suggests(VEIN_TYPE_SUGGESTION)
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    String type = StringArgumentType.getString(context, "type");
                                    return findNearestVein(source, type);
                                })
                        )
        );
    }

    private static int findNearestVein(CommandSourceStack source, String type) {
        BlockPos playerPos = BlockPos.containing(source.getPosition());

        VeinEntry nearest = VeinRegistry.findNearest(playerPos, type);

        if (nearest == null) {
            source.sendFailure(Component.literal("§c未找到类型为 §e" + type + " §c的矿脉（可能还未生成）"));
            return 0;
        }

        int dx = nearest.center.getX() - playerPos.getX();
        int dy = nearest.center.getY() - playerPos.getY();
        int dz = nearest.center.getZ() - playerPos.getZ();
        double distance = Math.sqrt(nearest.center.distSqr(playerPos));

        // 判断水平方向
        String direction = getDirection(dx, dz);

        source.sendSuccess(() -> Component.literal(
                "§a最近矿脉 §e" + type + "§a："
                        + "\n  §7坐标：§f" + nearest.center.getX() + ", " + nearest.center.getY() + ", " + nearest.center.getZ()
                        + "\n  §7距离：§f" + String.format("%.1f", distance) + " 格"
                        + "\n  §7方向：§f" + direction
                        + "\n  §7矿石数：§f" + nearest.totalOres
                        + "\n  §7品位：§f" + String.format("%.1f%%", nearest.grade * 100)
        ), false);

        return 1;
    }

    /**
     * 根据 X/Z 偏移计算大致方向（8 方位）
     */
    private static String getDirection(int dx, int dz) {
        double angle = Math.toDegrees(Math.atan2(-dx, dz));
        if (angle < 0) angle += 360;

        if (angle < 22.5 || angle >= 337.5) return "北 (Z-)";
        if (angle < 67.5) return "东北(X+ Z-)";
        if (angle < 112.5) return "东 (X+)";
        if (angle < 157.5) return "东南(X+ Z+)";
        if (angle < 202.5) return "南 (Z+)";
        if (angle < 247.5) return "西南(X- Z+)";
        if (angle < 292.5) return "西 (X-)";
        return "西北(X- Z-)";
    }
}
