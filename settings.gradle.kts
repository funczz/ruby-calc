pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://funczz.github.io/kotlin-migration") }
        maven { setUrl("https://funczz.github.io/kotlin-notifier") }
        maven { setUrl("https://funczz.github.io/kotlin-sam") }
    }
}

rootProject.name = "Ruby Calc"
include(":app")
