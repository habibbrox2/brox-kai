package com.inspiredandroid.kai.tools

import com.inspiredandroid.kai.network.tools.Tool
import org.koin.java.KoinJavaComponent.inject

/**
 * Android implementation of the downloads status tool.
 * This is registered in Platform.android.kt as part of the tool set.
 */
class DownloadsStatusToolImpl : Tool {
    private val downloadsPermissionController: DownloadsPermissionController by inject(DownloadsPermissionController::class.java)

    override val schema = DownloadsStatusTool.schema

    override suspend fun execute(args: Map<String, Any>): Any {
        val accessState = downloadsPermissionController.getDownloadsAccessState()

        val sourcesInfo = accessState.sources

        return DownloadsStatusResult(
            overallCoverage = accessState.overallCoverage.displayName,
            sources = sourcesInfo,
            hasLegacyPermission = accessState.hasLegacyPermission,
            canQueryDownloadManager = downloadsPermissionController.canQueryDownloadManager(),
            canQueryMediaStore = downloadsPermissionController.canQueryMediaStoreDownloads(),
            isSafAvailable = downloadsPermissionController.isSafAvailable(),
            message = downloadsPermissionController.getAccessSummary(),
            messageBengali = downloadsPermissionController.getAccessSummaryBengali(),
            details = mapOf(
                "source_details" to accessState.sources.map { source ->
                    mapOf(
                        "name" to source.name,
                        "isAvailable" to source.isAvailable,
                        "coverage" to source.coverage.name,
                        "limitations" to source.limitations,
                    )
                },
                "coverage_explanation" to mapOf(
                    "FULL" to "All file types accessible via legacy permission + MediaStore",
                    "MEDIA_PARTIAL" to "Media files via MediaStore; non-media files not visible",
                    "PARTIAL" to "Only DownloadManager entries visible; limited coverage",
                    "MEDIA_ONLY" to "Only media files (images, videos, audio) in Downloads",
                    "USER_SELECTED" to "Files/folders explicitly selected by user via SAF picker"
                )
            )
        )
    }
}