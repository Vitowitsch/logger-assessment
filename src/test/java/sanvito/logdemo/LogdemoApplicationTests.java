package sanvito.logdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class LogdemoApplicationTests {


    private static final String MSG = "text";

    private static final List<Target> targets = new ArrayList<>(Stream.of(new Target(LogLevel.DEBUG_LEVEL_LOG, "CONSOLE"),
                    new Target(LogLevel.ERR_LEVEL_LOG, "EMAIL")).collect(Collectors.toList()));


    private static final Logger logger = new Logger(targets);

    @Test
    void testGlobalLogLevelAvailable(CapturedOutput capture) {
        LogLevel level = LogLevel.INFO_LEVEL_LOG;
        logger.log(level, MSG);
        Assertions.assertTrue(capture.getOut().contains(level + "->CONSOLE" + ":" + MSG));
    }

    @Test
    void testGlobalLogLevelAvailableEdgeCase(CapturedOutput capture) {
        LogLevel level = LogLevel.DEBUG_LEVEL_LOG;
        logger.log(level, MSG);
        Assertions.assertTrue(capture.getOut().contains(level + "->CONSOLE"  + ":" + MSG));
    }

    @Test
    void testGlobalLogLevelNotAvailable(CapturedOutput capture) {
        LogLevel level = LogLevel.DEBUG_LEVEL_LOG;
        logger.log(level, MSG);
        Assertions.assertFalse(capture.getOut().contains(level + "->EMAIL"  + ":" + MSG));
    }

    @Test
    void test2Targets(CapturedOutput capture) {
        LogLevel level = LogLevel.ERR_LEVEL_LOG;
        logger.log(level, MSG);
        Assertions.assertTrue(capture.getOut().contains(level + "->EMAIL"  + ":" + MSG));

        level = LogLevel.DEBUG_LEVEL_LOG;
        logger.log(level, MSG);
        Assertions.assertFalse(capture.getOut().contains(level + "->EMAIL"  + ":" + MSG));

        level = LogLevel.DEBUG_LEVEL_LOG;
        logger.log(level, MSG);
        Assertions.assertTrue(capture.getOut().contains(level + "->CONSOLE"  + ":" + MSG));
    }

}
