package ru.pashkovd.suspiciouser.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockEntityMixin extends BlockEntity {
    @Shadow
    private ItemStack item;

    @Shadow
    public abstract void generateItem(PlayerEntity player);

    @Shadow
    private @Nullable Direction hitDirection;

    public BrushableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @author PashkovD
     * @reason delete random
     */
    @Overwrite
    private void spawnItem(PlayerEntity player) {
        if (this.world == null || this.world.getServer() == null) {
            return;
        }
        this.generateItem(player);
        if (!this.item.isEmpty()) {
            double d = EntityType.ITEM.getWidth();
            double e = 1.0 - d;
            double f = d / 2.0;
            Direction direction = Objects.requireNonNullElse(this.hitDirection, Direction.UP);
            BlockPos blockPos = this.pos.offset(direction, 1);
            double g = (double) blockPos.getX() + 0.5 * e + f;
            double h = (double) blockPos.getY() + 0.5 + (double) (EntityType.ITEM.getHeight() / 2.0f);
            double i = (double) blockPos.getZ() + 0.5 * e + f;
            ItemEntity itemEntity = new ItemEntity(this.world, g, h, i, this.item);
            itemEntity.setVelocity(Vec3d.ZERO);
            this.world.spawnEntity(itemEntity);
            this.item = ItemStack.EMPTY;
        }
    }

}
