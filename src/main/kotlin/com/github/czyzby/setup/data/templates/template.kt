package com.github.czyzby.setup.data.templates

import com.github.czyzby.setup.data.files.SourceFile
import com.github.czyzby.setup.data.files.path
import com.github.czyzby.setup.data.platforms.*
import com.github.czyzby.setup.data.project.Project

/**
 * Interface shared by all project templates. Templates should be annotated with ProjectTemplate.
 * @author MJ
 */
interface Template {
    val id: String
    // Sizes are kept as strings so you can set the sizes to static values, for example: MainClass.WIDTH.
    val width: String
        get() = "640"
    val height: String
        get() = "480"
    /**
     * Used as project description in README file. Optional.
     */
    val description: String
        get() = ""

    /**
     * @param project is being created. Should contain sources provided by this template.
     */
    fun apply(project: Project) {
        addApplicationListener(project)
        addAndroidLauncher(project)
        addDesktopLauncher(project)
        addDragomeLauncher(project)
        addGwtLauncher(project)
        addHeadlessLauncher(project)
        addIOSLauncher(project)
        addServerLauncher(project)
        project.readmeDescription = description
    }

    fun addApplicationListener(project: Project) {
        addSourceFile(project = project, platform = Core.ID, packageName = project.basic.rootPackage,
                fileName = "${project.basic.mainClass}.java", content = getApplicationListenerContent(project));
    }

    /**
     * @param project is being created.
     * @return content of Java class implementing ApplicationListener.
     */
    fun getApplicationListenerContent(project: Project): String

    fun addDesktopLauncher(project: Project) {
        addSourceFile(project = project, platform = Desktop.ID, packageName = "${project.basic.rootPackage}.desktop",
                fileName = "DesktopLauncher.java", content = getDesktopLauncherContent(project));
    }

    fun getDesktopLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new ${project.basic.mainClass}(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "${project.basic.name}";
        configuration.width = ${width};
        configuration.height = ${height};
        return configuration;
    }
}"""

    fun addGwtLauncher(project: Project) {
        addSourceFile(project = project, platform = GWT.ID, packageName = "${project.basic.rootPackage}.gwt",
                fileName = "GwtLauncher.java", content = getGwtLauncherContent(project));
    }

    fun getGwtLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(${width}, ${height});
        return configuration;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new ${project.basic.mainClass}();
    }
}"""

    fun addAndroidLauncher(project: Project) {
        addSourceFile(project = project, platform = Android.ID, packageName = "${project.basic.rootPackage}.android",
                fileName = "AndroidLauncher.java", content = getAndroidLauncherContent(project));
    }

    fun getAndroidLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        initialize(new ${project.basic.mainClass}(), configuration);
    }
}"""

    fun addIOSLauncher(project: Project) {
        // TODO iOS launcher, once stable
    }

    fun addDragomeLauncher(project: Project) {
        // TODO Dragome launcher, once released
    }

    fun addHeadlessLauncher(project: Project) {
        addSourceFile(project = project, platform = Headless.ID, packageName = "${project.basic.rootPackage}.headless",
                fileName = "HeadlessLauncher.java", content = getHeadlessLauncherContent(project));
    }

    fun getHeadlessLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the headless application. Can be converted into a utilities project or a server application. */
public class HeadlessLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static Application createApplication() {
        // Note: you can use a custom ApplicationListener implementation for the headless project instead of ${project.basic.mainClass}.
        return new HeadlessApplication(new ${project.basic.mainClass}(), getDefaultConfiguration());
    }

    private static HeadlessApplicationConfiguration getDefaultConfiguration() {
        HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
        configuration.renderInterval = -1f; // When this value is negative, ${project.basic.mainClass}#render() is never called.
        return configuration;
    }
}"""

    fun addServerLauncher(project: Project) {
        addSourceFile(project = project, platform = Server.ID, packageName = "${project.basic.rootPackage}.server",
                fileName = "ServerLauncher.java", content = getServerLauncherContent(project));
    }

    fun getServerLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.server;

/** Launches the server application. */
public class ServerLauncher {
    public static void main(String[] args) {
        // TODO Implement server application.
    }
}"""

    fun addSourceFile(project: Project, platform: String, packageName: String, fileName: String,
                      content: String, sourceFolderPath: String = path("src", "main", "java")) {
        if (project.hasPlatform(platform)) {
            project.files.add(SourceFile(projectName = platform, sourceFolderPath = sourceFolderPath,
                    packageName = packageName, fileName = fileName, content = content))
        }
    }
}
