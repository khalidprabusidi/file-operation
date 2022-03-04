package com.kps.loganalytic;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

class LogCLI {

    private static Logger logger = LoggerFactory.getLogger(LogCLI.class);

    private ArgFlag argFlag;
    private CommandLineParser clp;

    private int time;
    private String directory;
    private String[] arguments;

    public LogCLI(String[] arguments) throws ParseException {
        this.argFlag = new ArgFlag();
        this.clp = new DefaultParser();

        CommandLine cl = this.clp.parse(argFlag.getOptions(), arguments);
        this.time = Integer.valueOf(StringUtils.chop(cl.getOptionValue(ArgFlag.getArgTimeOption().getLongOpt())));
        this.directory = cl.getOptionValue(ArgFlag.getArgDirectoryOption().getLongOpt());
    }

    public String[] getArguments() {
        return arguments;
    }

    public int getTime() {
        return time;
    }

    public String getDirectory() {
        return directory;
    }

    public String readFile(File file) throws IOException {
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                String dateTimeFromLine = StringUtils.substringBetween(line, "[", "]");
                if(dateTimeFromLine != null){
                    LocalDateTime parsedDateTimeFromLine = LocalDateTime.parse(dateTimeFromLine, dateTimeFormatter);
                    Instant logDateTime = parsedDateTimeFromLine.toInstant(ZoneOffset.UTC);
                    if(logDateTime.isAfter(Instant.now().minus(this.time, ChronoUnit.MINUTES))){
                        logger.info(line);   
                    }                  
                }
            }
        } finally {
            it.close();
        }
        return "";
    }

    public void getListOfFiles(String directory) throws IOException {

        Files.walkFileTree(Paths.get(directory), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!Files.isDirectory(file) && !Files.isHidden(file)) {
                    try {
                        String logLine = readFile(file.toFile());
                        if(logLine.isEmpty()){
                            return TERMINATE;
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return CONTINUE;
            }
        });
    }

    public void exec() throws IOException {
        getListOfFiles(getDirectory());
    }
}