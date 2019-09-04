package me.artspb.idea.pitest.plugin.configuration.junit;

import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;

import static me.artspb.idea.pitest.plugin.Constants.ANY_IN_PACKAGE_PATTERN;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
class DirectoryBasedConfigurator extends Configurator {

    @Override
    public void configure(final JUnitConfiguration jUnitConfiguration, PitestConfiguration configuration) {
        JUnitConfiguration.Data data = jUnitConfiguration.getPersistentData();
        String targetTests = "";
        String targetClasses = "";
        String dirName = data.getDirName();
        final VirtualFile dir = LocalFileSystem.getInstance().findFileByPath(FileUtil.toSystemIndependentName(dirName));
        if (dir != null) {
            PsiDirectory directory = ApplicationManager.getApplication().runReadAction(new Computable<PsiDirectory>() {
                @Override
                public PsiDirectory compute() {
                    return PsiManager.getInstance(jUnitConfiguration.getProject()).findDirectory(dir);
                }
            });
            if (directory != null) {
                PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(directory);
                if (psiPackage != null) {
                    String packageName = psiPackage.getQualifiedName();
                    targetTests = isEmpty(packageName) ? "" : packageName + ANY_IN_PACKAGE_PATTERN;
                    targetClasses = targetTests;
                }
            }
        }
        configure(configuration, targetTests, targetClasses);
    }
}
