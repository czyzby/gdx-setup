package com.github.czyzby.setup.prefs

import com.github.czyzby.autumn.mvc.stereotype.preference.Property
import com.github.czyzby.kiwi.util.common.Strings

/**
 * Saves Android SDK path. Needless to say, this setting is unlikely to change once set.
 * @author MJ
 */
@Property("AndroidSdk")
class AndroidSdkPreference : AbstractStringPreference() {
}
