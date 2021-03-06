package pixlepix.auracascade.main.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import pixlepix.auracascade.AuraCascade;
import pixlepix.auracascade.KeyBindings;
import pixlepix.auracascade.network.PacketAngelJump;

/**
 * Created by localmacaccount on 5/30/15.
 */
public class ClientEventHandler {
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (KeyBindings.jumpKeyBind.isPressed()) {
            AuraCascade.proxy.networkWrapper.sendToServer(new PacketAngelJump(AuraCascade.proxy.getPlayer(), true));
        }
        if (KeyBindings.jumpDownKeyBind.isPressed()) {
            AuraCascade.proxy.networkWrapper.sendToServer(new PacketAngelJump(AuraCascade.proxy.getPlayer(), false));
        }
    }
}
