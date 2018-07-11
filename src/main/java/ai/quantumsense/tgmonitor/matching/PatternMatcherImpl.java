package ai.quantumsense.tgmonitor.matching;

import ai.quantumsense.tgmonitor.backend.InteractorFactory;
import ai.quantumsense.tgmonitor.backend.PatternMatcher;
import ai.quantumsense.tgmonitor.backend.pojo.PatternMatch;
import ai.quantumsense.tgmonitor.backend.pojo.TelegramMessage;
import ai.quantumsense.tgmonitor.monitor.data.MonitorDataFactory;

import java.util.HashSet;
import java.util.Set;

public class PatternMatcherImpl implements PatternMatcher {

    private InteractorFactory interactorFactory;
    private MonitorDataFactory monitorDataFactory;

    public PatternMatcherImpl(InteractorFactory interactorFactory, MonitorDataFactory monitorDataFactory) {
        this.monitorDataFactory = monitorDataFactory;
        this.interactorFactory = interactorFactory;
    }

    @Override
    public void newMessage(TelegramMessage msg) {
        String text = msg.getText();
        Set<String> matches = new HashSet<>();

        for (String p : patterns()) {
            if (text.contains(p)) matches.add(p);
        }

        if (matches.size() > 0) {
            interactorFactory.getInteractor().matchFound(new PatternMatch(msg, matches));
        }
    }

    private Set<String> patterns() {
        return monitorDataFactory.getMonitorData().getPatterns();
    }
}