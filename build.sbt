import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.{Matcher, Pattern}

import com.typesafe.sbt.packager.archetypes.ServerLoader

name := """showcase"""

version := "1.0-SNAPSHOT"

lazy val showcase = (project in file(".")).enablePlugins(PlayScala, SbtWeb, JavaServerAppPackaging, DebianPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

defaultLinuxInstallLocation := "/var/novapost"

serverLoading in Debian := ServerLoader.SystemV

version in Debian := getDebianVersion(version.value, true, new Date())

daemonUser in Linux := "nova"

daemonGroup in Linux := "nova"

description := "Sbt native packeger showcase"

maintainer := "Jamal CHAQOURI <jamal.chaqouri@gmail.com>"

packageDescription := "Showcase package"

packageDescription in Debian := "Showcase packet"

packageSummary := "Showcase cases"

debianNativeBuildOptions in Debian := Seq("-Zgzip", "-z3")

val SNAPSHOT_PATTERN : Pattern = Pattern.compile("(.*)[\\-\\+]SNAPSHOT")
val BETA_PATTERN : Pattern = Pattern.compile("(.*?)([\\.\\-_]?)(alpha|a|beta|b|milestone|m|cr|rc)(.*)", Pattern.CASE_INSENSITIVE)

def getDebianVersion(version: String, apply: Boolean, timestamp : Date) : String = {
  var finalVersion : String = version
  var matcher : Matcher = SNAPSHOT_PATTERN.matcher(finalVersion)
  if (matcher.matches()) {
    finalVersion = matcher.group(1) + "~"
    if (apply) {
      val snapshot : String = new SimpleDateFormat("yyyyMMddHHmmss").format(timestamp)
      finalVersion += snapshot
    } else {
      finalVersion += "SNAPSHOT"
    }
  } else {
    matcher = BETA_PATTERN.matcher(finalVersion)
    if (matcher.matches()) {
      finalVersion = matcher.group(1) + "~" + matcher.group(3) + matcher.group(4)
    }
  }
  finalVersion.replaceAll("[^\\.+~A-Za-z0-9]", "+").replaceAll("\\++", "+")
}
