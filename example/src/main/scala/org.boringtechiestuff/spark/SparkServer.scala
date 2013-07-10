package org.boringtechiestuff.spark

import spark.SparkEnv
import unfiltered.request.{ Seg, Path }
import unfiltered.response.ResponseString

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

