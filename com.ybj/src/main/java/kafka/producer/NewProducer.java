package kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class NewProducer {
    public static void main(String[] args) {
        // 创建Properties实例
        Properties prop = new Properties();
        //Properties的相关配置
        // 设置需要连接的kafka集群地址
        prop.setProperty("bootstrap.servers","bd01:9092,bd02:9092,bd03:9092");
        // 设置key和value的序列化类
        prop.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        prop.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        // 创建KafkaProducer实例，需要传入Properties
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);
        /*public KafkaProducer(Properties properties) {
            this(new ProducerConfig(properties), (Serializer)null, (Serializer)null,
            (Metadata)null, (KafkaClient)null);
        }*/

        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> record =
                    new ProducerRecord<>("topic1","Nkey"+i ,"NewProducer"+i);
            // kafka的新producer默认开启了批量发送
            // 当本地缓存的消息数量没有达到批大小时，等待新的数据
            // 到达批大小，一次性发送
            producer.send(record);
        }

        // 当新API的生产者出现数据发送没有收到时
        // 要考虑是否因为缓冲区数据没有到达发送的批次大小导致的
        producer.flush();
    }
}
