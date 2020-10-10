package hive;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyDateFormat extends UDF {
    //转换日期格式
    public String evaluate(String date){
        try {
            SimpleDateFormat parser = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(parser.parse(date));
        } catch (ParseException e) {
            return "0000-00-00 00:00:00";
        }
    }
}
