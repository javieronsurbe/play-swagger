
package com.iheart.playSwagger

import play.api.libs.json.Json
import sbt._
import Keys._

import scala.util.Success

object SwaggerPlayPlugin extends AutoPlugin {
  implicit val cl = getClass.getClassLoader

  override def trigger = allRequirements

  //  override def requires = Play
  object swaggerPlayPluginConfig {
    lazy val swaggerPlayPackageNames = settingKey[Seq[String]]("swagger packagenames")
    lazy val swaggerPlayRoutesFile = settingKey[String]("swagger routesfile")
    lazy val swaggerPlaySpecsFile = settingKey[File]("swagger specsfile")
  }

  import swaggerPlayPluginConfig._

  override lazy val projectSettings = Seq(commands += swaggerPlugin)

  lazy val swaggerPlugin =
    Command.command("generate-swagger") {
      (state: State) ⇒
        //FIXME Check presence of parameter
        val generator =
          SwaggerSpecGenerator(swaggerPlayPackageNames.value: _*).
            generate(swaggerPlayRoutesFile.value)
        //FIXME Check presence of parameter
        val out = new java.io.FileWriter(swaggerPlaySpecsFile.value)

        generator match {
          case Success(value) ⇒ out.write(Json.prettyPrint(value))
          case _              ⇒
        }
        out.close()
        state
    }

}
