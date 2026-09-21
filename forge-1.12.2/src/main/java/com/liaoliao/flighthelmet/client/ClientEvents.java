package com.liaoliao.flighthelmet.client;

import com.liaoliao.flighthelmet.FlightHelmetMod;
import com.liaoliao.flighthelmet.FlightHelmetSettings;
import com.liaoliao.flighthelmet.NicePickaxeItem;
import com.liaoliao.flighthelmet.RingHelper;
import com.liaoliao.flighthelmet.network.ModNetwork;
import com.liaoliao.flighthelmet.network.PacketSearchContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInput;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = FlightHelmetMod.MOD_ID, value = Side.CLIENT)
public final class ClientEvents {
    private static ItemStack lastTooltipItem = ItemStack.EMPTY;
    private static boolean settingsKeyDown;
    private static boolean searchKeyDown;

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player == null) {
            return;
        }
        ContainerSearchClient.tick();
        if (!Mouse.isButtonDown(1)) {
            NicePickaxeItem.resetClientRightClick();
        }

        boolean settingsDown = isKeyHeld(PigThingsKeys.OPEN_SETTINGS);
        if (settingsDown && !settingsKeyDown) {
            openSettingsScreen(minecraft, player);
        }
        settingsKeyDown = settingsDown;

        boolean searchDown = isKeyHeld(PigThingsKeys.SEARCH_CONTAINER);
        if (searchDown && !searchKeyDown) {
            startContainerSearch(minecraft, player);
        }
        searchKeyDown = searchDown;

        applyNoInertia(player);
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty()) {
            lastTooltipItem = event.getItemStack().copy();
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        ContainerSearchClient.render();
    }

    private static void openSettingsScreen(Minecraft minecraft, EntityPlayerSP player) {
        if (minecraft.currentScreen != null) {
            return;
        }
        ItemStack pickaxe = getHeldPickaxe(player);
        if (!pickaxe.isEmpty()) {
            minecraft.displayGuiScreen(new NicePickaxeSettingsScreen(pickaxe));
            return;
        }
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helmet.getItem() == FlightHelmetMod.FLIGHT_HELMET) {
            minecraft.displayGuiScreen(new FlightHelmetScreen(helmet));
            return;
        }
        ItemStack ring = RingHelper.getEquippedRing(player);
        if (!ring.isEmpty()) {
            minecraft.displayGuiScreen(new RingSettingsScreen(ring));
        }
    }

    /**
     * 1.12.2 的 KeyBinding.isKeyDown() 读的是 pressed 字段（由 Minecraft 每 tick 用 setKeyBindState 维护），
     * 而 MC 只在没有界面打开时才更新它 —— 开着箱子/背包界面按 Y 会永远是 false。
     * 这里直接轮询物理按键（仍按绑定后的 keyCode，改键后同样有效）。
     * F3 是原版调试修饰键：命中组合键（如 F3+G 区块显示）时原版不再把该键交给普通按键路径，
     * 物理轮询却照样读到按下状态，所以 F3 按住期间不认这两个绑定。
     */
    private static boolean isKeyHeld(KeyBinding binding) {
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            return false;
        }
        int keyCode = binding.getKeyCode();
        if (keyCode == 0) {
            return false;
        }
        return keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
    }

    private static void startContainerSearch(Minecraft minecraft, EntityPlayerSP player) {
        // 按键绑定界面（原版 GuiControls，整合包里的 GuiNewControls 也继承它）用 keyTyped 把下一个按键
        // 吃掉当作新的键位绑定，这个按键不属于我们；抢过来的话界面会被关掉并立刻开始搜索。
        if (minecraft.currentScreen instanceof GuiChat
                || minecraft.currentScreen instanceof GuiControls
                || ItemManagerHoverResolver.isSearchFieldFocused()) {
            return;
        }
        // 与 1.21 线一致的优先级：物品管理器悬停 > 容器槽位 > 上一次的提示物品。
        ItemStack target = ItemManagerHoverResolver.getHoveredItem();
        boolean openedContainer = minecraft.currentScreen instanceof GuiContainer
                && !(minecraft.currentScreen instanceof GuiInventory);
        if (target.isEmpty() && minecraft.currentScreen instanceof GuiContainer) {
            Slot slot = ((GuiContainer) minecraft.currentScreen).getSlotUnderMouse();
            if (slot != null && slot.getHasStack()) {
                target = slot.getStack().copy();
            }
        }
        if (target.isEmpty() && !lastTooltipItem.isEmpty()) {
            target = lastTooltipItem.copy();
        }
        FlightHelmetMod.LOGGER.info("Container search requested: {} x{}",
                target.isEmpty() ? "<empty>" : target.getDisplayName(), target.getCount());
        if (target.isEmpty()) {
            player.sendStatusMessage(new TextComponentTranslation("message.pigthings.search_no_item"), true);
            return;
        }
        if (minecraft.currentScreen != null) {
            // 关容器界面必须走原版路径：EntityPlayerSP.closeScreen() 会先发 CPacketCloseWindow，
            // 服务端收到才会执行 openContainer.onContainerClosed（工作台据此把合成格里的物品丢回玩家）。
            // 直接 displayGuiScreen(null) 只关客户端界面，服务端容器继续开着，里面的物品会在
            // 之后打开别的界面、openContainer 被替换时被静默丢弃（1.7.5 及之前就是这个行为）。
            if (player.openContainer != null && player.openContainer != player.inventoryContainer) {
                player.closeScreen();
            } else {
                minecraft.displayGuiScreen(null);
            }
        }
        player.sendStatusMessage(new TextComponentTranslation("message.pigthings.search_started"), true);
        ModNetwork.CHANNEL.sendToServer(new PacketSearchContainer(target, openedContainer));
    }

    private static ItemStack getHeldPickaxe(EntityPlayerSP player) {
        ItemStack mainHand = player.getHeldItemMainhand();
        if (mainHand.getItem() instanceof NicePickaxeItem) {
            return mainHand;
        }
        ItemStack offHand = player.getHeldItemOffhand();
        return offHand.getItem() instanceof NicePickaxeItem ? offHand : ItemStack.EMPTY;
    }

    private static void applyNoInertia(EntityPlayerSP player) {
        if (!player.capabilities.isFlying) {
            return;
        }
        // 与 Forge 版一致：戴着头盔时只认头盔的设置，戒指整套被忽略（不回落）。
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack equipment = helmet.getItem() == FlightHelmetMod.FLIGHT_HELMET
                ? helmet
                : RingHelper.getEquippedRing(player);
        if (equipment.isEmpty() || !FlightHelmetSettings.hasNoInertia(equipment)) {
            return;
        }
        MovementInput input = player.movementInput;
        boolean hasHorizontalInput = Math.abs(input.moveStrafe) > 0.001F || Math.abs(input.moveForward) > 0.001F;
        boolean hasVerticalInput = input.jump || input.sneak;
        if (!hasHorizontalInput) {
            player.motionX = 0.0D;
            player.motionZ = 0.0D;
        }
        if (!hasVerticalInput) {
            player.motionY = 0.0D;
        }
    }
}
