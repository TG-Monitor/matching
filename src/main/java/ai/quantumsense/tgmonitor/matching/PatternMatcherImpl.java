package ai.quantumsense.tgmonitor.matching;

import ai.quantumsense.tgmonitor.backend.Interactor;
import ai.quantumsense.tgmonitor.backend.PatternMatcher;
import ai.quantumsense.tgmonitor.backend.pojo.PatternMatch;
import ai.quantumsense.tgmonitor.backend.pojo.TelegramMessage;
import ai.quantumsense.tgmonitor.entities.Patterns;
import ai.quantumsense.tgmonitor.servicelocator.ServiceLocator;

import java.util.HashSet;
import java.util.Set;

public class PatternMatcherImpl implements PatternMatcher {

    private ServiceLocator<Interactor> interactorLocator;
    private ServiceLocator<Patterns> patternsLocator;

    public PatternMatcherImpl(ServiceLocator<Interactor> interactorLocator, ServiceLocator<Patterns> patternsLocator) {
        this.interactorLocator = interactorLocator;
        this.patternsLocator = patternsLocator;
    }

    @Override
    public void newMessage(TelegramMessage msg) {
        Set<String> matches = new HashSet<>();
        for (String p : patternsLocator.getService().getPatterns()) {
            if (msg.getText().contains(p)) matches.add(p);
        }
        if (matches.size() > 0) {
            interactorLocator.getService().matchFound(new PatternMatch(msg, matches));
        }
    }
}