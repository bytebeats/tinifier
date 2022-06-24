package me.bytebeats.plugin.tinifier.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.ui.UIUtil;
import com.tinify.Exception;
import com.tinify.Tinify;
import me.bytebeats.plugin.tinifier.Preferences;
import me.bytebeats.plugin.tinifier.util.ConstsKt;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TinifierConfigurable implements Configurable {
    private JPanel mainPanel;
    private JTextField apiKeyField;
    private JLabel apiUsageLabel;
    private JPanel apiKeyPanel;
    private JPanel pluginSettings;
    private JCheckBox checkSupportedFiles;
    private JLabel apiKeyLabel;

    public TinifierConfigurable() {
        UIUtil.addBorder(apiKeyPanel, IdeBorderFactory.createTitledBorder("API Settings", false));
        UIUtil.addBorder(pluginSettings, IdeBorderFactory.createTitledBorder("Plugin Settings", false));
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return ConstsKt.TINIFIER;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return apiKeyField;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        Preferences preferences = Preferences.Companion.getInstance();
        if (!StringUtil.isEmptyOrSpaces(preferences.getApiKey())) {
            if (StringUtil.isEmptyOrSpaces(Tinify.key())) {
                Tinify.setKey(preferences.getApiKey());
            }

            updateUsageCount(false);
        }

        return mainPanel;
    }

    @Override
    public boolean isModified() {
        Preferences preferences = Preferences.Companion.getInstance();
        if (checkSupportedFiles.isSelected() != preferences.getCheckSupportedFiles()) {
            return true;
        }

        if (StringUtil.isEmptyOrSpaces(preferences.getApiKey()) && StringUtil.isEmptyOrSpaces(apiKeyField.getText())) {
            return false;
        }

        return !apiKeyField.getText().equals(preferences.getApiKey());
    }

    @Override
    public void apply() {
        Preferences preferences = Preferences.Companion.getInstance();
        preferences.setApiKey(apiKeyField.getText());
        preferences.setCheckSupportedFiles(checkSupportedFiles.isSelected());
        Tinify.setKey(preferences.getApiKey());
        if (!StringUtil.isEmptyOrSpaces(preferences.getApiKey())) {
            updateUsageCount(true);
        } else {
            apiUsageLabel.setText(ConstsKt.API_USAGE_EMTPY);
        }
    }

    @Override
    public void reset() {
        Preferences preferences = Preferences.Companion.getInstance();
        apiKeyField.setText(preferences.getApiKey());
    }

    private void updateUsageCount(boolean showErrorDialog) {
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                if (Tinify.validate()) {
                    apiUsageLabel.setText(String.format(ConstsKt.API_USAGE_FORMATTER, Tinify.compressionCount()));
                } else {
                    apiUsageLabel.setText(ConstsKt.API_USAGE_EMTPY);
                }
            } catch (Exception e) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    apiUsageLabel.setText(ConstsKt.API_USAGE_EMTPY);
                    if (showErrorDialog) {
                        Messages.showErrorDialog(e.getMessage(), ConstsKt.TINIFIER);
                    }
                }, ModalityState.stateForComponent(mainPanel));
            }
        });
    }
}
