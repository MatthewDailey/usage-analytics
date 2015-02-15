package usage.analytics.batched;

import usage.analytics.DynamoDbUsageAnalytics;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

public class BatchedUsageAnalytics extends DynamoDbUsageAnalytics {

	public BatchedUsageAnalytics(AmazonDynamoDB dynamoDb, String uaTableName) {
		super(dynamoDb, uaTableName);
	}
	
	private Usage[] usageQueue = new Usage[DynamoDbUsageAnalytics.MAX_DYNAMO_BATCH_SIZE];
	private int queueSize = 0;
	
	@Override
	public synchronized void report(Usage usage) {
		usageQueue[queueSize] = usage;
		queueSize++;
		if (queueSize == usageQueue.length) {
			flush();
		}
	}

	@Override
	public synchronized void flush() {
		getAsyncTask().execute(usageQueue);
		usageQueue = new Usage[DynamoDbUsageAnalytics.MAX_DYNAMO_BATCH_SIZE];
		queueSize = 0;
	}

}
