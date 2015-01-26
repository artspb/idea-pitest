package me.artspb.idea.pitest.plugin.configuration.junit;

import com.intellij.execution.junit.JUnitConfiguration;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import org.jetbrains.annotations.NotNull;

import static me.artspb.idea.pitest.plugin.Constants.ANY_CLASS_PATTERN;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
abstract class Configurator {

    abstract void configure(JUnitConfiguration jUnitConfiguration, PitestConfiguration configuration);

    protected void configure(PitestConfiguration configuration, String targetTests, String targetClasses) {
        configuration.setTargetTests(replaceIfEmpty(targetTests));
        configuration.setTargetClasses(replaceIfEmpty(targetClasses));
    }

    @NotNull
    protected String replaceIfEmpty(String target) {
        return isEmpty(target) ? ANY_CLASS_PATTERN : target;
    }
}
