package com.ybj.hadoop.mapreduce.weblog.entity;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class WebLog implements WritableComparable<WebLog> {
    private String ip;
    private String time;
    private Long status;
    private String type;
    /*private Long up;
    private Long down;
    private Long sum;*/

    public WebLog(String ip, String time, Long status,String type) {
        this.ip = ip;
        this.time = time;
        this.status = status;
        this.type = type;
    }
    /*public WebLog(String ip, Long up, Long down, Long sum) {
        this.ip = ip;
        this.up = up;
        this.down = down;
        this.sum = up + down;
    }*/

    public WebLog() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /*public Long getUp() {
        return up;
    }

    public void setUp(long up) {
        this.up = up;
    }

    public Long getDown() {
        return down;
    }

    public void setDown(long down) {
        this.down = down;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }*/

    /*@Override
    public String toString() {
        return ip+" "+time+" "+status+" "+ use;
    }*/

    /*@Override
    public String toString() {
        return "WebLog{" +
                "ip='" + ip + '\'' +
                ", up=" + up +
                ", down=" + down +
                ", sum=" + sum +
                '}';
    }*/

    /**
     * 序列化
     * @param out
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        //out.writeUTF(this.type);
        out.writeUTF(this.ip);
        //out.writeUTF(this.time);
        out.writeLong(this.status);

        /*out.writeLong(up);
        out.writeLong(down);
        out.writeLong(sum);*/
    }

    /**
     * 反序列化
     * @param in
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        //this.type = in.readUTF();
        this.ip = in.readUTF();
        //this.time = in.readUTF();
        this.status = in.readLong();

        /*this.up = in.readLong();
        this.down = in.readLong();
        this.sum = in.readLong();*/
    }

    @Override
    public String toString() {
        return ip + "\t" +
                time + "\t"
                + status+ "\t" +type ;
    }

    @Override
    public int compareTo(WebLog o) {
        /*if(this.status.compareTo(o.status) >= 0){
            return -1;
        }else {
            return 1;
        }*/
        if(this.ip.compareTo(o.ip) == 0){
            if(this.status.compareTo(o.status) == 0){
                return 0;
            }else {
                return this.status.compareTo(o.status);
            }
        }else {
            return this.ip.compareTo(o.ip);
        }
    }
}
