package com.github.czyzby.setup.data.libs.unofficial

import com.github.czyzby.setup.data.platforms.Core
import com.github.czyzby.setup.data.platforms.GWT
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.Extension


/**
 * Version of SquidLib libraries.
 * @author SquidPony
 */
const val SQUID_LIB_VERSION = "3.0.0-b6"
/**
 * URL of SquidLib libraries.
 * @author SquidPony
 */
const val SQUID_LIB_URL = "https://github.com/SquidPony/SquidLib"

/**
 * Cross-platform regex utilities.
 * @author Tommy Ettinger
 */
@Extension
class RegExodus : ThirdPartyExtension() {
    override val id = "regExodus"
    override val defaultVersion = "0.1.7"
    override val url = "https://github.com/tommyettinger/RegExodus"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "com.github.tommyettinger:regexodus")

        addDependency(project, GWT.ID, "com.github.tommyettinger:regexodus:sources")
        addGwtInherit(project, "regexodus")
    }
}

/**
 * Utility for grid-based games.
 * @author SquidPony
 */
@Extension
class SquidLibUtil : ThirdPartyExtension() {
    override val id = "squidLibUtil"
    override val defaultVersion = SQUID_LIB_VERSION
    override val url = SQUID_LIB_URL

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "com.squidpony:squidlib-util")

        addDependency(project, GWT.ID, "com.squidpony:squidlib-util:sources")
        addGwtInherit(project, "squidlib-util")

        RegExodus().initiate(project)
    }
}

/**
 * Utility for roguelike games.
 * @author SquidPony
 */
@Extension
class SquidLib : ThirdPartyExtension() {
    override val id = "squidLib"
    override val defaultVersion = SQUID_LIB_VERSION
    override val url = SQUID_LIB_URL

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "com.squidpony:squidlib")

        addDependency(project, GWT.ID, "com.squidpony:squidlib:sources")
        addGwtInherit(project, "squidlib")

        SquidLibUtil().initiate(project)
    }
}
