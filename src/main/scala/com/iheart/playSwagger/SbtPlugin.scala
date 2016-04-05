
package com.iheart.playSwagger

import play.api.libs.json.Json
import sbt._
import Keys._

import scala.util.Success

object SbtPlugin extends AutoPlugin{
  implicit val cl = getClass.getClassLoader
  override def trigger = allRequirements
//  override def requires = Play
  lazy val packageNames=settingKey[Seq[String]]("swagger packagenames")
  lazy val routesFile=settingKey[String]("swagger routesfile")
  lazy val specsFile=settingKey[File]("swagger specsfile")

  override  lazy val projectSettings = Seq(commands += swaggerPlugin)

  lazy val swaggerPlugin =
    Command.command("generate-swagger"){
      (state:State) =>
        //FIXME Check presence of parameter
        val generator=
          SwaggerSpecGenerator(packageNames.value:_*).
            generate(routesFile.value)
        //FIXME Check presence of parameter
        val out = new java.io.FileWriter(specsFile.value)

        generator match {
          case Success(value)=>out.write(Json.prettyPrint(value))
          case _ =>
        }
        out.close()
        state
    }

}
