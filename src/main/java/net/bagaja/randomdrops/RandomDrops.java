package net.bagaja.randomdrops;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod("randomdropmod")
public class RandomDrops {
    public static final String MOD_ID = "randomdropmod";
    private static final Random RANDOM = new Random();
    private static final List<Item> ITEMS = new ArrayList<>();

    public RandomDrops() {
        MinecraftForge.EVENT_BUS.register(this);

        // Populate the ITEMS list with all registered items
        ForgeRegistries.ITEMS.forEach(item -> {
            if (item != Items.AIR) {
                ITEMS.add(item);
            }
        });
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Level world = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Player player = event.getPlayer();

        if (!world.isClientSide && !player.isCreative()) {
            // Cancel the original drop
            event.setCanceled(true);

            // Drop a random item
            Item randomItem = ITEMS.get(RANDOM.nextInt(ITEMS.size()));
            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    randomItem.getDefaultInstance());
            world.addFreshEntity(itemEntity);

            // Destroy the block
            world.destroyBlock(pos, false);
        }
    }
}