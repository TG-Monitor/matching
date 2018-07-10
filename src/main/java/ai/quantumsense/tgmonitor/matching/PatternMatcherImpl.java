package ai.quantumsense.tgmonitor.matching;

import ai.quantumsense.tgmonitor.backend.Interactor;
import ai.quantumsense.tgmonitor.backend.PatternMatcher;
import ai.quantumsense.tgmonitor.backend.pojo.PatternMatch;
import ai.quantumsense.tgmonitor.backend.pojo.TelegramMessage;
import ai.quantumsense.tgmonitor.monitor.Monitor;

import java.util.HashSet;
import java.util.Set;

public class PatternMatcherImpl implements PatternMatcher {

    private Interactor interactor;
    private Monitor monitor;

    public PatternMatcherImpl(Interactor interactor, Monitor monitor) {
        this.interactor = interactor;
        this.monitor = monitor;
    }

    @Override
    public void newMessage(TelegramMessage msg) {
        String text = msg.getText();
        Set<String> matches = new HashSet<>();

        for (String p : monitor.getPatterns()) {
            if (text.contains(p)) matches.add(p);
        }

        if (matches.size() > 0) {
            interactor.matchFound(new PatternMatch(msg, matches));
        }
    }
}