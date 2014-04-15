package com.jingwei.mobile.log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Log {
	
	/**
	 * Init the default log file name, with current time
	 */
	static{
		
		Calendar calendar = Calendar.getInstance();
		Log.defaultLog = String.format(
				"jingwei_%d_%d_%d-%d_%d_%d.log", 
				calendar.get(Calendar.YEAR), 
				calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND));
	}

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(Log.defaultLog);
		
		System.out.println(Calendar.getInstance().toString());
		System.out.println(new Date().toString());
		
		Log log = Log.GetInstance("a.log");
		//Log log = Log.GetInstance("/home/administrator/a.log");
		log.Write("abcdefg");

	}
	
	private String logFilePath;
	
	/**
	 * Private constructor to build a new log instance.
	 * @param logFilePath, where to write the log. 
	 * 		if the file exists, will append to the end of the log
	 * 		else, create a new file. by default, will use working directory.
	 */
	private Log(String logFilePath){
		this.logFilePath = logFilePath; 
	}
	
	/**
	 * Write log to the log instance
	 * @param str
	 * @throws IOException
	 */
	public void Write(String str) throws IOException{
		
		FileWriter writer = null;
		
		try{
			writer = new FileWriter(logFilePath, true);
			String cont = String.format("%s -- %s\n", new Date().toString(), str);
			writer.write(cont);
			
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			writer.close();
		}
		
	}
	
	public static void Log(String str){
		Log log = Log.GetInstance();
		try {
			log.Write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use a private map to store the different logs
	 */
	private static Map<String, Log> logMap;
	
	/**
	 * give a default file 
	 */
	private static String defaultLog;
	
	/**
	 * Return a log instance, which write log to the file given by logFilePath. 
	 * @param logFilePath, the log file path, support absolute file path.
	 * @return a log instance,
	 */
	public static Log GetInstance(String logFilePath){
		if(logMap == null){
			logMap = new HashMap<String, Log>();
		}
		
		if(logMap.containsKey(logFilePath)){
			return logMap.get(logFilePath);
		}else{
			Log log = new Log(logFilePath);
			logMap.put(logFilePath, log);
			return logMap.get(logFilePath);
		}
		
		
	}

	/**
	 * Support write to default log in working directory
	 * @return an log instance, which write log to the default log file.
	 */
	public static Log GetInstance(){
		return GetInstance(defaultLog);
	}

}
