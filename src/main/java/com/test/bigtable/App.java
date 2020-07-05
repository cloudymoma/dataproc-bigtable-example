package com.test.bigtable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import com.google.cloud.bigtable.hbase.BigtableConfiguration;
import com.google.cloud.bigtable.hbase.BigtableOptionsFactory;
import java.io.IOException;
import org.apache.hadoop.hbase.client.Connection;
import java.io.IOException;

/*
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) throws IOException {
		String tableName = "test-bin";
		SparkSession sc = SparkSession.builder().appName("test-bigtable").getOrCreate();
		Configuration hbaseConf = BigtableConfiguration.configure("learning-centre", "test");

		hbaseConf.set(TableInputFormat.INPUT_TABLE, tableName);
		hbaseConf.set(TableInputFormat.SCAN_ROW_START, "104");
		hbaseConf.set(TableInputFormat.SCAN_ROW_STOP,"105");

		RDD<Tuple2<ImmutableBytesWritable, Result>> hBaseRDD = sc.sparkContext()
			.newAPIHadoopRDD(hbaseConf, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

		System.out.println("Number of Records found : " + hBaseRDD.count());
		/**
		  JavaRDD<Tuple2<ImmutableBytesWritable, Result>> hBaseJavaRdd = hBaseRDD.toJavaRDD();
		  hBaseJavaRdd.foreach(v1 -> {
		// get rowkey
		String key = new String(v1._2.getRow());
		// get cell value
		String name = new String(v1._2.getValue("cf1".getBytes(), "name".getBytes()));
		String type = new String(v1._2.getValue("cf1".getBytes(), "type".getBytes()));
		System.out.println("Row key:" + key + "\tcf1.Name:" + name + "\tcf1.type:" + type);
		});
		*/

		sc.stop();
	}
}

