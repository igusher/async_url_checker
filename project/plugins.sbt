
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url("bintray-sbt-plugins", url("https://dl.bintray.com/sbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3")

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "2.1.0")
