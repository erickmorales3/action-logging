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

import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.ActionType
import org.mirzsoft.grails.actionlogging.annotation.CustomActionName
import org.mirzsoft.grails.actionlogging.annotation.PrintCustomLog
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification

@ActionLogging
@PrintCustomLog
@ActionType("Administrator/Supervisor Actions")
@SpringUserIdentification
class ActionLoggingSampleController {

    ActionLoggingService actionLoggingService

    @CustomActionName("Custom Action Name")
    @ActionType("Administrator Action")
    def index() {
        actionLoggingService.log("Custom log")
        actionLoggingService.log("Sample: This is the 'index' action of Test controller")

        try {
            def a = 1 / 0
        } catch (ex) {
            actionLoggingService.setCustomException(ex)
        }

        render "Action log saved."
    }

    @CustomActionName("Another Action Name")
    @ActionType("Supervisor Action")
    def other(){
        actionLoggingService.log("Sample: This is the 'other' action of Test controller")

        actionLoggingService.setCustomActionName("Override Action Name")
        actionLoggingService.setActionType("Override ActionType")
        actionLoggingService.setUserId(1992)

        render "Action log saved."
    }
}
