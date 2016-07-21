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

import org.mirzsoft.grails.actionlogging.ActionLoggingEvent
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.PrintCustomLog
import org.mirzsoft.grails.actionlogging.annotation.CustomActionName
import org.mirzsoft.grails.actionlogging.annotation.ActionType
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.codehaus.groovy.grails.exceptions.DefaultStackTraceFilterer

class ActionLoggingFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                session.actionLogging = null
                if (controllerName) {
                    def start = System.currentTimeMillis()
                    def actionLoggingEnabled = false
                    def printCustomLogEnabled = false
                    def customActionName
                    def actionType
                    def userId
                    def controllerNameValue = controllerName
                    def actionNameValue = actionName
                    
                    if (!actionName) {
                        actionNameValue = "index"
                    }

                    def controllerClass = grailsApplication.controllerClasses.find { it.fullName.indexOf(controllerNameValue.capitalize()+"Controller") != -1 }
     
                    if (controllerClass.getClazz().isAnnotationPresent(ActionLogging)) {
                        actionLoggingEnabled = controllerClass.getClazz().getAnnotation(ActionLogging).value()
                    }
                    
                    if (controllerClass.getClazz().isAnnotationPresent(PrintCustomLog)) {
                        printCustomLogEnabled = controllerClass.getClazz().getAnnotation(PrintCustomLog).value()
                    }
                    
                    if (controllerClass.getClazz().isAnnotationPresent(ActionType)) {
                        def actionTypeAnnotation = controllerClass.getClazz().getAnnotation(ActionType)
                        def actionTypeObject = (ActionType) actionTypeAnnotation;
                        actionType = actionTypeObject.value()
                    }
                    
                    
                    def userIdentificationAnnotated = controllerClass.getClazz().isAnnotationPresent(SpringUserIdentification)
                    if (userIdentificationAnnotated) {
                        def springSecurityService
                        try {
                            springSecurityService = applicationContext.getBean("springSecurityService")
                        } catch (Exception ex) {}

                        if (springSecurityService) {
                            userId = springSecurityService?.currentUser?.id
                        }
                    }
                    
                    def methods = controllerClass.getClazz().getMethods()
                    for(method in methods) {
                         if (method.getName() == actionNameValue) {
                            if (method.isAnnotationPresent(CustomActionName)) {
                                def customActionNameAnnotation = method.getAnnotation(CustomActionName)
                                def customActionNameObject = (CustomActionName) customActionNameAnnotation;
                                customActionName = customActionNameObject.value()
                            }
                            
                            if (method.isAnnotationPresent(ActionType)) {
                                def actionTypeAnnotation = method.getAnnotation(ActionType)
                                def actionTypeObject = (ActionType) actionTypeAnnotation;
                                actionType = actionTypeObject.value()
                            }
                            
                            if (method.isAnnotationPresent(ActionLogging)) {
                                actionLoggingEnabled = method.getAnnotation(ActionLogging).value()
                            }

                            if (method.isAnnotationPresent(PrintCustomLog)) {
                                printCustomLogEnabled = method.getAnnotation(PrintCustomLog).value()
                            }
                            break
                        }
                    }

                    def actionLoggingEvent = new ActionLoggingEvent(
                        controllerName: controllerNameValue,
                        actionName: actionNameValue,
                        customActionName: customActionName,
                        actionType: actionType,
                        startTime: start,
                        date: new Date(),
                        forwardURI: request.forwardURI,
                        remoteHost: request.remoteHost,
                        ajax : request.xhr,
                        params: new TreeMap(params),
                        userId: userId
                    )
                    
                    if (actionLoggingEnabled) {
                        if (actionLoggingEvent.save(flush: true)) {
//                            println "Saved: ${actionLoggingEvent}"
                        } else {
                            println "ActionLogging save failed."
                            println actionLoggingEvent.errors
                        }
                    }
                
                    session.actionLoggingEvent = actionLoggingEvent
                    session.actionLoggingEnabled = actionLoggingEnabled
                    session.printCustomLogEnabled = printCustomLogEnabled
                    println ""
                
                    //                println "Before handle: start ${start} '$request.forwardURI', from $request.remoteHost ($request.remoteAddr) " +
                    //               " at ${new Date()}, Ajax: $request.xhr, controller: $controllerName, action: $actionName, params: ${new TreeMap(params)}"
                }
            }
            
            after = { Map model ->
                
            }
            
            afterView = { Exception e ->
                def actionLoggingEvent = session?.actionLoggingEvent
                def actionLoggingEnabled = session?.actionLoggingEnabled
                
                if (actionLoggingEvent){
                    def end = System.currentTimeMillis()
                
                    actionLoggingEvent?.endTime = end
                    actionLoggingEvent?.totalTime = end - actionLoggingEvent?.startTime
                
                    session?.actionLoggingEvent = actionLoggingEvent
                
                    if (e) {
                        
                        def wrappedException = request.exception 
                        def sw = new StringWriter()
                        def pw = new PrintWriter(sw, true)
                        
                        def dstf = new DefaultStackTraceFilterer()
                        
                        def exFiltered = dstf.filter(wrappedException,true)
                        
                        exFiltered.printStackTrace(pw)
                        
                        def stacktrace = sw.getBuffer().toString()
                        
                        actionLoggingEvent?.exceptionMessage = exFiltered.message
                        actionLoggingEvent?.exceptionStackTrace = stacktrace
                        actionLoggingEvent?.status = "ERROR"
                        
                    } else {
                        if (!actionLoggingEvent?.status) {
                            actionLoggingEvent?.status = "SUCCESS"
                        }
                    }
                    
                    if (actionLoggingEnabled) {
                        if (actionLoggingEvent?.save(flush: true)) {
//                            println "Saved: ${actionLoggingEvent}"
                        } else {
                            println "ActionLogging save failed."
                            println actionLoggingEvent?.errors
                        }
                    }
                                
                    session?.actionLoggingEvent = actionLoggingEvent
                }
            }
        }
        
      
    }
    
}

