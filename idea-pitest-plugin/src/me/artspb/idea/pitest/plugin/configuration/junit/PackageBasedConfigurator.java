package me.artspb.idea.pitest.plugin.configuration.junit;

import com.intellij.execution.junit.JUnitConfiguration;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;

import static me.artspb.idea.pitest.plugin.Constants.ANY_IN_PACKAGE_PATTERN;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
class PackageBasedConfigurator extends Configurator {

    @Override
    public void configure(JUnitConfiguration jUnitConfiguration, PitestConfiguration configuration) {
        JUnitConfiguration.Data data = jUnitConfiguration.getPersistentData();
        String targetTests = isEmpty(data.getPackageName()) ? "" : data.getPackageName() + ANY_IN_PACKAGE_PATTERN;
        configure(configuration, targetTests, targetTests);
    }
}
