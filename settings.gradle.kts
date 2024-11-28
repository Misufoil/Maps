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
    }
}

rootProject.name = "Maps"
include(":app")
include(":features:main-map-impl")
include(":features:search-bar-impl")
include(":features:main-map-api")
include(":features:search-bar-api")
include(":navigation")
include(":di")
include(":core-utils")
include(":features:details-dialog-api")
include(":features:details-dialog-impl")
