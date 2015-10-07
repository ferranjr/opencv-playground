name := "FaceDetection"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xlint")

val javacppVersion = "1.0"

val opencvVersion = "3.0.0"

// Platform classifier for native library dependencies
lazy val platform = org.bytedeco.javacpp.Loader.getPlatform

// Some dependencies like `javacpp` are packaged with maven-plugin packaging
classpathTypes += "maven-plugin"

libraryDependencies ++= Seq(
  "org.bytedeco"                 % "javacpp"         % javacppVersion,
  "org.bytedeco"                 % "javacv"          % javacppVersion,
  "org.bytedeco.javacpp-presets" % "opencv"          % ( opencvVersion + "-" + javacppVersion) classifier platform classifier "",
  "org.scala-lang.modules"      %% "scala-swing"     % "1.0.1",
  "junit"                        % "junit"           % "4.12" % "test",
  "com.novocode"                 % "junit-interface" % "0.11" % "test"
)

autoCompilerPlugins := true