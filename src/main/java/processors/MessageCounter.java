package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.sql.Timestamp;

public class MessageCounter implements Processor {
    private int messageCounter, txtFileCounter, xmlFileCounter, illegalFileCounter = 0;
    private long beginProcessing, endProcessing = 0;
    private Message message;

    public void process(Exchange exchange) {
        message = exchange.getIn();
        String fileName = message.getHeader("CamelFileName").toString();

        if (fileName.endsWith(".txt")) {
            txtFileCounter++;
        } else if (fileName.endsWith(".xml")) {
            xmlFileCounter++;
        } else {
            illegalFileCounter++;
        }

        setProcessingTime(++messageCounter);
    }

    public void setProcessingTime(int msgCounter) {
        if (msgCounter == 1) {
            Timestamp begin = new Timestamp(System.currentTimeMillis());
            beginProcessing = begin.getTime();
        } else if (msgCounter == 100) {
            Timestamp end = new Timestamp(System.currentTimeMillis());
            endProcessing = end.getTime();
            setStatistics();
        }
    }

    public void setStatistics() {
        message.setHeader("TotalMsgCount", messageCounter);
        message.setHeader("TxtFileCount", txtFileCounter);
        message.setHeader("XmlFileCount", xmlFileCounter);
        message.setHeader("IllegalFileCount", illegalFileCounter);
        message.setHeader("ProcessingTime", calculateProcessingTime());
        message.setHeader("StatMsg", true);
        resetCounters();
    }

    public long calculateProcessingTime() {
        return endProcessing - beginProcessing;
    }

    public void resetCounters() {
        messageCounter = 0;
        txtFileCounter = 0;
        xmlFileCounter = 0;
        illegalFileCounter = 0;
    }
}
