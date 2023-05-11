package sanvito.logdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

enum LogLevel {
    DEBUG_LEVEL_LOG(3),
    INFO_LEVEL_LOG(2),
    WARN_LEVEL_LOG(1),
    ERR_LEVEL_LOG(0);
    private final int val;

    private LogLevel(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}

class Target {
    private Consumer<String> writer = System.out::println;

    private LogLevel level;
    private String type;

    public Target(LogLevel l, String t) {
        this.level = l;
        this.type = t;
    }

    public StringBuffer constructMsg(LogLevel level, String text) {
        // does not allocate new memory as + concat does
        return new StringBuffer(level.toString()).append("->").append(this.type).append(":").append(text);
    }

    public void log(LogLevel l, String text) {
        if (l.getVal() > this.level.getVal()) {
            return; //guard
        }
        StringBuffer buf = constructMsg(l, text);
        writer.accept(buf.toString());
    }
}


public class Logger {
    private List<Target> targets = new ArrayList<>();

    public Logger(List<Target> t) {
        this.targets = t;
    }

    public void log(LogLevel level, final String text) {
        targets.forEach(t -> t.log(level, text));
    }

}
