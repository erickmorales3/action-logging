# Action Logging - Grails Plugin

A simple Grails Plugin to save a controller actions log in database, including controller name, action name, start-end time, total time (seconds), exceptions, custom messages and more.

## Description

It's specially useful to developers and system administrators, because saves custom messages, exceptions and execution time. This allows to know application weaknesses to correct it before user report. In addition, it allows to identify the current user to create  a user action history.

For further information see [https://erickmorales3.github.io/action-logging/](https://erickmorales3.github.io/action-logging/)

## Grails Version

2.* >

## Install

Repository:

```java
mavenRepo "http://dl.bintray.com/erickmorales3/maven"
```

Dependency:

```java
compile ":action-logging:1.0.0"
```

## How to use

Import some annotation classes on the controller where you want to enable Action Logging.

```java
import org.mirzsoft.grails.actionlogging.annotation.*
```

Add @ActionLogging annotation

```java
@ActionLogging
class SampleController {

}
```

This way, will store a log of all existing actions in controller and the result can be seen navigating to http://your/app/path/actionLoggingEvent/index

![alt tag](https://github.com/erickmorales3/action-logging/blob/master/actionLoggingEventList.png)

## Some examples

#### Enable Action Logging to all controller actions except to specific one

```java
@ActionLogging
class SampleController {
    
    def index(){
        
    }
    
    @ActionLogging(false)
    def methodWithoutActionLogging(){
        
    }
}
```

#### Enable Action Logging to specific controller action

```java

class SampleController {
    
    def index(){
        
    }
    
    @ActionLogging
    def methodWithActionLogging(){
        
    }
}
```

#### Adding custom log messages

Inject actionLoggingService class:

```java
@ActionLogging
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.log("A custom log message 1")
        actionLoggingService.log("A custom log message 2")
    }
}
```

#### Printing custom log messages like println function

By default custom messages are only visible in Action Logging Event List screen or in action_loging database table, but is possible enable pirntln function adding the @PrintCustomLog annotation:

- To all controller actions

```java
@ActionLogging
@PrintCustomLog
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.log("A custom log message 1 - printed")
        actionLoggingService.log("A custom log message 2 - printed")
    }
}
```

- To all controller actions except to specific one

```java
@ActionLogging
@PrintCustomLog
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.log("A custom log message 1 - printed")
        actionLoggingService.log("A custom log message 2 - printed")
    }
    
    @PrintCustomLog(false)
    def methodWithoutCustomPrint(){
        actionLoggingService.log("A custom log message 3")
        actionLoggingService.log("A custom log message 4")
    }
}
```

- To specific controller action

```java
@ActionLogging
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.log("A custom log message 1")
        actionLoggingService.log("A custom log message 2")
    }
    
    @PrintCustomLog
    def methodWithCustomPrint(){
        actionLoggingService.log("A custom log message 3 - printed")
        actionLoggingService.log("A custom log message 4 - printed")
    }
}
```

#### Setting action type to all controller actions

```java
@ActionLogging
@ActionType("Administrator/Supervisor Actions")
class SampleController {
    
}
```

#### Identifying current user with Spring Security Core Plugin

```java
@ActionLogging
@SpringUserIdentification
class SampleController {
    
}
```

#### Identifying current user without Spring Security Core Plugin

```java
@ActionLogging
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.setUserId(3)
    }
}
```

#### Setting custom action name

- Using @CustomActionName annotation

```java
@ActionLogging
class SampleController {
    @CustomActionName("Custom action name")
    def index(){
        
    }
}
```

- Using actionLoggingService class

```java
@ActionLogging
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.setCustomActionName("Custom action name")
    }
}
```

#### Setting action type to specific action

- Using @ActionType annotation

```java
@ActionLogging
class SampleController {
    @ActionType("Administrator Action")
    def index(){
        
    }
}
```

- Using actionLoggingService class

```java
@ActionLogging
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.setActionType("Administrator Action")
    }
}
```

It overrides action type defined in class declaration.

#### Setting handled exception in try catch block

The handled exception in try catch block are omited, but is posible setting an exception object manually, as follows:

```java
@ActionLogging
class SampleController {
    def actionLoggingService
    
    def index(){
        try {
            def a = 1 / 0
        } catch (ex) {
            actionLoggingService.setCustomException(ex)
        }
    }
}
```


License
----

 MIT License

### Thanks

I hope it helps you
