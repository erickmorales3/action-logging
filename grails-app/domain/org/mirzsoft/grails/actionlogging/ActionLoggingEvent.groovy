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

class ActionLoggingEvent implements Serializable{
    Integer id
    String controllerName
    String actionName
    String customActionName
    String customLog = ""
    String exceptionMessage
    String exceptionStackTrace
    String status
    String actionType
    Long startTime
    Long endTime
    Long totalTime
    Date date
    String forwardURI
    String remoteHost
    Boolean ajax
    String params
    Integer userId
    
    static transients = ["", ""]
    
    static constraints = {
        controllerName nullable: true, blank: true, maxSize: 250
        actionName nullable: true, blank: true, maxSize: 250
        customActionName nullable: true, blank: true, maxSize: 5000
        customLog nullable: true, blank: true, maxSize: 5000000
        exceptionMessage nullable: true, blank: true, maxSize: 5000000
        exceptionStackTrace nullable: true, blank: true, maxSize: 5000000
        status nullable: true, blank: true, maxSize: 150, inList:["SUCCESS","ERROR"]
        actionType nullable: true, blank: true, maxSize: 5000
        startTime nullable: true, blank: true
        endTime nullable: true, blank: true
        totalTime nullable: true, blank: true
        date nullable: false, blank: false
        forwardURI nullable: true, blank: true, maxSize: 5000
        remoteHost nullable: true, blank: true, maxSize: 250
        ajax nullable: true, blank: true
        params nullable: true, blank: true, maxSize: 5000
        userId nullable: true, blank: true
    }
    
    def getHtmlCustomLog(){
        return customLog?.replace("\n", "<br/>")
    }
    
    def getHtmlExceptionStackTrace(){
        return exceptionStackTrace?.replace("\n", "<br/>")
    }
    
}
