class ActionLoggingGrailsPlugin {
    // the plugin version
    def version = "1.0.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "action-logging" // Headline display name of the plugin
    def author = "Erick M. Morales M."
    def authorEmail = "erickmorales3@gmail.com"
    def description = '''\
A simple Grails Plugin to store log of controller actions in database including controller name, action name, start-end time, total time (seconds), exceptions, custom messages and more.
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/erickmorales3/action-logging"

    // Extra (optional) plugin metadata
    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "MIT"

}
