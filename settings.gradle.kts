pluginManagement {
    repositories {
        google()
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

include(":shared_domain")
include(":feature:authentication:domain")
include(":feature:authentication:data")
include(":feature:authentication:presentation")

include(":feature:userdashboard:domain")
include(":feature:userdashboard:data")
include(":feature:userdashboard:presentation")
include(":feature:admindashboard:domain")
include(":feature:admindashboard:data")
include(":feature:admindashboard:presentation")
include(":core:ui")

include(":di")
include(":core:data")
