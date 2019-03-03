package ru

import org.apache.spark.sql.{Dataset, SparkSession}

import scala.io.Source

abstract class SparkTask(spark: SparkSession)  {
  def getFileFromResource(fileName: String)(implicit spark: SparkSession): Dataset[String] = {
    val lines = Source.fromInputStream(getClass.getResourceAsStream(fileName)).getLines().toSeq
    import spark.implicits._
    spark.sparkContext.parallelize(lines).toDS()
  }
}
