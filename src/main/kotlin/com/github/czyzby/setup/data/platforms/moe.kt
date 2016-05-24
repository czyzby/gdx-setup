package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.files.CopiedFile
import com.github.czyzby.setup.data.files.SourceFile
import com.github.czyzby.setup.data.files.path
import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.GdxPlatform

/**
 * Represents iOS MOE backend.
 * @author Kotcrab
 */
@GdxPlatform
class MOE : Platform {
    companion object {
        const val ID = "ios-moe"
    }

    override val id = ID
    override val isGraphical = false // Will not be selected as LibGDX client platform. iOS is the default one.

    override fun createGradleFile(project: Project): GradleFile = MOEGradleFile(project);

    override fun initiate(project: Project) {
        project.rootGradle.buildDependencies.add("\"com.intel.gradle:moeGradlePlugin:\$moeVersion\"")
        project.properties["moeVersion"] = project.advanced.moeVersion
        project.rootGradle.buildRepositories.add("maven { url uri(System.getenv(\"INTEL_MULTI_OS_ENGINE_HOME\") + \"/gradle\") }")

        project.files.add(SourceFile(projectName = ID, sourceFolderPath = path("xcode", "ios-moe"), fileName = "Info.plist", packageName = "", content = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>CFBundleDevelopmentRegion</key>
	<string>en</string>
	<key>CFBundleExecutable</key>
	<string>$(EXECUTABLE_NAME)</string>
	<key>CFBundleIdentifier</key>
	<string>${project.basic.rootPackage}</string>
	<key>CFBundleInfoDictionaryVersion</key>
	<string>6.0</string>
	<key>CFBundleName</key>
	<string>${project.basic.name}</string>
	<key>CFBundlePackageType</key>
	<string>APPL</string>
	<key>CFBundleShortVersionString</key>
	<string></string>
	<key>CFBundleSignature</key>
	<string>????</string>
	<key>CFBundleVersion</key>
	<string></string>
	<key>LSRequiresIPhoneOS</key>
	<true/>
	<key>UIRequiresFullScreen</key>
	<true/>
	<key>Intel.MOE.Main.Class</key>
	<string>${project.basic.rootPackage}.IOSMoeLauncher</string>
	<key>UIApplicationExitsOnSuspend</key>
	<false/>
	<key>UIRequiredDeviceCapabilities</key>
	<array>
		<string>armv7</string>
	</array>
	<key>UISupportedInterfaceOrientations</key>
	<array>
		<string>UIInterfaceOrientationPortrait</string>
		<string>UIInterfaceOrientationPortraitUpsideDown</string>
		<string>UIInterfaceOrientationLandscapeLeft</string>
		<string>UIInterfaceOrientationLandscapeRight</string>
	</array>
	<key>UISupportedInterfaceOrientations~ipad</key>
	<array>
		<string>UIInterfaceOrientationPortrait</string>
		<string>UIInterfaceOrientationPortraitUpsideDown</string>
		<string>UIInterfaceOrientationLandscapeLeft</string>
		<string>UIInterfaceOrientationLandscapeRight</string>
	</array>
</dict>
</plist>"""))

        arrayOf("Default.png", "Default@2x.png", "Default@2x~ipad.png", "Default-375w-667h@2x.png",
                "Default-414w-736h@3x.png", "Default-568h@2x.png", "Default~ipad.png", "Icon.png",
                "Icon@2x.png", "Icon-72.png", "Icon-72@2x.png").forEach {
            project.files.add(CopiedFile(projectName = ID, path = path("resources", it),
                    original = path("generator", "ios-moe", "resources", it)))
        }

        arrayOf("build.xcconfig", "custom.xcconfig", "main.cpp").forEach {
            project.files.add(CopiedFile(projectName = ID, path = path("xcode", "ios-moe", it),
                    original = path("generator", "ios-moe", "xcode", "ios-moe", it)))
        }

        arrayOf("build.xcconfig", "Info-Test.plist").forEach {
            project.files.add(CopiedFile(projectName = ID, path = path("xcode", "ios-moe-Test", it),
                    original = path("generator", "ios-moe", "xcode", "ios-moe-Test", it)))
        }

        // Warning: This file originally uses %ASSET_PATH% which was replaced by gdx-setup assets path (../../../assets)
        // Be careful when updating
        project.files.add(CopiedFile(projectName = ID, path = path("xcode", "ios-moe.xcodeproj", "project.pbxproj"),
                original = path("generator", "ios-moe", "xcode", "ios-moe.xcodeproj", "project.pbxproj")))
    }
}

class MOEGradleFile(val project: Project) : GradleFile(MOE.ID) {
    init {
        dependencies.add("project(':${Core.ID}')")
        addDependency("com.badlogicgames.gdx:gdx-backend-moe:\$gdxVersion")
        addDependency("com.badlogicgames.gdx:gdx-platform:\$gdxVersion:natives-ios")
    }

    override fun getContent() = """apply plugin: 'moe'

task copyNatives << {
    file("xcode/native/ios/").mkdirs();
    def LD_FLAGS = "LIBGDX_NATIVES = "
    configurations.natives.files.each { jar->
        def outputDir = null
        if (jar.name.endsWith("natives-ios.jar")) outputDir = file("xcode/native/ios")
        if (outputDir != null) {
            FileCollection fileCollection = zipTree(jar)
            for (File libFile : fileCollection) {
                if (libFile.getAbsolutePath().endsWith(".a") && !libFile.getAbsolutePath().contains("/tvos/")) {
                    copy {
                        from libFile.getAbsolutePath()
                        into outputDir
                    }
                    LD_FLAGS += " -force_load \${'$'}{SRCROOT}/native/ios/" + libFile.getName()
                }
            }
        }
    }
    def outFlags = file("xcode/ios-moe/custom.xcconfig");
    outFlags.write LD_FLAGS

    def proguard = file("/Applications/Intel/multi_os_engine/tools/proguard.cfg")
    if (proguard.exists()) {
        if (!proguard.text.contains("-keep class com.badlogic.**")) {
            proguard << "\n-keep class com.badlogic.** { *; }\n"
            proguard << "-keep enum com.badlogic.** { *; }\n"
        }
    }
}

task createBuildDir {
    def file = new File('build/xcode/ios-moe.xcodeproj')
    if (!file.exists()) {
        file.mkdirs();
    }
}

moe {
    mainClassName 'IOSMoeLauncher'
    xcode {
        mainTarget 'ios-moe'
        packageName '${project.basic.rootPackage}'
        deploymentTarget = '9.0'
        xcodeProjectDirPath 'xcode'
        generateProject false
    }
}

moeMainReleaseIphoneosXcodeBuild.dependsOn copyNatives
moeMainDebugIphoneosXcodeBuild.dependsOn copyNatives
moeMainReleaseIphonesimulatorXcodeBuild.dependsOn copyNatives
moeMainDebugIphonesimulatorXcodeBuild.dependsOn copyNatives

dependencies {
  configurations { natives }

${joinDependencies(dependencies)}}
"""

}
