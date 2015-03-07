package usage.analytics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public abstract class DynamoDbUsageAnalytics implements UsageAnalytics {

    private final AmazonDynamoDB dynamoDb;
    private final String uaTableName;

    protected final static int MAX_DYNAMO_BATCH_SIZE = 25;
    public final static String TAG = "usage-analytics";

    private final SimpleDateFormat dateFormatter =
                                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    public DynamoDbUsageAnalytics(AmazonDynamoDB dynamoDb, String uaTableName) {
        this.dynamoDb = dynamoDb;
        this.uaTableName = uaTableName;
    }

    public AsyncTask<Usage, ?, ?> getAsyncTask() {
        return new DynamoUsageRecodingAsyncTask();
    }

    private class DynamoUsageRecodingAsyncTask extends AsyncTask<Usage, Void, Void> {

        @Override
        protected Void doInBackground(Usage... params) {
            try {
                batchWrite(params);
            } catch (Exception e) {
                Log.i(TAG, "Failed to write usage data. " + getExceptionString(e));
            }
            return null;
        }

    }

    private String getExceptionString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private void batchWrite(Usage... params) {
        List<WriteRequest> batch = Lists.newLinkedList();
        for (Usage usage : params) {
            if (usage == null) {
                continue;
            }

            batch.add(toWriteRequest(usage));
            if (batch.size() == MAX_DYNAMO_BATCH_SIZE) {
                writeBatch(batch);
                batch = Lists.newLinkedList();
            }
        }

        if (batch.size() > 0) {
            writeBatch(batch);
        }
    }

    private void writeBatch(List<WriteRequest> batch) {
        Preconditions.checkState(batch.size() <= MAX_DYNAMO_BATCH_SIZE);
        dynamoDb.batchWriteItem(new BatchWriteItemRequest(ImmutableMap.of(uaTableName, batch)));
    }

    private WriteRequest toWriteRequest(Usage usage) {
        return new WriteRequest(new PutRequest(toDynamoRow(usage)));
    }

    public Map<String, AttributeValue> toDynamoRow(Usage usage) {
        ImmutableMap.Builder<String, AttributeValue> builder =
                                        ImmutableMap.builder();
        builder.put("id", 			new AttributeValue().withS(usage.id()));
        builder.put("installation", new AttributeValue().withS(usage.installation()));
        builder.put("tag", 			new AttributeValue().withS(usage.tag()));
        builder.put("description", 	new AttributeValue().withS(usage.description()));
        builder.put("timestamp", 	new AttributeValue().withS(dateFormatter.format(new Date(usage.timestamp()))));
        return builder.build();
    }
}
