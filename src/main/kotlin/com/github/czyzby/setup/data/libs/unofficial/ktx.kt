package com.github.czyzby.setup.data.libs.unofficial

import com.github.czyzby.setup.data.platforms.Core
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.Extension


/**
 * Current version of KTX libraries.
 * @author MJ
 */
const val KTX_VERSION = "1.9.4-b1"

/**
 * Kotlin utilities for Scene2D actors API.
 * @author MJ
 */
@Extension
class KtxActors : ThirdPartyExtension() {
  override val id = "ktxActors"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/actors"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-actors")
  }
}

/**
 * Kotlin utilities for assets management.
 * @author MJ
 */
@Extension
class KtxAssets : ThirdPartyExtension() {
  override val id = "ktxAssets"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/assets"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-assets")
  }
}

/**
 * Kotlin utilities for LibGDX collections.
 * @author MJ
 */
@Extension
class KtxCollections : ThirdPartyExtension() {
  override val id = "ktxCollections"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/collections"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-collections")
  }
}

/**
 * Kotlin utilities for internationalization.
 * @author MJ
 */
@Extension
class KtxI18n : ThirdPartyExtension() {
  override val id = "ktxI18n"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/i18n"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-i18n")
  }
}

/**
 * Kotlin dependency injection without reflection usage.
 * @author MJ
 */
@Extension
class KtxInject : ThirdPartyExtension() {
  override val id = "ktxInject"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/inject"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-inject")
  }
}

/**
 * Kotlin utilities for zero-overhead logging.
 * @author MJ
 */
@Extension
class KtxLog : ThirdPartyExtension() {
  override val id = "ktxLog"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/log"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-log")
  }
}

/**
 * Kotlin utilities for math-related classes.
 * @author MJ
 */
@Extension
class KtxMath : ThirdPartyExtension() {
  override val id = "ktxMath"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/math"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-math")
  }
}

/**
 * Kotlin type-safe builders for Scene2D GUI.
 * @author MJ
 */
@Extension
class KtxScene2D : ThirdPartyExtension() {
  override val id = "ktxScene2D"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/scene2d"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-scene2d")
  }
}

/**
 * Kotlin type-safe builders for Scene2D widget styles.
 * @author MJ
 */
@Extension
class KtxStyle : ThirdPartyExtension() {
  override val id = "ktxStyle"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/style"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-style")
  }
}

/**
 * Kotlin type-safe builders for VisUI widgets.
 * @author Kotcrab
 */
@Extension
class KtxVis : ThirdPartyExtension() {
  override val id = "ktxVis"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/vis"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-vis")
  }
}

/**
 * Kotlin type-safe builders for VisUI widget styles.
 * @author MJ
 * @author Kotcrab
 */
@Extension
class KtxVisStyle : ThirdPartyExtension() {
  override val id = "ktxVisStyle"
  override val defaultVersion = KTX_VERSION
  override val url = "https://github.com/czyzby/ktx/tree/master/vis-style"

  override fun initiateDependencies(project: Project) {
    addDependency(project, Core.ID, "com.github.czyzby:ktx-vis-style")
  }
}
