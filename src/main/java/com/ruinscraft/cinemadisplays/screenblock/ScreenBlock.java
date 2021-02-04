package com.ruinscraft.cinemadisplays.screenblock;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class ScreenBlock extends Block implements BlockEntityProvider {

    public static Identifier IDENT;
    public static ScreenBlock SCREEN_BLOCK;

    public ScreenBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(-1.0f, 3600000.0F).dropsNothing().nonOpaque());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ScreenBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    public static void register() {
        IDENT = new Identifier("cinemadisplays", "screen");
        SCREEN_BLOCK = new ScreenBlock();

        Registry.register(Registry.BLOCK, IDENT, SCREEN_BLOCK);
    }

}
