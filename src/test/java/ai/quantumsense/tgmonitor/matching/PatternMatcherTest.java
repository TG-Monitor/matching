package ai.quantumsense.tgmonitor.matching;

import ai.quantumsense.tgmonitor.backend.Interactor;
import ai.quantumsense.tgmonitor.entities.Patterns;
import ai.quantumsense.tgmonitor.servicelocator.ServiceLocator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PatternMatcherTest {

    private static PatternMatcherImpl matcher;

    @BeforeClass
    public static void createPatternMatcher() {
        ServiceLocator<Interactor> interactorLocator = new ServiceLocator<Interactor>() {
            @Override
            public void registerService(Interactor interactor) {}
            @Override
            public Interactor getService() {
                return null;
            }
        };
        ServiceLocator<Patterns> patternsLocator = new ServiceLocator<Patterns>() {
            @Override
            public void registerService(Patterns patterns) {}
            @Override
            public Patterns getService() {
                return null;
            }
        };
        matcher = new PatternMatcherImpl(interactorLocator, patternsLocator);
    }

    @Test
    public void matchesWords() {
        String text = "foo and bar";
        Set<String> patterns = set("and", "other", "pattern");
        Set<String> expected = set("and");
        Assert.assertEquals(expected, matcher.match(text, patterns));
    }

    @Test
    public void isCaseInsensitiveWay1() {
        String text = "foo and bar";
        Set<String> patterns = set("AND", "OTHER", "PATTERN");
        Set<String> expected = set("AND");
        Assert.assertEquals(expected, matcher.match(text, patterns));
    }

    @Test
    public void isCaseInsensitiveWay2() {
        String text = "FOO AND BAR";
        Set<String> patterns = set("And", "Other", "Pattern");
        Set<String> expected = set("And");
        Assert.assertEquals(expected, matcher.match(text, patterns));
    }

    @Test
    public void doesNotMatchSubwords() {
        String text = "foo standard bar";
        Set<String> patterns = set("and", "other", "pattern");
        Assert.assertEquals(Collections.EMPTY_SET, matcher.match(text, patterns));
    }

    @Test
    public void matchesSequenceOfWords() {
        String text = "So it is like that?";
        Set<String> patterns = set("it is", "other", "pattern");
        Set<String> expected = set("it is");
        Assert.assertEquals(expected, matcher.match(text, patterns));
    }

    @Test
    public void doesNotMatchSequenceOfSubwords() {
        String text = "Hit island";
        Set<String> patterns = set("it is", "other", "pattern");
        Assert.assertEquals(Collections.EMPTY_SET, matcher.match(text, patterns));
    }

    @Test
    public void canHandleNestedPatterns() {
        String text = "So it is like that?";
        Set<String> patterns = set("like that?", "like", "that", "like that", "other");
        Set<String> expected = set("like that?", "like", "that", "like that");
        Assert.assertEquals(expected, matcher.match(text, patterns));
    }

    private Set<String> set(String... s) {
        return new HashSet<>(Arrays.asList(s));
    }
}
