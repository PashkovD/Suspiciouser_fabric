package ru.pashkovd.suspiciouser.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.GravelBlock;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GravelBlock.class)
public abstract class GravelBlockMixin extends FallingBlock {
    protected GravelBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getOffHandStack().getItem().equals(Items.BRUSH)) {
            world.setBlockState(pos, Blocks.SUSPICIOUS_GRAVEL.getDefaultState());
            BrushableBlockEntity be = (BrushableBlockEntity) world.getBlockEntity(pos);
            NbtCompound nbt = new NbtCompound();
            nbt.put("item", player.getMainHandStack().writeNbt(new NbtCompound()));
            assert be != null;
            be.readNbt(nbt);
            world.addBlockEntity(be);
            player.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
