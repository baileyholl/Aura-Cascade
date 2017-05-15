package pixlepix.auracascade.block.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;
import pixlepix.auracascade.data.recipe.ProcessorRecipe;
import pixlepix.auracascade.data.recipe.ProcessorRecipeRegistry;
import pixlepix.auracascade.main.AuraUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pixlepix on 12/21/14.
 */
public class ProcessorTile extends ConsumerTile {
    public static int MAX_PROGRESS = 60;
    public static int POWER_PER_PROGRESS = 150;

    public static ItemStack getDoubleResult(ItemStack stack) {
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            String oreName = OreDictionary.getOreName(id);
            if (StringUtils.startsWith(oreName, "ore")) {
                String dustName = StringUtils.replace(oreName, "ore", "dust");
                if (OreDictionary.getOres(dustName).size() != 0) {
                    return OreDictionary.getOres(dustName).get(0).copy();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getMaxProgress() {
        return MAX_PROGRESS;
    }

    @Override
    public int getPowerPerProgress() {
        return POWER_PER_PROGRESS;
    }

    @Override
    public boolean validItemsNearby() {
        int range = 3;
        List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range, range, range)));
        for (EntityItem entityItem : nearbyItems) {
            ItemStack stack = entityItem.getEntityItem();
            if (getDoubleResult(stack) != ItemStack.EMPTY) {
                return true;
            }
        }
        return ProcessorRecipeRegistry.getRecipeFromEntity(nearbyItems, isPrismatic()) != null;
    }

    public boolean isPrismatic() {
        return false;

    }

    public int oreMultFactor() {
        return 2;
    }

    @Override
    public void onUsePower() {
    //    AuraCascade.analytics.eventDesign(isPrismatic() ? "consumerProcessorPrism" : "consumerProcessor", AuraUtil.formatLocation(this));
        int range = 3;
        ItemStack resultStack;
        List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range, range, range)));
        for (EntityItem entityItem : nearbyItems) {
            ItemStack stack = entityItem.getEntityItem();
            if (getDoubleResult(stack) != ItemStack.EMPTY) {
                resultStack = getDoubleResult(stack);
                resultStack.setCount(oreMultFactor());
                stack.shrink(1);
                spawnInWorld(resultStack, entityItem);
                return;
            }
        }
        ProcessorRecipe recipe = ProcessorRecipeRegistry.getRecipeFromEntity(nearbyItems, isPrismatic());
        if (recipe != null) {
            EntityItem spawnNear = nearbyItems.get(0);
            List<ItemStack> ingredients = new ArrayList<ItemStack>(recipe.componentList);
            for (EntityItem entityItem : nearbyItems) {

                ItemStack entityStack = entityItem.getEntityItem();

                Iterator<ItemStack> recipeItemIter = ingredients.iterator();
                while (recipeItemIter.hasNext()) {
                    ItemStack curStack = recipeItemIter.next();
                    if (curStack.getCount() <= entityStack.getCount() && curStack.getItemDamage() == entityStack.getItemDamage() && curStack.getItem() == entityStack.getItem()) {
                        spawnNear = entityItem;
                        entityStack.shrink(curStack.getCount());
                        recipeItemIter.remove();
                        break;
                    }
                }
            }

            spawnInWorld(recipe.result.copy(), spawnNear);
        }
    }

    public boolean spawnInWorld(ItemStack resultStack, EntityItem entityItem) {
        if (resultStack != ItemStack.EMPTY) {
            AuraUtil.respawnItemWithParticles(world, entityItem, resultStack);
            return true;
        }
        return false;
    }
}