package org.redsxi.transitplus.common.network

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation

interface PackageType<P: Package<P>> {

    val codec: Codec<P>
    val id: ResourceLocation


    companion object {
        fun <P: Package<P>> createPackageType(id: ResourceLocation, codec: Codec<P>): PackageType<P> = object: PackageType<P> {
            override val codec: Codec<P> = codec
            override val id: ResourceLocation = id
        }
    }
}