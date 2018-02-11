
package com.enjoy.core.main;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class EnjoyLogger {

  public static void initial(boolean logFileFlag) {
    Properties prop = new Properties();

    configRoot(prop, logFileFlag);

    PropertyConfigurator.configure(prop);
  }
 
	private static void configRoot(Properties prop, boolean logFileFlag){
		 prop.put("log4j.rootLogger", "INFO, xDefault");
		 
		 System.out.println("[configRoot] logFileFlag :: " + logFileFlag);
		 
		 if (logFileFlag) {	
			 prop.put("log4j.appender.xDefault", "org.apache.log4j.DailyRollingFileAppender");
			 prop.put("log4j.appender.xDefault.file", ConfigFile.getPathLog() + "/System.log.txt");
			 prop.put("log4j.appender.xDefault.datePattern", "'.'yyyy-MM-dd");
		 }else{
			 prop.put("log4j.appender.xDefault", "org.apache.log4j.ConsoleAppender");
		 }
		 
		 prop.put("log4j.appender.xDefault.layout", "org.apache.log4j.PatternLayout");
		 prop.put("log4j.appender.xDefault.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");
		    
	}

  //****************************************************************************

  public static EnjoyLogger getLogger(Class clazz) {
    Logger logger = Logger.getLogger(getShortClassName(clazz));

    return (new EnjoyLogger(logger));
  }

  public static EnjoyLogger getLogger(String str){
      Logger logger = Logger.getLogger(str);

       return (new EnjoyLogger(logger));
  }

  private static String getShortClassName(Class clazz) {
    String name = clazz.getName();
    int index = name.lastIndexOf(".");

    return name.substring(index + 1);
  }

  //****************************************************************************

  private Logger fLogger;

  private EnjoyLogger(Logger logger) {
    this.fLogger = logger;
  }

  public void debug(Object obj) {
    fLogger.debug(obj);
  }

  public void info(Object obj) {
    fLogger.info(obj);
  }

  public void warn(Object obj) {
    fLogger.warn(obj);
  }

  public void error(Object obj) {
    fLogger.error(obj);
  }

  public void fatal(Object obj) {
    fLogger.fatal(obj);
  }

}



