package com.xahj.mongodb;


import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created with IntelliJ IDEA.
 * Author: Amos
 * E-mail: amos@amoscloud.com
 * Date: 2020/9/4
 * Time: 0:39
 * Description:
 */
public class MongoDbJdbc {
    /*** 地址 */
    private static final String MONGODB_ADDRESS = "127.0.0.1";
    /*** 端口 */
    private static final Integer MONGODB_PORT = 27017;
    /*** 数据库 */
    private static final String DATABASE_NAME = "amos";
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
        newMongoClient();
        // ----- 查看所有数据库
        listDatabases();
        // ----- 选择数据库
        getDatabase();
        // ----- 创建集合
        createCollection();
        // ----- 获取集合
        getCollection();
        // ----- 插入文档
        insert();
        // ----- 更新文档
        update();
        // ----- 删除文档
        delete();
        // ----- 检索所有文档
        findAll();
        // ----- 条件查找 (匹配)
        findByEq();
        // ----- 条件查找 [小于]
        findByLt();
        // ----- 条件查找 [小于等于]
        findByLte();
        // ----- 条件查找 [大于]
        findByGt();
        // ----- 条件查找 [大于等于]
        findByGte();
        // ----- 条件查找 [and]
        findByAnd();
        // ----- 条件查找 [or]
        findByOr();
    }

    /**
     * 建立连接
     */
    private static void newMongoClient() {
        System.out.println("==================== newMongoClient ====================");
        mongoClient = new MongoClient(MONGODB_ADDRESS, MONGODB_PORT);
    }

    /**
     * 查看所有数据库
     */
    private static void listDatabases() {
        System.out.println("==================== listDatabases ====================");
        MongoIterable<String> names = mongoClient.listDatabaseNames();
        for (String mane : names) {
            System.out.println(mane);
        }
    }

    /**
     * 获取数据库
     */
    private static void getDatabase() {
        System.out.println("==================== getDatabase ====================");
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

    /**
     * 创建集合
     */
    private static void createCollection() {
        System.out.println("==================== createCollection ====================");
        database.createCollection(COLLECTION_NAME);
    }

    /**
     * 获取集合
     */
    private static void getCollection() {
        System.out.println("==================== getCollection ====================");
        collection = database.getCollection(COLLECTION_NAME);
    }

    /**
     * 插入文档
     */
    private static void insert() {
        System.out.println("==================== insert ====================");
        // ----- 插入一条文档
        Document document = new Document().append("title", "java").append("desc", "JAVA是我的衣食父母").append("by", "yanwu")
                .append("tage", Collections.singletonList("java")).append("likes", 200);
        collection.insertOne(document);
        // ----- 插入多条文档
        List<Document> documents = new ArrayList<>();

        Document document1 = new Document().append("title", "php").append("desc", "PHP是世界上最好的语言").append("by", "yanwu")
                .append("tage", Collections.singletonList("php")).append("likes", 100);
        documents.add(document1);

        Document document2 = new Document().append("title", "python").append("desc", "python是人工智能的未来").append("by", "yanwu")
                .append("tage", Collections.singletonList("python")).append("likes", 250);
        documents.add(document2);

        Document document3 = new Document().append("title", "mongodb").append("desc", "学习学习").append("by", "yanwu")
                .append("tage", Collections.singletonList("mongodb")).append("likes", 150);
        documents.add(document3);

        collection.insertMany(documents);
    }

    /**
     * 检索所有文档
     */
    private static void findAll() {
        System.out.println("==================== findAll ====================");
        // ----- 拿到该集合中所有的文档
        print(collection.find());
    }

    /**
     * 更新文档
     */
    private static void update() {
        System.out.println("==================== update ====================");
        collection.updateMany(eq("title", "java"), new Document("$set", new Document("title", "java语言")));
    }

    /**
     * 删除文档
     */
    private static void delete() {
        System.out.println("==================== delete ====================");
        // ----- 删除一个
        collection.deleteOne(eq("title", "java"));
        findAll();
        // ----- 删除多个
        collection.deleteMany(eq("by", "yanwu"));
        findAll();
    }

    /**
     * 条件查找 (匹配)
     */
    private static void findByEq() {
        System.out.println("==================== findByEq: likes == 200 ====================");
        print(collection.find(eq("likes", 200)));
    }

    /**
     * 条件查找 [小于]
     */
    private static void findByLt() {
        System.out.println("==================== findByLt: likes < 200 ====================");
        print(collection.find(lt("likes", 200)));
    }

    /**
     * 条件查找 [小于等于]
     */
    private static void findByLte() {
        System.out.println("==================== findByLte: likes <= 200 ====================");
        print(collection.find(lte("likes", 200)));
    }

    /**
     * 条件查找 [大于]
     */
    private static void findByGt() {
        System.out.println("==================== findByGt: likes > 200 ====================");
        print(collection.find(gt("likes", 200)));
    }

    /**
     * 条件查找 [大于等于]
     */
    private static void findByGte() {
        System.out.println("==================== findByGte: likes >= 200 ====================");
        print(collection.find(gte("likes", 200)));
    }

    /**
     * 条件查找 [and]
     */
    private static void findByAnd() {
        System.out.println("==================== findByGte: likes > 150 && likes < 250 ====================");
        print(collection.find(and(gt("likes", 150), lt("likes", 250))));
    }

    /**
     * 条件查找 [or]
     */
    private static void findByOr() {
        System.out.println("==================== findByGte: likes < 150 || likes > 200 ====================");
        print(collection.find(or(lt("likes", 150), gt("likes", 200))));
    }

    /**
     * 打印输出文档集合
     */
    private static void print(FindIterable<Document> documents) {
        for (Document document : documents) {
            System.out.println(document.toJson());
        }
    }
}
