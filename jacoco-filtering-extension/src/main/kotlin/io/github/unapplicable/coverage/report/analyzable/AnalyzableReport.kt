package io.github.unapplicable.coverage.report.analyzable

import io.github.unapplicable.coverage.config.DiffCoverageConfig
import io.github.unapplicable.coverage.diff.DiffSource
import io.github.unapplicable.coverage.report.DiffReport
import io.github.unapplicable.coverage.report.reportFactory
import org.jacoco.core.analysis.Analyzer
import org.jacoco.core.analysis.ICoverageVisitor
import org.jacoco.core.data.ExecutionDataStore
import org.jacoco.report.IReportVisitor

internal interface AnalyzableReport {

    fun buildVisitor(): IReportVisitor
    fun buildAnalyzer(executionDataStore: ExecutionDataStore, coverageVisitor: ICoverageVisitor): Analyzer
}

internal fun analyzableReportFactory(
    diffCoverageConfig: DiffCoverageConfig,
    diffSource: DiffSource
): Set<AnalyzableReport> {
    return reportFactory(diffCoverageConfig, diffSource)
        .map { reportMode ->
            when (reportMode) {
                is DiffReport -> DiffCoverageAnalyzableReport(reportMode)
                else -> FullCoverageAnalyzableReport(reportMode)
            }
        }.toSet()
}


