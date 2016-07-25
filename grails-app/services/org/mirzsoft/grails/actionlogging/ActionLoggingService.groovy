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

import grails.transaction.Transactional
import groovy.util.logging.Slf4j

import javax.servlet.http.HttpServletRequest

import org.codehaus.groovy.grails.exceptions.DefaultStackTraceFilterer
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.mirzsoft.grails.actionlogging.ActionLoggingEvent.Status
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.validation.FieldError
import org.springframework.web.context.request.RequestContextHolder

@Slf4j
class ActionLoggingService {

    private final DefaultStackTraceFilterer stf = new DefaultStackTraceFilterer()

    MessageSource messageSource

    void log(message) {
        ActionLoggingEvent event = getEvent()
        if (!event) return

        if (request.printCustomLogEnabled) {
            log.info message
        }

        event.customLog += message + '\n'
    }

    void setCustomActionName(String customActionName) {
        setEventProperty 'customActionName', customActionName
    }

    void setActionType(String actionType) {
        setEventProperty 'actionType', actionType
    }

    void setUserId(Long userId) {
        setEventProperty 'userId', userId
    }

    void setCustomException(Throwable exception) {
        ActionLoggingEvent event = getEvent()
        if (!event) return

        def sw = new StringWriter()
        stf.filter(exception, true).printStackTrace(new PrintWriter(sw, true))

        event.exceptionMessage = exception.message
        event.exceptionStackTrace = sw.toString()
        event.status = Status.ERROR
    }

    @Transactional
    void save(ActionLoggingEvent event) {
        if (event.save()) {
            log.debug 'Saved: {}', event
        }
        else {
            logErrors event
        }
    }

    private void logErrors(ActionLoggingEvent event) {
        if (log.warnEnabled) {
            Locale locale = LocaleContextHolder.getLocale()
            StringBuilder validationErrors = new StringBuilder('ActionLogging save failed: ')
            String delimiter = ''
            for (FieldError error in event.errors.fieldErrors) {
                validationErrors << delimiter
                delimiter = ', '
                validationErrors << messageSource.getMessage(error, locale)
            }
            log.warn validationErrors.toString()
        }
    }

    private void setEventProperty(String propertyName, propertyValue) {
        ActionLoggingEvent event = getEvent()
        if (event) {
            event[propertyName] = propertyValue
        }
    }

    private HttpServletRequest getRequest() {
        ((GrailsWebRequest) RequestContextHolder.currentRequestAttributes()).request
    }

    private ActionLoggingEvent getEvent() {
        ActionLoggingEvent event = request.getAttribute('actionLoggingEvent')
        if (event) {
            event
        }
        else {
            log.info "ActionLoggingEvent object not found."
        }
    }
}
