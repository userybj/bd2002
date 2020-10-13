package kafka.consumer;

import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class OldConsumer {
    public static void main(String[] args) {
        // 创建Properties
        Properties props = new Properties();
        // 添加配置项
        // 配置1 连接kafka集群正在使用的zookeeper集群
        props.put("zookeeper.connect", "bd01:2181,bd02:2181,bd03:2181");
        // 配置2  设置消费者组名
        props.put("group.id", "g1");
        props.put("zookeeper.session.timeout.ms", "15000");
        // 设置consumer从最早可读数据开始消费
        props.put("offset.auto.commit", "earliest");
        // 创建ConsumerConfig需要传入Properties
        ConsumerConfig config = new ConsumerConfig(props);
        // 创建JavaConsumerConnector实例, 需要传入ConsumerConfig
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(config);
        // 设置消费者从哪些topic读取数据
        HashMap<String, Integer> topicCountMap = new HashMap<>();
        // 设置需要读取的topic和topic的分区数
        topicCountMap.put("topic1", 3);

        Map<String, List<KafkaStream<String, String>>> messageStreams = consumerConnector.createMessageStreams(
                topicCountMap,new StringDecoder(null),new StringDecoder(null));
        for (List<KafkaStream<String, String>> value : messageStreams.values()) {
            for (KafkaStream<String, String> messageAndMetadata : value) {
                ConsumerIterator<String, String> iterator = messageAndMetadata.iterator();
                while (iterator.hasNext()){
                    MessageAndMetadata<String, String> next = iterator.next();
                    String key = next.key();
                    String message = next.message();
                    System.out.printf("key:%s,value:%s\n", key, message);
                }
            }
        }
    }
}
