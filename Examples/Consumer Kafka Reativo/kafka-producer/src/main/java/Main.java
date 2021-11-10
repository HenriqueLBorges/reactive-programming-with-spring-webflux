import java.io.*;
import java.time.Duration;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class Main {

    private final static String BOOTSTRAP_SERVERS = System.getenv("BOOTSTRAP_SERVERS") != null ? System.getenv("BOOTSTRAP_SERVERS") : "localhost:9092";
    private final static String CLIENT_ID_CONFIG = System.getenv("CLIENT_ID_CONFIG") != null ? System.getenv("CLIENT_ID_CONFIG") : "kafka-producer";
    private final static String KAFKA_TOPIC = System.getenv("KAFKA_TOPIC") != null ? System.getenv("KAFKA_TOPIC") : "KAFKA_TOPIC";

    /**
     * Kafka Producer Application Main method.
     *
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting Kafka producer...");

            String [] words = getWords();

            /*for (int i = 0; i < words.length; i ++){
                System.out.println(words[i]);
            }*/
            produceWords(words);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Insert words on the topic
     *
     * @param words - List of words
     */
    private static void produceWords(String [] words) {
        final Producer<Long, String> producer = new KafkaProducer<>(generateProps());
        System.out.println("----------------------------------------------------------");
        System.out.println("Producer from words created");
        long time = System.currentTimeMillis();

        try {
            int index = 0;
            for (long i = 0; i < words.length; i++) {
                System.out.println("----------------------------------------------------------");
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(KAFKA_TOPIC, i, words[index]);

                RecordMetadata metadata = producer.send(record).get();

                long elapsedTime = System.currentTimeMillis() - time;
                System.out.println("Sent record(key=" + record.key() + " value=" + record.value().toString() + ") " +
                        "meta(partition= " + metadata.partition() + ", offset=" + metadata.offset() + ") time=" + elapsedTime);
                Thread.sleep(Duration.ofSeconds(1).toMillis());
                index += 1;
            }
        } catch (Exception e) {
            System.out.println("----------------------------------------------------------");
            System.out.println("Exception occurred in Producer from words:" + e);
        } finally {
            producer.flush();
            producer.close();
        }
    }

    /**
     * A Kafka Producer Application needs a set of properties in order to work. This method returns the properties in order to our Kafka producer to produce messages.
     *
     * @return Properties - Kafka Application Properties
     */
    private static Properties generateProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());

        return props;
    }

    /**
     * Get the list words
     *
     * @return words - List of words
     */
    private static String[] getWords() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File file = new File(classloader.getResource("animals.txt").getFile());

        try(BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            StringTokenizer tokenizer = new StringTokenizer(everything, ",");
            String [] words = new String[tokenizer.countTokens()];
            int index = 0;

            while (tokenizer.hasMoreTokens()) {
                words[index] = tokenizer.nextToken();
                index += 1;
            }

            return words;
        } catch (Exception e) {
            System.out.println("Exception occurred while reading file:" + e);
        }

        return null;
    }
}
