package usage.analytics.individual;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import usage.analytics.DynamoDbUsageAnalytics;

public class IndividualDynamoUsageAnalytics extends DynamoDbUsageAnalytics {

	public IndividualDynamoUsageAnalytics(AmazonDynamoDB dynamoDb, String uaTableName) {
		super(dynamoDb, uaTableName);
	}

	@Override
	public void report(Usage usage) {
		getAsyncTask().execute(usage);
	}

	@Override
	public void flush() {
		// Do nothing.
	}

}
