package    yh.ai.dependencyinjection;

import    yh.ai.utils.ConsoleLogger;
import    yh.ai.utils.ImportantLogger;
import    yh.ai.utils.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

public class LoggerProvider implements Provider<Logger> {
    private final LogLevel logLevel;

    @Inject
    public LoggerProvider(final LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public Logger get() {
        switch (logLevel){
            case IMPORTANT:
                return new ImportantLogger();
            default:
                return new ConsoleLogger();
        }
    }
}
