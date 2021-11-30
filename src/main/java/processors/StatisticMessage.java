package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultMessage;


public class StatisticMessage implements Processor {
    String processingTime, txtFileCount, xmlFileCount, illegalFileCount, totalMsgCount;

    @Override
    public void process(Exchange exchange) throws Exception {
        Message inputMsg = exchange.getIn();
        DefaultMessage outputMsg = new DefaultMessage(exchange);
        getStatisticCounters(inputMsg);
        exchange.setIn(setStatisticsToMessage(outputMsg));
    }

    public void getStatisticCounters(Message inputMsg) {
        processingTime = inputMsg.getHeader("ProcessingTime").toString();
        txtFileCount = inputMsg.getHeader("TxtFileCount").toString();
        xmlFileCount = inputMsg.getHeader("XmlFileCount").toString();
        illegalFileCount = inputMsg.getHeader("IllegalFileCount").toString();
        totalMsgCount = inputMsg.getHeader("TotalMsgCount").toString();
    }

    public Message setStatisticsToMessage(Message outputMsg){
        StringBuilder statistics = new StringBuilder();
        statistics.append("Number of all files: ").append(totalMsgCount).append(System.lineSeparator());
        statistics.append("Processing time (ms): ").append(processingTime).append(System.lineSeparator());
        statistics.append("Number of text files: ").append(txtFileCount).append(System.lineSeparator());
        statistics.append("Number of xml files: ").append(xmlFileCount).append(System.lineSeparator());
        statistics.append("Number of other files: ").append(illegalFileCount).append(System.lineSeparator());
        outputMsg.setBody(statistics.toString());
        return outputMsg;
    }
}
