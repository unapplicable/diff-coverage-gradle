package io.github.unapplicable.coverage.gradle

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.io.TempDir
import java.io.File

abstract class BaseDiffCoverageTest {

    @TempDir
    lateinit var tempTestDir: File

    lateinit var rootProjectDir: File
    lateinit var buildFile: File
    lateinit var diffFilePath: String
    lateinit var gradleRunner: GradleRunner

    /**
     * should be invoked in @Before test class method
     */
    fun initializeGradleTest() {
        val configuration: TestConfiguration = buildTestConfiguration()

        rootProjectDir = tempTestDir.copyDirFromResources<BaseDiffCoverageTest>(configuration.resourceTestProject)
        diffFilePath = rootProjectDir.resolve(configuration.diffFilePath).toUnixAbsolutePath()
        buildFile = rootProjectDir.resolve(configuration.rootBuildFilePath)

        gradleRunner = buildGradleRunner(rootProjectDir).apply {
            runTask("test")
        }
    }

    abstract fun buildTestConfiguration(): TestConfiguration
}

class TestConfiguration(
    val resourceTestProject: String,
    val rootBuildFilePath: String,
    val diffFilePath: String,
)
