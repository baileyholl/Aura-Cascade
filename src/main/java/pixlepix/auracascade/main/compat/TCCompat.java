package pixlepix.auracascade.main.compat;

import pixlepix.auracascade.block.AuraBlock;
import pixlepix.auracascade.registry.BlockRegistry;
import thaumcraft.api.ThaumcraftApi;

/**
 * Created by localmacaccount on 2/27/15.
 */
public class TCCompat {

    public static void postInit() {
        // todo 1.8.8 ThaumcraftApi.portableHoleBlackList.addAll(BlockRegistry.getBlockFromClass(AuraBlock.class));
    }
}
