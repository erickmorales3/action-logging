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

package org.mirzsoft.grails.actionlogging

import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.exceptions.DefaultStackTraceFilterer

class ActionLoggingService {
    def grailsApplication
    
    def log(message) {  
        def session = RequestContextHolder.currentRequestAttributes().getSession()
        def actionLoggingEvent = session.actionLoggingEvent
        def printCustomLogEnabled = session.printCustomLogEnabled
        
        if (actionLoggingEvent) {
            if(printCustomLogEnabled){
                println message
            }
        
            actionLoggingEvent.customLog += "${message}\n"
            session.actionLoggingEvent = actionLoggingEvent    
        } else {
            println "ActionLoggingEvent object not found."
        }
    }
    
    def setCustomActionName(customActionName){
        def session = RequestContextHolder.currentRequestAttributes().getSession()
        def actionLoggingEvent = session.actionLoggingEvent
        
        if (actionLoggingEvent) {
            actionLoggingEvent.customActionName = customActionName
            session.actionLoggingEvent = actionLoggingEvent    
        } else {
            println "ActionLoggingEvent object not found."
        }
    }
    
    def setActionType(actionType){
        def session = RequestContextHolder.currentRequestAttributes().getSession()
        def actionLoggingEvent = session.actionLoggingEvent
        
        if (actionLoggingEvent) {
            actionLoggingEvent.actionType = actionType
            session.actionLoggingEvent = actionLoggingEvent    
        } else {
            println "ActionLoggingEvent object not found."
        }
    }
    
    def setUserId(userId){
        def session = RequestContextHolder.currentRequestAttributes().getSession()
        def actionLoggingEvent = session.actionLoggingEvent
        
        if (actionLoggingEvent) {
            actionLoggingEvent.userId = userId
            session.actionLoggingEvent = actionLoggingEvent    
        } else {
            println "ActionLoggingEvent object not found."
        }
    }
    
    def setCustomException(exception){
        def session = RequestContextHolder.currentRequestAttributes().getSession()
        def actionLoggingEvent = session.actionLoggingEvent
        
        if (actionLoggingEvent) {
            def wrappedException = exception 
            def sw = new StringWriter()
            def pw = new PrintWriter(sw, true)

            def dstf = new DefaultStackTraceFilterer()

            def exFiltered = dstf.filter(wrappedException,true)

            exFiltered.printStackTrace(pw)

            def stacktrace = sw.getBuffer().toString()

            actionLoggingEvent?.exceptionMessage = exFiltered.message
            actionLoggingEvent?.exceptionStackTrace = stacktrace
            actionLoggingEvent?.status = "ERROR"
            
            session.actionLoggingEvent = actionLoggingEvent 
        }
    }
    
}
