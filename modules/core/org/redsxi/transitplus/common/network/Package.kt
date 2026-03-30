package org.redsxi.transitplus.common.network

abstract class Package<P: Package<P>> {
    abstract val type: PackageType<P>




}