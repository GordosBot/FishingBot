package net.botwithus;

import net.botwithus.enums.Fish;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class SkeletonScriptGraphicsContext extends ScriptGraphicsContext {

    private SkeletonScript script;

    public SkeletonScriptGraphicsContext(ScriptConsole scriptConsole, SkeletonScript script) {
        super(scriptConsole);
        this.script = script;
    }

    @Override
    public void drawSettings() {
        if (ImGui.Begin("FishingBotV1", ImGuiWindowFlag.None.getValue())) {
            if (ImGui.BeginTabBar("Bar", ImGuiWindowFlag.None.getValue())) {
                if (ImGui.BeginTabItem("Settings", ImGuiWindowFlag.None.getValue())) {
                    ImGui.Text("Bot State: " + script.getBotState());
                    if (ImGui.Button("Start!")) {
                        script.setBotState(SkeletonScript.BotState.FISHING);
                        script.saveConfiguration();
                    }
                    ImGui.SameLine();
                    if (ImGui.Button("Stop!")) {
                        script.setBotState(SkeletonScript.BotState.IDLE);
                        script.saveConfiguration();
                    }
                    ImGui.SameLine();
                    script.debugMode.set(ImGui.Checkbox("Debug mode", script.debugMode.get()));
                    handleConfigChange();

                    String[] areas = Fish.getAreas();
                    ImGui.Combo("Area", script.selectedArea, areas);
                    handleConfigChange();

                    String selectedArea = areas[script.selectedArea.get()];
                    String[] fishesInArea = Fish.getFishesByArea(selectedArea);
                    ImGui.Combo("Fish", script.fishType, fishesInArea);
                    handleConfigChange();

                    ImGui.EndTabItem();
                }
                if (ImGui.BeginTabItem("Stats", ImGuiWindowFlag.None.getValue())) {
                    long elapsedTime = System.currentTimeMillis() - script.startTime;
                    long seconds = elapsedTime / 1000 % 60;
                    long minutes = elapsedTime / (1000 * 60) % 60;
                    long hours = elapsedTime / (1000 * 60 * 60) % 24;
                    ImGui.Text(String.format("Runtime: %02d:%02d:%02d%n", hours, minutes, seconds));
                    ImGui.Text("Level: " + Skills.FISHING.getActualLevel());
                    ImGui.Text("Levels gained: " + script.levelsGained);
                    ImGui.Text("ETA: %s", script.eta);
                    ImGui.Separator();
                    ImGui.Text("Xp gained: %,d", script.xpGained);
                    ImGui.Text("Xp per hour: %,d", script.xpPerHour);
                    ImGui.Separator();
                    ImGui.Text("Total fish caught: " + script.totalFishCaught);
                    ImGui.Text("Total fish caught per hour: %,d", script.totalFishCaughtPerHour);
                    ImGui.EndTabItem();
                    ImGui.EndTabItem();
                }
                ImGui.EndTabBar();
            }
            ImGui.End();
        }
    }

    void handleConfigChange() {
        if (ImGui.IsItemClicked(ImGui.MouseButton.LEFT_BUTTON)) {
            script.saveConfiguration();
        }
    }

}
