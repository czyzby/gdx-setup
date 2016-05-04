package com.github.czyzby.setup.views

import com.github.czyzby.autumn.annotation.Processor
import com.github.czyzby.autumn.context.Context
import com.github.czyzby.autumn.context.ContextDestroyer
import com.github.czyzby.autumn.context.ContextInitializer
import com.github.czyzby.autumn.processor.AbstractAnnotationProcessor
import com.github.czyzby.lml.annotation.LmlActor
import com.github.czyzby.lml.scene2d.ui.reflected.ButtonTable
import com.github.czyzby.setup.data.templates.Template
import com.github.czyzby.setup.data.templates.official.*

/**
 * Handles templates tab.
 * @author MJ
 */
@Processor
class TemplatesData : AbstractAnnotationProcessor<ProjectTemplate>() {
    val templates = mutableListOf<Template>()
    @LmlActor("templatesTable") private lateinit var templatesTable: ButtonTable

    fun getSelectedTemplate(): Template = templates.first { it.id == templatesTable.buttonGroup.checked.name }

    // Automatic scanning of project templates:

    override fun getSupportedAnnotationType(): Class<ProjectTemplate> = ProjectTemplate::class.java
    override fun isSupportingTypes(): Boolean = true
    override fun processType(type: Class<*>, annotation: ProjectTemplate, component: Any, context: Context,
                             initializer: ContextInitializer, contextDestroyer: ContextDestroyer) {
        templates.add(component as Template)
    }
}

/**
 * Should annotate all project templates.
 * @author MJ
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProjectTemplate