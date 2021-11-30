package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.sql.Timestamp;

public class MessageCounter implements Processor {
    private int msgCounter, txtFileCounter, xmlFileCounter, illegalFileCounter = 0;
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

        setTimeAndSetStatistics(++msgCounter);
    }

    public void setTimeAndSetStatistics(int msgCounter) {
        if (msgCounter == 1) {
            Timestamp begin = new Timestamp(System.currentTimeMillis());
            beginProcessing = begin.getTime();
        } else if (msgCounter == 3) {
            Timestamp end = new Timestamp(System.currentTimeMillis());
            endProcessing = end.getTime();
            setStatisticsAndResetCounters();
        }
    }

    public void setStatisticsAndResetCounters() {
        message.setHeader("TotalMsgCount", msgCounter);
        message.setHeader("TxtFileCount", txtFileCounter);
        message.setHeader("XmlFileCount", xmlFileCounter);
        message.setHeader("IllegalFileCount", illegalFileCounter);
        message.setHeader("ProcessingTime", endProcessing - beginProcessing);
        message.setHeader("StatMsg", true);
        resetCounters();
    }

    public void resetCounters() {
        msgCounter = 0;
        txtFileCounter = 0;
        xmlFileCounter = 0;
        illegalFileCounter = 0;
    }
}
