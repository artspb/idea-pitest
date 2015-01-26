package me.artspb.idea.pitest.plugin.execution;

import com.intellij.execution.Executor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.ToolWindowId;
import me.artspb.idea.pitest.plugin.Icons;
import me.artspb.idea.pitest.plugin.runner.PitestProgramRunner;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class PitestExecutor extends Executor {

    public static final String EXECUTOR_ID = "PitestExecutor";
    public static final String CONTEXT_ACTION_ID = "RunPitest";

    @Override
    public String getToolWindowId() {
        return ToolWindowId.RUN;
    }

    @Override
    public Icon getToolWindowIcon() {
        return Icons.PITEST;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return Icons.PITEST;
    }

    @Override
    public Icon getDisabledIcon() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Run selected configuration with Pitest";
    }

    @NotNull
    @Override
    public String getActionName() {
        return PitestProgramRunner.RUNNER_ID;
    }

    @NotNull
    @Override
    public String getId() {
        return EXECUTOR_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return "Run with Pitest";
    }

    @Override
    public String getStartActionText(String configurationName) {
        String name = configurationName != null ? escapeMnemonicsInConfigurationName(StringUtil.first(configurationName, 30, true)) : null;
        return "Run" + (StringUtil.isEmpty(name) ? "" : " '" + name + "'") + " with Pitest";
    }

    private static String escapeMnemonicsInConfigurationName(String configurationName) {
        return configurationName.replace("_", "__");
    }

    @Override
    public String getContextActionId() {
        return CONTEXT_ACTION_ID;
    }

    @Override
    public String getHelpId() {
        return null;
    }
}
