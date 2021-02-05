package com.ruinscraft.cinemadisplays.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class PreviewScreenBlock extends Block implements BlockEntityProvider {

    public static Identifier IDENT;
    public static PreviewScreenBlock PREVIEW_SCREEN_BLOCK;

    public PreviewScreenBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(-1.0f, 3600000.0F).dropsNothing().nonOpaque());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new PreviewScreenBlockEntity();
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
        IDENT = new Identifier("cinemadisplays", "preview_screen");
        PREVIEW_SCREEN_BLOCK = new PreviewScreenBlock();

        Registry.register(Registry.BLOCK, IDENT, PREVIEW_SCREEN_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("cinemadisplays", "preview_screen"), new BlockItem(PREVIEW_SCREEN_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    }

}
