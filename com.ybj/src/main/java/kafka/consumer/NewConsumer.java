package kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

public class NewConsumer {
    public static void main(String[] args) {
        //创建Properties配置文件
        Properties prop = new Properties();
        prop.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.setProperty("bootstrap.servers", "192.168.3.43:9092,192.168.3.44:9092");
        prop.setProperty("group.id", "test02");
        prop.setProperty("auto.offset.reset", "earliest");
        // 创建consumer实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);

        consumer.subscribe(Arrays.asList("test2"));

        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(1000);
            Iterator<ConsumerRecord<String, String>> iterator = poll.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> record = iterator.next();
                String k = record.key();
                String v = record.value();
                System.out.printf("key=%s,v=%s\n", k, v);
            }
        }
    }
}
