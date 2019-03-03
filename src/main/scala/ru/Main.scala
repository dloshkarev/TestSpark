package ru

import org.apache.spark.sql.SparkSession


object Main extends App {

  implicit val spark: SparkSession = org.apache.spark.sql.SparkSession.builder
    .master("local")
    .appName("Test Spark")
    .getOrCreate

  new Ml().breastCancer

}