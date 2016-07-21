/*****************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Erick M. Morales M.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *****************************************************************************/

import static org.codehaus.groovy.grails.commons.ControllerArtefactHandler.TYPE as CONTROLLER
import groovy.util.logging.Slf4j

import org.mirzsoft.grails.actionlogging.ActionLoggingEvent
import org.mirzsoft.grails.actionlogging.ActionLoggingService
import org.mirzsoft.grails.actionlogging.ActionLoggingEvent.Status
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.ActionType
import org.mirzsoft.grails.actionlogging.annotation.CustomActionName
import org.mirzsoft.grails.actionlogging.annotation.PrintCustomLog
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification

@Slf4j
class ActionLoggingFilters {

    ActionLoggingService actionLoggingService
    def springSecurityService

    def filters = {
        actionLogging(controller:'*', action:'*') {
            before = {
                long start = System.currentTimeMillis()

                if (!controllerName) {
                    return
                }

                Class controllerClazz = grailsApplication.getArtefactByLogicalPropertyName(CONTROLLER, controllerName).clazz
                if (!controllerClazz.getAnnotation(ActionLogging)?.value()) {
                    return
                }

                String actionNameValue = actionName ?: "index"
                boolean printCustomLogEnabled = controllerClazz.getAnnotation(PrintCustomLog)?.value()
                String actionType = controllerClazz.getAnnotation(ActionType)?.value()
                String customActionName
                boolean actionLoggingEnabled = true

                for (method in controllerClazz.methods) {
                    if (method.name != actionNameValue) {
                        continue
                    }

                    CustomActionName customActionNameAnnotation = method.getAnnotation(CustomActionName)
                    if (customActionNameAnnotation) {
                        customActionName = customActionNameAnnotation.value()
                    }

                    ActionType actionTypeAnnotation = method.getAnnotation(ActionType)
                    if (actionTypeAnnotation) {
                        actionType = actionTypeAnnotation.value()
                    }

                    if (!method.getAnnotation(ActionLogging)?.value()) {
                        actionLoggingEnabled = false
                    }

                    printCustomLogEnabled = method.getAnnotation(PrintCustomLog)?.value()

                    break
                }

                Long userId
                if (springSecurityService && controllerClazz.getAnnotation(SpringUserIdentification)?.value()) {
                    userId = springSecurityService.principal?.id
                }

                ActionLoggingEvent event = new ActionLoggingEvent(
                    controllerName: controllerName,
                    actionName: actionNameValue,
                    customActionName: customActionName,
                    actionType: actionType,
                    startTime: start,
                    date: new Date(),
                    forwardURI: request.forwardURI,
                    remoteHost: request.remoteHost,
                    ajax : request.xhr,
                    params: new TreeMap(params),
                    userId: userId)

                request.actionLoggingEvent = event

                actionLoggingService.save event

                // if (log.debugEnabled) {
                //     log.debug "Before handle: start ${start} '$request.forwardURI', from $request.remoteHost ($request.remoteAddr) " +
                //         " at ${new Date()}, Ajax: $request.xhr, controller: $controllerName, action: $actionName, params: ${new TreeMap(params)}"
                // }
            }

            afterView = { Exception e ->
                ActionLoggingEvent event = request.actionLoggingEvent
                if (!event) return

                long end = System.currentTimeMillis()
                event.endTime = end
                event.totalTime = end - event.startTime

                if (e) {
                    actionLoggingService.setCustomException request.exception ?: e
                }
                else if (event.status != Status.ERROR) {
                    event.status = Status.SUCCESS
                }

                actionLoggingService.save event
            }
        }
    }
}
