package utils;

import java.util.HashMap;

public class Timer {
	private HashMap<Integer, Long> timers;
	public static final int FETCHER = 0;
	public static final int FILTERURLS = 1;
	public static final int PARSER = 2;
	public static final int SCHEDULER = 3;
	public static final int WRITER = 4;
	public static final int READER = 5;
	public static final int CLASSIFIER = 6;
	
	private static final String[] labels = {
			"Fetcher", 
			"FilterUrls", 
			"Parser", 
			"Scheduler", 
			"Writer", 
			"Reader",
			"Classifier"};
	
	public Timer() {
		timers = new HashMap<Integer, Long>();
	}
	
	public void startTimer(int which) {
		timers.put(which, System.currentTimeMillis());
		info("Starting " + labels[which] + " timer...");
	}
	
	public long finishTimer(int which) {
		Long current = System.currentTimeMillis();
		Long l = timers.get(which);
		
		if (l == null) {
			info("Had not started" + labels[which] + " timer...");
			return -1;
		} else {
			info(labels[which] + " spent " + (current-l) + " ms.");
			return current - l;
		}
	}
	
	public void info(String msg) {
		System.out.println(msg);
	}
}
