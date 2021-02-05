package com.ruinscraft.cinemadisplays.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PreviewScreenBlockEntity extends BlockEntity {

    public static Identifier IDENT;
    public static BlockEntityType<PreviewScreenBlockEntity> PREVIEW_SCREEN_BLOCK_ENTITY;

    public PreviewScreenBlockEntity() {
        super(PREVIEW_SCREEN_BLOCK_ENTITY);
    }

    public static void register() {
        IDENT = new Identifier("cinemadisplays", "preview_screen_block_entity");
        PREVIEW_SCREEN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, IDENT,
                BlockEntityType.Builder.create(PreviewScreenBlockEntity::new, PreviewScreenBlock.PREVIEW_SCREEN_BLOCK).build(null));
    }

}
