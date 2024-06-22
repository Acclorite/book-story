@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://github.com/psiegman/mvn-repo/raw/master/releases")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://github.com/psiegman/mvn-repo/raw/master/releases")
        }
    }
}

rootProject.name = "Book's Story"
include(":app")
 