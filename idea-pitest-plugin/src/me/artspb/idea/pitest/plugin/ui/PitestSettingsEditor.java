package me.artspb.idea.pitest.plugin.ui;

import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.util.ui.UIUtil;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO validation
 *
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class PitestSettingsEditor extends SettingsEditor<RunConfigurationBase> implements PanelWithAnchor {

    private JPanel editor;
    private JComponent anchor;

    private JCheckBox editManuallyField;
    private LabeledComponent<TextFieldWithBrowseButton> targetTestsField;
    private LabeledComponent<TextFieldWithBrowseButton> targetClassesField;
    private LabeledComponent<TextFieldWithBrowseButton> excludedClassesField;
    private LabeledComponent<TextFieldWithBrowseButton> reportDirField;
    private LabeledComponent<TextFieldWithBrowseButton> sourceDirsField;

    private final RunConfigurationBase runConfiguration;

    public PitestSettingsEditor(@NotNull RunConfigurationBase runConfiguration) {
        this.runConfiguration = runConfiguration;
        anchor = UIUtil.mergeComponentsWithAnchor(targetTestsField, targetClassesField, excludedClassesField,
                reportDirField, sourceDirsField);
        init();
    }

    private void init() {
        editManuallyField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(@NotNull ActionEvent e) {
                changePanels(editManuallyField.isSelected());
            }
        });
        // TODO configureClassChooser
        configureDirChooser(reportDirField, "Select Report Directory"); // TODO introduce bundle
        configureDirChooser(sourceDirsField, "Select Source Directory");
    }

    private void configureDirChooser(LabeledComponent<TextFieldWithBrowseButton> field, String title) {
        FileChooserDescriptor dirFileChooser = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        dirFileChooser.setTitle(title);
        TextBrowseFolderListener listener = new TextBrowseFolderListener(dirFileChooser, runConfiguration.getProject());
        field.getComponent().addBrowseFolderListener(listener);
    }

    @Override
    protected void resetEditorFrom(RunConfigurationBase runConfiguration) {
        resetEditor(runConfiguration);
    }

    @Override
    protected void applyEditorTo(RunConfigurationBase runConfiguration) throws ConfigurationException {
        PitestConfiguration configuration = PitestConfiguration.getOrCreate(runConfiguration);
        configuration.setEditManually(editManuallyField.isSelected());
        configuration.setTargetTests(targetTestsField.getComponent().getText());
        configuration.setTargetClasses(targetClassesField.getComponent().getText());
        configuration.setExcludedClasses(excludedClassesField.getComponent().getText());
        configuration.setReportDir(reportDirField.getComponent().getText());
        configuration.setSourceDirs(sourceDirsField.getComponent().getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        resetEditor(runConfiguration);
        return editor;
    }

    private void resetEditor(RunConfigurationBase runConfiguration) {
        PitestConfiguration configuration = PitestConfiguration.getOrCreate(runConfiguration);
        boolean editManually = configuration.isEditManually();
        editManuallyField.setSelected(editManually);
        targetTestsField.getComponent().setText(configuration.getTargetTests());
        targetClassesField.getComponent().setText(configuration.getTargetClasses());
        excludedClassesField.getComponent().setText(configuration.getExcludedClasses());
        reportDirField.getComponent().setText(configuration.getReportDir());
        sourceDirsField.getComponent().setText(configuration.getSourceDirs());
        changePanels(editManually);
    }

    private void changePanels(boolean enabled) {
        targetTestsField.setEnabled(enabled);
        targetClassesField.setEnabled(enabled);
        excludedClassesField.setEnabled(enabled);
        reportDirField.setEnabled(enabled);
        sourceDirsField.setEnabled(enabled);
    }

    @Override
    public JComponent getAnchor() {
        return anchor;
    }

    @Override
    public void setAnchor(JComponent anchor) {
        this.anchor = anchor;
        targetTestsField.setAnchor(anchor);
        targetClassesField.setAnchor(anchor);
        excludedClassesField.setAnchor(anchor);
        reportDirField.setAnchor(anchor);
        sourceDirsField.setAnchor(anchor);
    }
}
