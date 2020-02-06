package snownee.cuisine.cookware.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import snownee.cuisine.cookware.CookwareModule;
import snownee.cuisine.cookware.tile.AbstractCookwareTile;
import snownee.cuisine.cookware.tile.OvenTile;
import snownee.cuisine.util.ModSlot;

public class OvenContainer extends Container {

    protected AbstractCookwareTile tile;
    protected final PlayerEntity player;

    public OvenContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, CookwareModule.OVEN_TILE.create().getInventory());
    }

    public OvenContainer(int id, PlayerInventory playerInventory, OvenTile tile) {
        this(id, playerInventory, tile.getInventory());
        this.tile = tile;
    }

    public OvenContainer(int id, PlayerInventory playerInventory, IInventory inventory) {
        super(CookwareModule.OVEN_CONTAINER, id);
        assertInventorySize(inventory, 10);
        this.player = playerInventory.player;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new ModSlot(inventory, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        //        this.addSlot(new Slot(inventory, 9, 124, 35) {
        //            @Override
        //            public boolean isItemValid(ItemStack stack) {
        //                return false;
        //            }
        //        });
        this.addSlot(new ModSlot(inventory, 9, 124, 35));

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return !tile.isRemoved() && !(player.getDistanceSq(tile.getPos().getX() + 0.5D, tile.getPos().getY() + 0.5D, tile.getPos().getZ() + 0.5D) > 64.0D);
    }

    public void cook() {
        if (tile != null && !tile.isRemoved()) {
            tile.cookAsItem(player);
        }
    }

}
