package com.tabnine.general;

import com.intellij.ide.plugins.PluginStateListener;
import com.tabnine.TabNineDisablePluginListener;
import com.tabnine.TabNinePluginStateListener;
import com.tabnine.binary.BinaryFacade;
import com.tabnine.binary.BinaryRun;
import com.tabnine.binary.TabNineGateway;
import com.tabnine.binary.fetch.*;
import com.tabnine.selections.TabNineLookupListener;
import com.tabnine.state.UninstallReporter;
import org.jetbrains.annotations.NotNull;

public class DependencyContainer {
    private static TabNineGateway GATEWAY_INSTANCE = null;
    private static TabNineLookupListener LOOKUP_LISTENER_INSTANCE = null;
    private static TabNineDisablePluginListener DISABLE_PLUGIN_LISTENER_INSTANCE = null;

    public static synchronized TabNineGateway singletonOfTabNineGateway() {
        if (GATEWAY_INSTANCE == null) {
            GATEWAY_INSTANCE = new TabNineGateway();

            GATEWAY_INSTANCE.init();
        }

        return GATEWAY_INSTANCE;
    }

    public static synchronized TabNineLookupListener singletonOfTabNineLookupListener() {
        if (LOOKUP_LISTENER_INSTANCE == null) {
            LOOKUP_LISTENER_INSTANCE = new TabNineLookupListener();
        }

        return LOOKUP_LISTENER_INSTANCE;
    }

    public static TabNineDisablePluginListener singletonOfTabNineDisablePluginListener() {
        if (DISABLE_PLUGIN_LISTENER_INSTANCE == null) {
            DISABLE_PLUGIN_LISTENER_INSTANCE = new TabNineDisablePluginListener(instanceOfUninstallReporter());
        }

        return DISABLE_PLUGIN_LISTENER_INSTANCE;
    }

    @NotNull
    public static BinaryFacade instanceOfBinaryFacade() {
        return new BinaryFacade(instanceOfBinaryRun());
    }

    @NotNull
    public static PluginStateListener instanceOfTabNinePluginStateListener() {
        return new TabNinePluginStateListener(instanceOfUninstallReporter());
    }

    private static UninstallReporter instanceOfUninstallReporter() {
        return new UninstallReporter(instanceOfBinaryRun());
    }

    @NotNull
    private static BinaryRun instanceOfBinaryRun() {
        return new BinaryRun(instanceOfBinaryFetcher());
    }

    @NotNull
    private static BinaryVersionFetcher instanceOfBinaryFetcher() {
        return new BinaryVersionFetcher(instanceOfLocalBinaryVersions(), instanceOfBinaryRemoteSource(), instanceOfBinaryDownloader());
    }

    @NotNull
    private static BinaryDownloader instanceOfBinaryDownloader() {
        return new BinaryDownloader(instanceOfBinaryPropositionValidator());
    }

    @NotNull
    private static TempBinaryValidator instanceOfBinaryPropositionValidator() {
        return new TempBinaryValidator(instanceOfBinaryValidator());
    }

    @NotNull
    private static BinaryRemoteSource instanceOfBinaryRemoteSource() {
        return new BinaryRemoteSource();
    }

    @NotNull
    private static LocalBinaryVersions instanceOfLocalBinaryVersions() {
        return new LocalBinaryVersions(instanceOfBinaryValidator());
    }

    @NotNull
    private static BinaryValidator instanceOfBinaryValidator() {
        return new BinaryValidator();
    }
}
