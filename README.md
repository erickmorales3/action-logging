# Action Logging - Grails Plugin

A simple Grails Plugin to store log of controller actions in database including controller name, action name, start-end time, total time (seconds), exceptions, custom messages and more.

## Descripción

It's specially useful to developers and system administrators, because it stores custom messages, exceptions occurred during runtime including StackTrace messages and execution total time. This allows to know application weaknesses and do  corrections before user report. In addition offers a tool to assist in aplication security because it allows store the user id resposible of action execution.

## Grails Version

2.* >

## Install

Maven Repository:

```java
"http://dl.bintray.com/erickmorales3/maven"
```

Dependency:

```java
compile ":action-logging:1.0.0"
```

## How to use

Firstly you need to import some annotation classes on the controller where you want to enable the Action Logging.

```java
import org.mirzsoft.grails.actionlogging.annotation.*
```

Subsequently, you need to add @ActionLogging annotation to enables the Action Logging, as follows:

```java
@ActionLogging
class SampleController {

}
```

This way, will store a log of all existing actions in controller and the result can be seen nivigating to actionLoggingEvent controller.

![alt tag](https://github.com/erickmorales3/action-logging/blob/master/actionLoggingEventList.png)

## Some examples

#### Adding custom log messages

You need to inject actionLoggingService service:

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

By default custom messages are only visible in actionLoggingEvent screen or in action_loging database table, but is possible enable pirntln function adding the @PrintCustomLog annotation:

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

#### Setting action type to all controller action to identification

```java
@ActionLogging
@ActionType("Administrator/Supervisor Actions")
class SampleController {
    
}
```

#### Identifying responsible user with Spring Security Core Plugin

```java
@ActionLogging
@SpringUserIdentification
class SampleController {
    
}
```

#### Identifying responsible user without Spring Security Core Plugin

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

- Using actionLoggingService service

```java
@ActionLogging
class SampleController {
    def actionLoggingService
    
    def index(){
        actionLoggingService.setCustomActionName("Custom action name")
    }
}
```

#### Setting action type to specific action to identification

- Using @ActionType annotation

```java
@ActionLogging
class SampleController {
    @ActionType("Administrator Action")
    def index(){
        
    }
}
```

- Using actionLoggingService service

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

The handled exception in try catch block, are omited, but is posible setting an exception object manually, as follows:

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
