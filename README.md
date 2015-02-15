# usage-analytics
Android Usage Analytics library use by saypenis.com

To use this package, you will need to pass in a com.amazonaws.services.dynamodbv2.AmazonDynamoDB client 
and the name of a table associated with that dynamo client. 

The table should have a hashkey 'id' associated with your usage.analytics.UsageAnalytics.Usage implementation.

This data is intended to be exported and analyzed on a separate platform of your choosing.
