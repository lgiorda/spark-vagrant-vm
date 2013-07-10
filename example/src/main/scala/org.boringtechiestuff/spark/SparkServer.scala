package org.boringtechiestuff.spark

import spark.SparkEnv
import unfiltered.request.{ Seg, Path }
import unfiltered.response.ResponseString

/**
 * Trying to draw inspiration from this talk:
 * http://files.meetup.com/3138542/Quantifind%20Spark%20User%20Group%20Talk.pdf
 *
 * What exactly does the SparkEnv.get/set function do, and why call them on
 * each route access?
 *
 *
 *
    vagrant ssh

    hadoop fs -mkdir /lib
    hadoop fs -put /vagrant/target/scala-2.9.3/spark-assembly-1-SNAPSHOT.jar /lib

    java -cp /vagrant/target/scala-2.9.3/spark-assembly-1-SNAPSHOT.jar org.boringtechiestuff.spark.SparkServer

 */

object SparkServer extends SparkApp with unfiltered.filter.Plan {

  val sparkEnv = SparkEnv.get
  val myData = (1 to 20000).toSeq
  val workingSet = context.makeRDD(myData).cache()

  def intent = {
    case Path(Seg("count" :: _)) => {
      SparkEnv.set(sparkEnv)
      val number = workingSet.count()
      ResponseString(number.toString)
    }
  }

  unfiltered.jetty.Http.local(9999).filter(this).run()
}

