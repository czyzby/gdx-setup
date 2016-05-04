package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project

/**
 * Represents iOS backend.
 * @author MJ
 */
// TODO @GdxPlatform // Implement iOS platform once stable.
class iOS : Platform {
    companion object {
        const val ID = "ios"
    }

    override val id = ID

    override fun createGradleFile(project: Project): GradleFile {
        throw UnsupportedOperationException("NYI")
    }

    override fun initiate(project: Project) {
        throw UnsupportedOperationException("NYI")
    }
}