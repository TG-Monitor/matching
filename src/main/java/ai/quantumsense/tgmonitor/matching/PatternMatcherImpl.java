package ai.quantumsense.tgmonitor.matching;

import ai.quantumsense.tgmonitor.backend.Interactor;
import ai.quantumsense.tgmonitor.backend.PatternMatcher;
import ai.quantumsense.tgmonitor.backend.pojo.PatternMatch;
import ai.quantumsense.tgmonitor.backend.pojo.TelegramMessage;
import ai.quantumsense.tgmonitor.entities.Patterns;
import ai.quantumsense.tgmonitor.servicelocator.ServiceLocator;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcherImpl implements PatternMatcher {

    private ServiceLocator<Interactor> interactorLocator;
    private ServiceLocator<Patterns> patternsLocator;

    public PatternMatcherImpl(ServiceLocator<Interactor> interactorLocator, ServiceLocator<Patterns> patternsLocator) {
        this.interactorLocator = interactorLocator;
        this.patternsLocator = patternsLocator;
    }

    @Override
    public void newMessage(TelegramMessage msg) {
        Set<String> matches = match(msg.getText(), patternsLocator.getService().getPatterns());
        if (matches.size() > 0)
            interactorLocator.getService().matchFound(new PatternMatch(msg, matches));
    }

    /**
     * Match a string against each of a set of patterns.
     *
     * An individual pattern matches the string if this pattern occurs in the
     * string with "word boundaries" around it. A word boundary can roughly be
     * the beginning of a string, the end of a string, a whitespace character,
     * or a punctuation character.
     *
     * That is, if the pattern is a single word, then a match occurs if the
     * string contains exactly this word, but not if this word is a substring
     * of another word. The same applies if the pattern is a sequence of words.
     *
     * Furthermore, the matching is case-insensitive.
     *
     * Examples:
     *
     * Pattern    String                   Match
     * "and"      "foo and bar"            yes
     * "and"      "Standard"               no
     * "and"      "Foo And Bar"            yes
     * "AND"      "foo and bar"            yes
     * "it is"    "So it is like that?"    yes
     * "it is"    "Hit island"             no
     *
     * @return The subset of 'patterns' that matches the string.
     */
    Set<String> match(String str, Set<String> patterns) {
        Set<String> matches = new HashSet<>();
        for (String p : patterns) {
            Matcher m = Pattern.compile("\\b" + p + "\\b", Pattern.CASE_INSENSITIVE).matcher(str);
            if (m.find())
                matches.add(p);
        }
        return matches;
    }
}