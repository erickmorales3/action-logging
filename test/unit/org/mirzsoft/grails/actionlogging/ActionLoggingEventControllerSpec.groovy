package org.mirzsoft.grails.actionlogging



import grails.test.mixin.*
import spock.lang.*

@TestFor(ActionLoggingEventController)
@Mock(ActionLoggingEvent)
class ActionLoggingEventControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.actionLoggingEventList
            model.actionLoggingEventCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.actionLoggingEvent!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def actionLoggingEvent = new ActionLoggingEvent()
            actionLoggingEvent.validate()
            controller.save(actionLoggingEvent)

        then:"The create view is rendered again with the correct model"
            model.actionLoggingEvent!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            actionLoggingEvent = new ActionLoggingEvent(params)

            controller.save(actionLoggingEvent)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/actionLoggingEvent/show/1'
            controller.flash.message != null
            ActionLoggingEvent.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def actionLoggingEvent = new ActionLoggingEvent(params)
            controller.show(actionLoggingEvent)

        then:"A model is populated containing the domain instance"
            model.actionLoggingEvent == actionLoggingEvent
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def actionLoggingEvent = new ActionLoggingEvent(params)
            controller.edit(actionLoggingEvent)

        then:"A model is populated containing the domain instance"
            model.actionLoggingEvent == actionLoggingEvent
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/actionLoggingEvent/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def actionLoggingEvent = new ActionLoggingEvent()
            actionLoggingEvent.validate()
            controller.update(actionLoggingEvent)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.actionLoggingEvent == actionLoggingEvent

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            actionLoggingEvent = new ActionLoggingEvent(params).save(flush: true)
            controller.update(actionLoggingEvent)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/actionLoggingEvent/show/$actionLoggingEvent.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/actionLoggingEvent/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def actionLoggingEvent = new ActionLoggingEvent(params).save(flush: true)

        then:"It exists"
            ActionLoggingEvent.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(actionLoggingEvent)

        then:"The instance is deleted"
            ActionLoggingEvent.count() == 0
            response.redirectedUrl == '/actionLoggingEvent/index'
            flash.message != null
    }
}
