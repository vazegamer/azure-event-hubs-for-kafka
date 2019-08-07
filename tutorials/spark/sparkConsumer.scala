/**
 * Read from a Kafka-enabled Event Hub
 */

// Import login module
import org.apache.kafka.common.security.plain.PlainLoginModule

// Update values as needed
val TOPIC = "spark-test"
val BOOTSTRAP_SERVERS = "mynamespace.servicebus.windows.net:9093"
val EH_SASL = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"Endpoint=sb://mynamespace.servicebus.windows.net/;SharedAccessKeyName=XXXXXX;SharedAccessKey=XXXXXX\";"

// Read stream using Spark SQL (structured streaming)
// consider adding .option("startingOffsets", "earliest") to read earliest available offset during testing
// group ID is autogenerated
val df = spark.readStream
    .format("kafka")
    .option("subscribe", TOPIC)
    .option("kafka.bootstrap.servers", BOOTSTRAP_SERVERS)
    .option("kafka.sasl.mechanism", "PLAIN")
    .option("kafka.security.protocol", "SASL_SSL")
    .option("kafka.sasl.jaas.config", EH_SASL)
    .option("kafka.request.timeout.ms", "60000")
    .option("kafka.session.timeout.ms", "60000")
    .option("failOnDataLoss", "false")
    .load()

// write to console
dfWrite.writeStream
    .outputMode("append")
    .format("console")
    .start()