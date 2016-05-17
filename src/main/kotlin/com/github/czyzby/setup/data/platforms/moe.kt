package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project

/**
 * Represents iOS MOE backend.
 */
// @GdxPlatform // TODO Implement iOS MOE backend.
class MOE : Platform {
    companion object {
        const val ID = "ios-moe"
    }

    override val id = ID
    override val isGraphical = false // Will not be selected as LibGDX client platform. iOS is the default one.

    override fun createGradleFile(project: Project): GradleFile {
        throw UnsupportedOperationException()
    }

    override fun initiate(project: Project) {
        throw UnsupportedOperationException()
    }
}
