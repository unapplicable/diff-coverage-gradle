package io.github.unapplicable.coverage.gradle

import io.github.unapplicable.coverage.gradle.DiffCoveragePlugin.Companion.DIFF_COV_TASK
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

class DiffCoverageMultiModuleTest : BaseDiffCoverageTest() {

    companion object {
        const val TEST_PROJECT_RESOURCE_NAME = "multi-module-test-project"
    }

    override fun buildTestConfiguration() = TestConfiguration(
        TEST_PROJECT_RESOURCE_NAME,
        "build.gradle",
        "test.diff"
    )

    @BeforeEach
    fun setup() {
        initializeGradleTest()
    }

    @Disabled
    @Test
    fun `diff-coverage should automatically collect jacoco configuration from submodules in multimodule project`() {
        // setup
        val baseReportDir = "build/custom/"
        val htmlReportDir = rootProjectDir.resolve(baseReportDir).resolve(File("diffCoverage", "html"))
        buildFile.appendText(
            """
            
            diffCoverageReport {
                diffSource.file = '$diffFilePath'
                reports {
                    html = true
                    baseReportDir = '$baseReportDir'
                }
                violationRules.failIfCoverageLessThan 0.9
            }
        """.trimIndent()
        )

        // run
        val result = gradleRunner.runTaskAndFail(DIFF_COV_TASK)

        // assert
        result.assertDiffCoverageStatusEqualsTo(FAILED)
            .assertOutputContainsStrings(
                "Fail on violations: true. Found violations: 1.",
                "Rule violated for bundle $TEST_PROJECT_RESOURCE_NAME: " +
                        "branches covered ratio is 0.5, but expected minimum is 0.9"
            )
        assertThat(htmlReportDir.list()).containsExactlyInAnyOrder(
            *expectedHtmlReportFiles("com.module1", "com.module2")
        )
    }

}
