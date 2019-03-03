package ru

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import Models.Obs

class Ml(implicit spark: SparkSession) extends SparkTask(spark) {

  /**
    * https://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+(Original)
    */
  def breastCancer = {
    implicit val enc: Encoder[Obs] = Encoders.product[Obs]

    val id = StructField("id", DataTypes.IntegerType)
    val thickness = StructField("thickness", DataTypes.DoubleType)
    val size = StructField("size", DataTypes.DoubleType)
    val shape = StructField("shape", DataTypes.DoubleType)
    val madh = StructField("madh", DataTypes.DoubleType)
    val epsize = StructField("epsize", DataTypes.DoubleType)
    val bnuc = StructField("bnuc", DataTypes.DoubleType)
    val bchrom = StructField("bchrom", DataTypes.DoubleType)
    val nNuc = StructField("nNuc", DataTypes.DoubleType)
    val mit = StructField("mit", DataTypes.DoubleType)
    val clas = StructField("clas", DataTypes.DoubleType)

    val fields = Array(id, thickness, size, shape, madh, epsize, bnuc, bchrom, nNuc, mit, clas)
    val schema = StructType(fields)

    val ds = spark.read
      .schema(schema)
      .option("delimiter", ",")
      .option("header", "true")
      .option("mode", "DROPMALFORMED")
      .csv(getFileFromResource("/breast-cancer-wisconsin.csv"))
      .as[Obs]

    ds.printSchema()
    ds.show()

    println("benign: " + ds.filter(_.clas == 2.0).count)
    println("malignant: " + ds.filter(_.clas == 4.0).count)

    val featureAssemble = new VectorAssembler()
      .setInputCols(Array("thickness", "size", "shape", "madh", "epsize", "bnuc", "bchrom", "nNuc", "mit"))
      .setOutputCol("features")

    val parts = ds.randomSplit(Array(0.6, 0.4))
    val trainingData = parts(0).cache()
    val testData = featureAssemble.transform(parts(1))

    val labelIndexer = new StringIndexer()
      .setInputCol("clas")
      .setOutputCol("label")

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val pipeline = new Pipeline().setStages(Array(featureAssemble, labelIndexer, lr))
    val model = pipeline.fit(trainingData).stages.last.asInstanceOf[LogisticRegressionModel]
    println(s"Weights: ${model.coefficients} Intercept: ${model.intercept}")

    val predictions = model.transform(testData)

    val correctCount = predictions.filter("(clas = 4.0 AND prediction = 1.0) OR (clas = 2.0 AND prediction = 0.0)").count()
    println("Accuracy: " + correctCount * 100 / predictions.count() + "%")
  }
}
