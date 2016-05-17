package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.files.SourceFile
import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.GdxPlatform
import java.util.*

/**
 * Represents GWT backend.
 * @author MJ
 */
@GdxPlatform
class GWT : Platform {
    companion object {
        const val ID = "gwt"
        const val BASIC_INHERIT = "com.badlogic.gdx.backends.gdx_backends_gwt"
        val INHERIT_COMPARATOR = Comparator<kotlin.String> { a, b ->
            // Basic GWT inherit has to be first:
            if (a == BASIC_INHERIT) {
                -1
            } else if (b == BASIC_INHERIT) {
                1
            } else {
                a.compareTo(b)
            }
        }
    }

    override val id = ID

    override fun createGradleFile(project: Project): GradleFile = GWTGradleFile(project)

    override fun initiate(project: Project) {
        addGradleTaskDescription(project, "superDev", "compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/${id}](http://localhost:8080/${id}). Use only during development.")
        addGradleTaskDescription(project, "dist", "compiles GWT sources. The compiled application can be found at `${id}/build/dist`: you can use any HTTP server to deploy it.")

        project.gwtInherits.add(BASIC_INHERIT)
        project.properties["${id}FrameworkVersion"] = project.advanced.gwtVersion
        project.properties["gwtPluginVersion"] = project.advanced.gwtPluginVersion
        project.rootGradle.buildDependencies.add("\"de.richsource.gradle.plugins:gwt-gradle-plugin:\$gwtPluginVersion\"")

        // Adding GWT definition to core project:
        project.files.add(SourceFile(projectName = Core.ID, packageName = project.basic.rootPackage,
                fileName = "${project.basic.mainClass}.gwt.xml", content = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE module PUBLIC "-//Google Inc.//DTD Google Web Toolkit ${project.advanced.gwtVersion}//EN" "https://gwt.googlesource.com/gwt/+/${project.advanced.gwtVersion}/distro-source/core/src/gwt-module.dtd">
<module>
    <source path="" />${(project.reflectedClasses + project.reflectedPackages).joinToString(separator = "\n", prefix = "\n") { "    <extend-configuration-property name=\"gdx.reflect.include\" value=\"$it\" />" }}
</module>"""))
        project.gwtInherits.add("${project.basic.rootPackage}.${project.basic.mainClass}")

        // Adding GWT definition to shared project:
        if (project.hasPlatform(Shared.ID)) {
            project.files.add(SourceFile(projectName = Shared.ID, packageName = project.basic.rootPackage,
                    fileName = "Shared.gwt.xml", content = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE module PUBLIC "-//Google Inc.//DTD Google Web Toolkit ${project.advanced.gwtVersion}//EN" "https://gwt.googlesource.com/gwt/+/${project.advanced.gwtVersion}/distro-source/core/src/gwt-module.dtd">
<module>
    <source path="" />
</module>"""))
            project.gwtInherits.add("${project.basic.rootPackage}.Shared")
        }

        // Adding GWT definition:
        project.files.add(SourceFile(projectName = ID, packageName = project.basic.rootPackage,
                fileName = "GdxDefinition.gwt.xml", content = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE module PUBLIC "-//Google Inc.//DTD Google Web Toolkit ${project.advanced.gwtVersion}//EN" "https://gwt.googlesource.com/gwt/+/${project.advanced.gwtVersion}/distro-source/core/src/gwt-module.dtd">
<module rename-to="html">
    <source path="" />
${project.gwtInherits.sortedWith(INHERIT_COMPARATOR).joinToString(separator = "\n") { "    <inherits name=\"$it\" />" }}
    <entry-point class="${project.basic.rootPackage}.gwt.GwtLauncher" />
    <set-configuration-property name="gdx.assetpath" value="../assets" />
</module>"""))

        // Adding SuperDev definition:
        project.files.add(SourceFile(projectName = ID, packageName = project.basic.rootPackage,
                fileName = "GdxDefinitionSuperdev.gwt.xml", content = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE module PUBLIC "-//Google Inc.//DTD Google Web Toolkit ${project.advanced.gwtVersion}//EN" "https://gwt.googlesource.com/gwt/+/${project.advanced.gwtVersion}/distro-source/core/src/gwt-module.dtd">
<module rename-to="html">
    <inherits name="${project.basic.rootPackage}.GdxDefinition" />
    <collapse-all-properties />
    <add-linker name="xsiframe"/>
    <set-configuration-property name="devModeRedirectEnabled" value="true"/>
    <set-configuration-property name="xsiframe.failIfScriptTag" value="FALSE"/>
</module>"""))

        // Copying webapp files:
        addCopiedFile(project, "webapp", "index.html")
        addCopiedFile(project, "webapp", "refresh.png")
        addCopiedFile(project, "webapp", "soundmanager2-jsmin.js")
        addCopiedFile(project, "webapp", "soundmanager2-setup.js")
        addCopiedFile(project, "webapp", "styles.css")
        addCopiedFile(project, "webapp", "WEB-INF", "web.xml")
    }
}

class GWTGradleFile(val project: Project) : GradleFile(GWT.ID) {
    init {
        buildDependencies.add("project(':${Core.ID}')")
        dependencies.add("project(':${Core.ID}')")

        addDependency("com.badlogicgames.gdx:gdx:\$gdxVersion:sources")
        addDependency("com.badlogicgames.gdx:gdx-backend-gwt:\$gdxVersion")
        addDependency("com.badlogicgames.gdx:gdx-backend-gwt:\$gdxVersion:sources")
    }

    override fun getContent(): String = """apply plugin: 'gwt'
apply plugin: 'war'
apply plugin: 'jetty'

gwt {
  gwtVersion = gwtFrameworkVersion // Should match the version used for building the GWT backend. See gradle.properties.
  maxHeapSize = '1G' // Default 256m is not enough for the GWT compiler. GWT is HUNGRY.
  minHeapSize = '1G'

  src = files(file('src/main/java')) // Needs to be in front of "modules" below.
  modules '${project.basic.rootPackage}.GdxDefinition'
  devModules '${project.basic.rootPackage}.GdxDefinitionSuperdev'
  project.webAppDirName = 'webapp'

  compiler {
    strict = true
    enableClosureCompiler = true
    disableCastChecking = true
  }
}

dependencies {
${joinDependencies(dependencies)}}

task draftRun(type: JettyRunWar) {
  dependsOn draftWar
  dependsOn.remove('war')
  webApp = draftWar.archivePath
  daemon = true
}

task superDev(type: de.richsource.gradle.plugins.gwt.GwtSuperDev) {
  dependsOn draftRun
  doFirst {
  	gwt.modules = gwt.devModules
  }
}

task dist(dependsOn: [clean, compileGwt]) {
  doLast {
    file("build/dist").mkdirs()
    copy {
      from "build/gwt/out"
      into "build/dist"
    }
    copy {
      from "webapp"
      into "build/dist"
      }
    copy {
      from "war"
      into "build/dist"
    }
  }
}

draftWar {
  from "war"
}

task addSource << {
${buildDependencies.joinToString(separator = "") {
        "  sourceSets.main.compileClasspath += files($it.sourceSets.main.allJava.srcDirs)\n"
    }}}

tasks.compileGwt.dependsOn(addSource)
tasks.draftCompileGwt.dependsOn(addSource)
eclipse.project.name = appName + '-gwt'
"""
}
