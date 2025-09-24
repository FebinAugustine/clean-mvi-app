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

rootProject.name = "Clean MVI App"
include(":app")
include(":core")

include(":shared_domain")
include(":feature:authentication:domain")
include(":feature:authentication:data")
include(":feature:authentication:presentation")

include(":feature:userdashboard:domain")
include(":feature:userdashboard:data")
include(":feature:userdashboard:presentation")
