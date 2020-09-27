package zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperDemo {
    public static void main(String[] args) throws Exception{
        String connectString = "bd01,bd02,bd03";
        int timeOut = 3000;
        ZooKeeper zk = new ZooKeeper(connectString, timeOut, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.EventType type = event.getType();
                switch (type) {
                    case NodeCreated:
                        System.out.println("创建节点");
                        break;
                    case NodeDeleted:
                        System.out.println("节点删除");
                        break;
                    default:
                        System.out.println("其他情况");
                        break;
                }
            }
        });
        System.out.println(zk);
    }
}
