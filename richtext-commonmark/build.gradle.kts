plugins {
  id("richtext-kmp-library")
  id("org.jetbrains.compose") version Compose.desktopVersion
  id("org.jetbrains.kotlin.plugin.compose") version Kotlin.version
  id("org.jetbrains.dokka")
}

repositories {
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

android {
  namespace = "com.halilibo.richtext.commonmark"
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)

        implementation(Commonmark.core)
        implementation(Commonmark.tables)
        implementation(Commonmark.strikethrough)
        implementation(Commonmark.autolink)

        api(project(":richtext-ui"))
        api(project(":richtext-markdown"))
      }
    }
    val commonTest by getting

    val androidMain by getting {
      kotlin.srcDir("src/commonJvmAndroid/kotlin")
      dependencies {
      }
    }

    val jvmMain by getting {
      kotlin.srcDir("src/commonJvmAndroid/kotlin")
      dependencies {

      }
    }

    val jvmTest by getting {
      kotlin.srcDir("src/commonJvmAndroidTest/kotlin")
      dependencies {
        implementation(Kotlin.Test.jdk)
      }
    }
  }
}
