package studio.dreamys;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.util.ChatComponentText;

public class CommandSetRotation extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
@Override
    public String getCommandName() {
        return "setrotation";
    }

    @Override
    public String getCommandUsage(net.minecraft.command.ICommandSender sender) {
        return "/setrotation";
    }

    @Override
    public void processCommand(net.minecraft.command.ICommandSender sender, String[] args) {
        PrettierHUD.rotations.add(new Pair<>(Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch));
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Rotation set!"));
    }
}
