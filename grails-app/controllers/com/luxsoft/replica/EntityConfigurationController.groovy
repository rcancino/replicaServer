package com.luxsoft.replica

import org.springframework.dao.DataIntegrityViolationException

/**
 * EntityConfigurationController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class EntityConfigurationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [entityConfigurationInstanceList: EntityConfiguration.list(params), entityConfigurationInstanceTotal: EntityConfiguration.count()]
    }

    def create() {
        [entityConfigurationInstance: new EntityConfiguration(params)]
    }

    def save() {
        def entityConfigurationInstance = new EntityConfiguration(params)
        if (!entityConfigurationInstance.save(flush: true)) {
            render(view: "create", model: [entityConfigurationInstance: entityConfigurationInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), entityConfigurationInstance.id])
        redirect(action: "show", id: entityConfigurationInstance.id)
    }

    def show() {
        def entityConfigurationInstance = EntityConfiguration.get(params.id)
        if (!entityConfigurationInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), params.id])
            redirect(action: "list")
            return
        }

        [entityConfigurationInstance: entityConfigurationInstance]
    }

    def edit() {
        def entityConfigurationInstance = EntityConfiguration.get(params.id)
        if (!entityConfigurationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), params.id])
            redirect(action: "list")
            return
        }

        [entityConfigurationInstance: entityConfigurationInstance]
    }

    def update() {
        def entityConfigurationInstance = EntityConfiguration.get(params.id)
        if (!entityConfigurationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (entityConfigurationInstance.version > version) {
                entityConfigurationInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'entityConfiguration.label', default: 'EntityConfiguration')] as Object[],
                          "Another user has updated this EntityConfiguration while you were editing")
                render(view: "edit", model: [entityConfigurationInstance: entityConfigurationInstance])
                return
            }
        }

        entityConfigurationInstance.properties = params

        if (!entityConfigurationInstance.save(flush: true)) {
            render(view: "edit", model: [entityConfigurationInstance: entityConfigurationInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), entityConfigurationInstance.id])
        redirect(action: "show", id: entityConfigurationInstance.id)
    }

    def delete() {
        def entityConfigurationInstance = EntityConfiguration.get(params.id)
        if (!entityConfigurationInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), params.id])
            redirect(action: "list")
            return
        }

        try {
            entityConfigurationInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'entityConfiguration.label', default: 'EntityConfiguration'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
