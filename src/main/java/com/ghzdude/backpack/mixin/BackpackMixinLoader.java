package com.ghzdude.backpack.mixin;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class BackpackMixinLoader implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("mixins.ghzbackpacks.json");
    }
}
