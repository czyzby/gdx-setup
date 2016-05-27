package com.github.czyzby.setup.data.platforms.unofficial

import com.github.czyzby.setup.data.files.CopiedFile
import com.github.czyzby.setup.data.files.path
import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.platforms.Core
import com.github.czyzby.setup.data.platforms.Desktop
import com.github.czyzby.setup.data.platforms.Platform
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.GdxPlatform

/**
 * Unofficial backend, which uses JTransc to transpile Java bytecode to Haxe. Uses Lime (Haxe OpenGL library) to
 * support LibGDX features. Allows to distribute the same application to Windows, Linux, Mac, Android, iOS, Tizen
 * and HTML5 (JS/WebGL). Flash support is planned. Since it does not rely on Java sources like GWT does, it allows
 * to target the web platform with other JVM languages like Kotlin.
 * @author Carlos Ballesteros Velasco
 * @author MJ
 */
@GdxPlatform
class JTransc : Platform {
    companion object {
        const val ID = "jtransc"
    }

    override val id = ID
    override fun createGradleFile(project: Project): GradleFile = JTranscGradleFile(project)
    override fun initiate(project: Project) {
        project.properties["${id}Version"] = project.advanced.jtranscVersion
        project.rootGradle.buildDependencies.add("\"com.jtransc:jtransc-gradle-plugin:\$${id}Version\"")

        // Adding game icons:
        arrayOf(16, 32, 64, 128).forEach {
            val icon = "libgdx${it}.png"
            project.files.add(CopiedFile(projectName = JTransc.ID, path = path("src", "main", "resources", icon),
                    original = path("icons", icon)))
        }

        addGradleTaskDescription(project, "runJtransc", "runs the application on the default platform. Note that all JTransc tasks require installing Haxe and running `haxelib install lime`, `haxelib install hxcpp` commands.")
        addGradleTaskDescription(project, "run", "starts a Java application. Use for testing only.")
        addGradleTaskDescription(project, "runCpp",
                "transpiles Java bytecode to Haxe, and then to C++ to compile a native application in debug mode. This should be used for testing only - while proper stacks are printed, the application is much slower in this mode.")
        addGradleTaskDescription(project, "distCpp",
                "transpiles Java bytecode to Haxe, and then to C++ to compile a native application for your current platform. It will be available in `build/jtransc-haxe/export/release` folder. Make sure to set up Lime with `haxelib run lime setup windows` (or `linux`, `mac`).")
        addGradleTaskDescription(project, "runJs",
                "transpiles Java bytecode to Haxe, and then to JavaScript to prepare a HTML5 application in debug mode. Visit [localhost:3000](http://localhost:3000) to test it.")
        addGradleTaskDescription(project, "distJs",
                "transpiles Java bytecode to Haxe, and then to JavaScript to prepare a HTML5 application in release mode. It will be available in `build/jtransc-haxe/export/release` folder. Use any HTTP server to deploy the application.")
        // TODO Add descriptions of other JTransc tasks once they become stable.
    }
}

class JTranscGradleFile(val project: Project) : GradleFile(JTransc.ID) {
    val jtranscDependencies = mutableListOf<String>()
    init {
        dependencies.add("project(':${Core.ID}')")
        addDependency("com.badlogicgames.gdx:gdx-backend-lwjgl:\$gdxVersion")
        addDependency("com.badlogicgames.gdx:gdx-platform:\$gdxVersion:natives-desktop")
        addJTranscDependency("com.jtransc.gdx:gdx-backend-jtransc:\$jtranscVersion")
    }

    /**
     * @param dependency will be added as "compile" dependency, quoted.
     */
    fun addJTranscDependency(dependency: String) = jtranscDependencies.add("\"$dependency\"")

    override fun getContent(): String = """apply plugin: "application"
apply plugin: "jtransc"

mainClassName = "${project.basic.rootPackage}.jtransc.JTranscLauncher"
sourceSets.main.resources.srcDirs += [ rootProject.file('assets').absolutePath ]
sourceCompatibility = ${project.advanced.desktopJavaVersion}
eclipse.project.name = appName + '-jtransc'

dependencies {
${joinDependencies(dependencies = jtranscDependencies, type = "jtransc")}
${joinDependencies(dependencies)}}

jtransc {
	// Optional properties (https://github.com/jtransc/jtransc/blob/master/jtransc-gradle-plugin/src/com/jtransc/gradle/JTranscExtension.kt)
	title = '${project.basic.name}'
	name = '${project.basic.name}'
	version = '${project.advanced.version}'
	company = "${project.basic.name}"
	package_ = "${project.basic.rootPackage}"
	assets = [rootProject.file('assets').absolutePath]
	embedResources = true
	vsync = true
	relooper = true
	minimizeNames = false
	analyzer = false

	customTarget("cpp", "haxe:cpp", "exe")
	customTarget("windows", "haxe:windows", "exe")
	customTarget("linux", "haxe:linux", "bin")
	customTarget("mac", "haxe:mac", "app")
	customTarget("android", "haxe:android", "apk")
	customTarget("ios", "haxe:ios", "ipa")
	customTarget("tizen", "haxe:tizen", "app")
	customTargetMinimized("html5", "haxe:html5", "js")
}

run {
  ignoreExitValue = true
}"""

}
