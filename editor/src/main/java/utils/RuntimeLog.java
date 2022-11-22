package utils;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class RuntimeLog extends Formatter {
    @Override
    public String format(LogRecord record) {
	    String builder = "["+record.getLevel() + "] "+record.getSourceClassName()+"."+
                record.getSourceMethodName()+" "+"\n"+
                this.formatMessage(record) +
			    System.lineSeparator() +"\n";
        // pre-Java7: builder.append(System.getProperty('line.separator'));
        return builder;
    }
    //conveinence method to add a logger to a class
    public static Logger addLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        Handler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(new RuntimeLog());
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        return logger;

    }
}

