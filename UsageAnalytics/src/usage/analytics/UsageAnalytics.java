package usage.analytics;

public interface UsageAnalytics {
	
	public interface Usage {
		String id();
		String installation();
		String tag();
		String description();
		long timestamp();
	}
	
	void report(Usage usage);
	void flush();
}
