
package com.kps.loganalytic;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ArgFlag {

    private static final Option ARG_TIME_OPTION = new Option("t", "time", true, "Time argument in minutes");
    private static final Option ARG_DIRECTORY_OPTION = new Option("d", "directory", true, "Directory argument");

    private Options options;

    public ArgFlag() {
        Options newOptions = new Options();
        newOptions.addOption(ARG_TIME_OPTION);
        newOptions.addOption(ARG_DIRECTORY_OPTION);
        this.options = newOptions;
    }

    public static Option getArgTimeOption() {
        return ARG_TIME_OPTION;
    }

    public static Option getArgDirectoryOption() {
        return ARG_DIRECTORY_OPTION;
    }

    public Options getOptions() {
        return this.options;
    }
}
