package on.ssgdeal.common.mdc;

import java.util.UUID;
import org.slf4j.MDC;

public class MdcContext implements AutoCloseable {

    public MdcContext() {
        MDC.clear();
        injectThreadId();
    }

    private void injectThreadId() {
        String threadId = UUID.randomUUID().toString();
        MDC.put("threadId", threadId);
    }

    @Override
    public void close() throws Exception {
        MDC.clear();
    }
}
