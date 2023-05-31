package studio.dreamys;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;

public class AngleUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float get360RotationYaw(float yaw) {
        return (yaw % 360 + 360) % 360;
    }

    public static float get360RotationYaw() {
        return get360RotationYaw(mc.thePlayer.rotationYaw);
    }

    public static float clockwiseDifference(float initialYaw360, float targetYaw360) {
        return get360RotationYaw(targetYaw360 - initialYaw360);
    }

    public static float getActualRotationYaw() { //f3
        return getActualRotationYaw(mc.thePlayer.rotationYaw);
    }

    public static float getActualRotationYaw(float yaw) { //f3
        return yaw > 0 ?
                (yaw % 360 > 180 ? -(180 - (yaw % 360 - 180)) : yaw % 360) :
                (-yaw % 360 > 180 ? (180 - (-yaw % 360 - 180)) : -(-yaw % 360));
    }

    public static float antiClockwiseDifference(float initialYaw360, float targetYaw360) {
        return get360RotationYaw(initialYaw360 - targetYaw360);
    }

    public static float smallestAngleDifference(float initialYaw360, float targetYaw360) {
        return Math.min(clockwiseDifference(initialYaw360, targetYaw360), antiClockwiseDifference(initialYaw360, targetYaw360));
    }


    public static boolean shouldRotateClockwise(float start, float target) {
        return clockwiseDifference(get360RotationYaw(start), target) < 180;
    }



    public static float getClosest() {
        if (get360RotationYaw() < 45 || get360RotationYaw() > 315) {
            return 0f;
        } else if (get360RotationYaw() < 135) {
            return 90f;
        } else if (get360RotationYaw() < 225) {
            return 180f;
        } else {
            return 270f;
        }
    }
    public static float getClosest(float yaw) {
        if (yaw < 45 || yaw > 315) {
            return 0f;
        } else if (yaw < 135) {
            return 90f;
        } else if (yaw < 225) {
            return 180f;
        } else {
            return 270f;
        }
    }


    public static Tuple<Float, Float> getRotation(Vec3 vec3) {
        double diffX = vec3.xCoord - mc.thePlayer.posX;
        double diffY = vec3.yCoord - mc.thePlayer.posY - mc.thePlayer.getEyeHeight();
        double diffZ = vec3.zCoord - mc.thePlayer.posZ;
        return getRotationTo(diffX, diffY, diffZ);
    }

    public static Tuple<Float, Float> getRotation(Vec3 from, Vec3 to) {
        double diffX = from.xCoord - to.xCoord;
        double diffY = from.yCoord - to.yCoord;
        double diffZ = from.zCoord - to.zCoord;
        return getRotationTo(diffX, diffY, diffZ);
    }

    private static Tuple<Float, Float> getRotationTo(double diffX, double diffY, double diffZ) {
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90) * -1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Tuple<>(yaw, pitch);
    }

    public static float wrapAngleTo180(double angle) {
        return (float) (angle - Math.floor(angle / 360.0f + 0.5) * 360.0);
    }

    public static float getAngleDifference(float actualYaw1, float actualYaw2){
        if(actualYaw1 - actualYaw2 > 180) {
            return Math.abs(actualYaw1 - 360 - actualYaw2);
        } else if(actualYaw1 - actualYaw2 < -180){
            return Math.abs(actualYaw2 - 360 - actualYaw1);
        } else return Math.abs(actualYaw1 - actualYaw2);
    }

    public static double getRotationDifference(final Pair<Float, Float> a, final Pair<Float, Float> b) {
        return Math.hypot(getAngleDifference(a.getFirst(), b.getFirst()), a.getSecond() - b.getSecond());
    }

    public static Pair<Float, Float> getYawAndPitch(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        if(predict) {
            if(mc.thePlayer.onGround) {
                eyesPos.addVector(mc.thePlayer.motionX, 0.0, mc.thePlayer.motionZ);
            } else eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);
        }

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new Pair<>(AngleUtils.wrapAngleTo180((double) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                AngleUtils.wrapAngleTo180((double) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
                ));
    }


    public static float getActualYawFrom360(float yaw360) {
        float currentYaw = yaw360;
        if(mc.thePlayer.rotationYaw > yaw360){
            while (mc.thePlayer.rotationYaw - currentYaw < 180 || mc.thePlayer.rotationYaw - currentYaw > 0){
                if(Math.abs(currentYaw + 360 - mc.thePlayer.rotationYaw) < Math.abs(currentYaw - mc.thePlayer.rotationYaw))
                    currentYaw = currentYaw + 360;
                else  break;
            }
        }
        if(mc.thePlayer.rotationYaw < yaw360){
            while (currentYaw - mc.thePlayer.rotationYaw > 180 || mc.thePlayer.rotationYaw - currentYaw < 0){
                if(Math.abs(currentYaw - 360 - mc.thePlayer.rotationYaw) < Math.abs(currentYaw - mc.thePlayer.rotationYaw))
                    currentYaw = currentYaw - 360;
                else  break;
            }
        }
        return currentYaw;


    }
}
