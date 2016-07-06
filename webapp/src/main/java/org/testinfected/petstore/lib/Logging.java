package org.testinfected.petstore.lib;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {

    private Logging() {}

    public static Logger off() {
        Logger logger = makeAnonymousLogger();
        logger.setLevel(Level.OFF);
        return logger;
    }

    public static Logger toConsole() {
        Logger logger = makeAnonymousLogger();
        logger.addHandler(ConsoleHandler.toStandardOutput());
        return logger;
    }

    public static Logger toFile(String path) throws IOException {
        Logger logger = makeAnonymousLogger();
        FileHandler handler = new FileHandler(path);
        handler.setFormatter(new PlainFormatter());
        logger.addHandler(handler);
        return logger;
    }

    private static Logger makeAnonymousLogger() {
        Logger logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        return logger;
    }
}
