// Copy and Modified From https://github.com/irtimaled/BoundingBoxOutlineReloaded
// The origin file is licenced under MIT license.

package com.zjinja.mcmod.zry_client_utils_mod.utils;

import com.irtimaled.bbor.common.MathHelper;
import com.irtimaled.bbor.common.TypeHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Coords {
    private final int x;
    private final int y;
    private final int z;

    public Coords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coords(double x, double y, double z) {
        this.x = BBORMathHelper.floor(x);
        this.y = BBORMathHelper.floor(y);
        this.z = BBORMathHelper.floor(z);
    }

    public Coords(Vec3i pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Coords(Vec3d pos) {
        this(pos.x, pos.y, pos.z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return TypeHelper.combineHashCodes(z, y, x);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coords other = (Coords) obj;
        return getX() == other.getX() &&
                getY() == other.getY() &&
                getZ() == other.getZ();

    }
}

