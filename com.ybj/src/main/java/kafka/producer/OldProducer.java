package kafka.producer;

import kafka.javaapi.producer.Producer;

import java.util.Properties;

public class OldProducer {
    public static void main(String[] args) {
        //创建Properties
        Properties prop = new Properties();
        //关于prop的相关配置
        // 配置1.kafka集群的连接信息
        prop.setProperty("metadata.broker.list","bd01:9092,bd02:9092,bd03:9092,");
        // 配置2.kafkaProducer发送数据时key和value的序列化方式
        prop.put("serializer.class", "kafka.serializer.StringEncoder");

        ProducerConfig config = new ProducerConfig(prop);  //def this(originalProps : java.util.Properties)
        // 创建producer实例，需要传入ProducerConfig对象
        Producer<String, String> producer = new Producer<>(config);   //def this(config : kafka.producer.ProducerConfig)

        // 发送消息
        producer.send(new KeyedMessage<>("topic1", "key", "oldProducer"));
    }
}
