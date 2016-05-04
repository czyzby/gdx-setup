package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project

/**
 * Represents Dragome backend.
 * @author MJ
 */
// TODO @GdxPlatform // Implement Dragome platform
class Dragome : Platform {
    companion object {
        const val ID = "dragome"
    }

    override val id = ID
    override fun createGradleFile(project: Project): GradleFile {
        throw UnsupportedOperationException("NYI")
    }

    override fun initiate(project: Project) {
        throw UnsupportedOperationException("NYI")
    }
}