package com.ybj.mongondb;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

public class MongodbDemo {
    /*** 地址 */
    private static final String MONGODB_ADDRESS = "192.168.19.66";
    /*** 端口 */
    private static final Integer MONGODB_PORT = 27017;
    /*** 数据库 */
    private static final String DATABASE_NAME = "admin";
    /*** 集合 */
    private static final String COLLECTION_NAME = "java";
    /*** 连接 */
    private static MongoClient mongoClient;
    /*** 数据库 */
    private static MongoDatabase database;
    /*** 集合 */
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {
        // ----- 建立连接
        MongoClient client = new MongoClient(MONGODB_ADDRESS, MONGODB_PORT);
        // ----- 查看所有数据库
        /*MongoIterable<String> databaseNames = client.listDatabaseNames();
        for (String name : databaseNames) {
            System.out.println(name);
        }*/
        // ----- 选择数据库
        MongoDatabase database = client.getDatabase(DATABASE_NAME);
        System.out.println("************"+database);
        // ----- 创建集合
        database.createCollection(COLLECTION_NAME);
        // ----- 获取集合
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        System.out.println("##########"+collection);
        // ----- 插入文档
        // ----- 更新文档
        // ----- 删除文档
        // ----- 检索所有文档
        // ----- 条件查找 (匹配)
        // ----- 条件查找 [小于]
        // ----- 条件查找 [小于等于]
        // ----- 条件查找 [大于]
        // ----- 条件查找 [大于等于]
        // ----- 条件查找 [and]
        // ----- 条件查找 [or]
    }
}
