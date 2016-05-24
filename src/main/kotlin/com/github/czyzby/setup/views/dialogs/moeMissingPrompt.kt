package com.github.czyzby.setup.views.dialogs

import com.github.czyzby.autumn.mvc.stereotype.ViewDialog

/**
 * Shown if Intel's Multi-OS Engine is not detected on current machine and user tries to use iOS MOE backend
 * @author Kotcrab
 */
@ViewDialog(id = "moeMissing", value = "templates/dialogs/moeMissing.lml", cacheInstance = true) class MoeMissingPrompt
