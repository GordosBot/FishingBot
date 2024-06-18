package net.botwithus;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.scene.entities.characters.player.Player;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.imgui.NativeBoolean;
import net.botwithus.rs3.imgui.NativeInteger;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;

import java.util.Random;

public class SkeletonScript extends LoopingScript {

    private BotState botState = BotState.IDLE;
    private boolean someBool = true;
    private Random rand = new Random();
    public Long startTime;
    public NativeBoolean debugMode = new NativeBoolean(false);
    public NativeInteger fishType = new NativeInteger(0);
    public NativeInteger selectedArea = new NativeInteger(0);
    private int animationDeadCount = 0;

    enum BotState {
        IDLE,
        FISHING,
        BANKING,
    }

    public SkeletonScript(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        sgc = new SkeletonScriptGraphicsContext(getConsole(), this);
        startTime = System.currentTimeMillis();
        loadConfiguration();
    }

    @Override
    public void onLoop() {
        LocalPlayer player = Client.getLocalPlayer();
        if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN || botState == BotState.IDLE) {
            Execution.delay(rand.nextLong(3000,7000));
            return;
        }
        switch (botState) {
            case IDLE -> {
                Execution.delay(rand.nextLong(1000,3000));
            }
            case FISHING -> {
                Execution.delay(handleFishing(player));
            }
            case BANKING -> {
                Execution.delay(handleBanking(player));
            }
        }
    }

    private long handleFishing(LocalPlayer player) {
        if (player.getAnimationId() != -1 || player.isMoving())
            animationDeadCount = 0;
        if (player.getAnimationId() == -1 && animationDeadCount > 2) {
            animationDeadCount = 0;
        } else {
            animationDeadCount++;
            return rand.nextInt(450, 710);
        }
        if (Backpack.isFull()) {
            println("Inventory is Full! Going to banking");
            botState = BotState.BANKING;
            return rand.nextInt(1743, 4786);
        }
        SimulateRandomAfk();
        Npc fishingSpot = NpcQuery.newQuery().name("Fishing spot").results().nearest();
        if (fishingSpot != null)
            println("interacted fishing spot: " + fishingSpot.interact("Net"));

        return rand.nextLong(1765, 2875);
    }

    private int handleBanking(LocalPlayer player) {
        if (player.getAnimationId() != -1 || player.isMoving())
            return rand.nextInt(1250, 3425);
        SimulateRandomAfk();
        if (Backpack.isFull()) {
            println("Banking!");
            SceneObject bank = SceneObjectQuery.newQuery().name("Deposit box").option("Deposit-All").results().nearest();
            println("Bank: " + bank);
            if (bank != null) {
                println("Interacted bank: " + bank.interact("Deposit-All"));
            }
        } else {
            botState = BotState.FISHING;
        }
        return rand.nextInt(945, 1668);
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

    public void SimulateRandomAfk() {
        double random = rand.nextDouble();
        if (random * 100.0 > 97) {
            int milliSeconds = rand.nextInt(5000, 25000);
            if (rand.nextDouble() * 100 < 0.5)
                milliSeconds += rand.nextInt(15000, 50000);
            println("Unlucky 3% roll! Simulating human afk for " + milliSeconds / 1000
                    + " seconds.");
            delay(milliSeconds);
        }
    }

    void saveConfiguration() {
        configuration.addProperty("debugMode", String.valueOf(debugMode.get()));
    }

    void loadConfiguration() {
        debugMode.set(Boolean.parseBoolean(configuration.getProperty("debugMode")));
    }
}
