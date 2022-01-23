package org.anarres.gradle.plugin.jnaerator;

import java.util.Collections;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author shevek
 */
public class JNAeratorPluginApplyTest {

    Project project;

    @BeforeAll
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    public void testApply() {
        project.apply(Collections.singletonMap("plugin", "java"));
        // project.apply(Collections.singletonMap("plugin", "org.anarres.jnaerator"));
        project.getPlugins().apply(JNAeratorPlugin.class);
        assertTrue(project.getPlugins().hasPlugin(JNAeratorPlugin.class),"Project is missing plugin");
        Task task = project.getTasks().findByName("jnaerator");
        assertNotNull( task,"Project is missing jnaerator task");
        assertTrue(task instanceof DefaultTask,"JNAerator task is the wrong type");
        assertTrue( ((DefaultTask) task).isEnabled(),"JNAerator task should be enabled");
    }
}
