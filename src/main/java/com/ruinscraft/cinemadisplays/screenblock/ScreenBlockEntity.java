package com.ruinscraft.cinemadisplays.screenblock;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ScreenBlockEntity extends BlockEntity {

    public static Identifier IDENT;
    public static BlockEntityType<ScreenBlockEntity> SCREEN_BLOCK_ENTITY;

    public ScreenBlockEntity() {
        super(SCREEN_BLOCK_ENTITY);
    }

    public static void register() {
        IDENT = new Identifier("cinemadisplays", "screen_block_entity");
        SCREEN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, IDENT,
                BlockEntityType.Builder.create(ScreenBlockEntity::new, ScreenBlock.SCREEN_BLOCK).build(null));
    }

}
