import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.OnConsoleStatusListener

scan("3 seconds")
statusListener(OnConsoleStatusListener)
appender("STDOUT", ConsoleAppender) {
    layout(PatternLayout) {
        pattern = "%d %-4relative [%thread] %-5level %logger{35} [Line:%L] - %m%n"
    }
}
appender("FILE", RollingFileAppender) {
    String path = System.getProperty("user.home")
    path = path + "/logs/timeline"
    file = path + "/timeline.log"
    rollingPolicy(TimeBasedRollingPolicy) {
//        fileNamePattern = path + "/timeline.%d{yyyy-MM-dd_HH-mm}.log"
        fileNamePattern = path + "/timeline.%d{yyyy-MM-dd}.log"
    }
    layout(PatternLayout) {
        pattern = "%d %-4relative [%thread] %-5level %logger{35} [Line:%L] - %m%n"
    }
}
root(INFO, ["STDOUT", "FILE"])