package net.botwithus;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
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
    private Random random = new Random();
    public Long startTime;
    public NativeBoolean debugMode = new NativeBoolean(false);
    public NativeInteger fishType = new NativeInteger(0);
    public NativeInteger selectedArea = new NativeInteger(0);

    enum BotState {
        IDLE,
        FISHING,
        BANKING,
        //...
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
            Execution.delay(random.nextLong(3000,7000));
            return;
        }
        switch (botState) {
            case IDLE -> {
                println("We're idle!");
                Execution.delay(random.nextLong(1000,3000));
            }
            case FISHING -> {
                Execution.delay(handleFishing(player));
            }
            case BANKING -> {
                println("We're banking!");
            }
        }
    }

    private long handleFishing(LocalPlayer player) {
        println("Starting fishing!");
        if (Interfaces.isOpen(1251)) {
            println("Waiting for fishing progress! 1251 interface is open.");
            return random.nextLong(250, 1500);
        }
        //if our inventory is full, lets bank.
        if (Backpack.isFull()) {
            println("Going to banking state!");
            botState = BotState.BANKING;
            return random.nextLong(250,1500);
        }
        //click my tree, mine my rock, etc...
        SceneObject fishingSpot = SceneObjectQuery.newQuery().name("Fish").option("Net").results().nearest();
        println("Found fishing spot: " + fishingSpot);
        if (fishingSpot != null) {
            fishingSpot.interact("Net");
        }
        return random.nextLong(1500,3000);
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

    public boolean isSomeBool() {
        return someBool;
    }

    public void setSomeBool(boolean someBool) {
        this.someBool = someBool;
    }

    void saveConfiguration() {
        configuration.addProperty("debugMode", String.valueOf(debugMode.get()));
    }

    void loadConfiguration() {
        debugMode.set(Boolean.parseBoolean(configuration.getProperty("debugMode")));
    }
}
