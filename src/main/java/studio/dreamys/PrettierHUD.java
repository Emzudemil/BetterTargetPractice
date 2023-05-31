package studio.dreamys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = PrettierHUD.MODID, version = PrettierHUD.VERSION)

public class PrettierHUD {
    public static final String MODID = "prettierhud";
    public static final String VERSION = "1.0";

    private KeyBinding keyBinding;
    public static final List<Pair<Float, Float>> rotations = new ArrayList<>();
    private int currentRotation = -1;
    private long startTime = -1;

    private final Rotation rotator = new Rotation();

    @Mod.EventHandler
    public void preInit(FMLInitializationEvent event) {
        keyBinding = new KeyBinding("key.prettierhud.toggle", 0, "key.categories.prettierhud");
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandSetRotation());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (keyBinding.isPressed()) {
            currentRotation = 0;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("Rotation started!"));
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        if (rotations.size() == 0) {
            return;
        }

        if (currentRotation == -1)
            return;

        if(startTime == -1) {
            startTime = System.currentTimeMillis();
        }
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
        if(rotator.completed) {
            // Send Left Click
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.onTick(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode());
            if(currentRotation == rotations.size() - 1) {
                currentRotation = -1;
                startTime = -1;
                Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("Rotation ended!"));
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
                return;
            }
            currentRotation++;
            rotator.easeTo(rotations.get(currentRotation).getFirst(), rotations.get(currentRotation).getSecond(), 400+(Math.round(Math.random()*200)));
        }
        rotator.update();
    }
}
