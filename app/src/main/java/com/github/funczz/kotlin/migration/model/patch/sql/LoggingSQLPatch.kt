package com.github.funczz.kotlin.migration.model.patch.sql

import com.github.funczz.kotlin.logging.LoggerFactory

@Suppress("Unused")
class LoggingSQLPatch(private val patch: SQLPatch) : AbstractSQLPatch() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun down(): String {
        return patch.down()
    }

    override fun getTag(): String {
        return patch.getTag()
    }

    override fun up(): String {
        return patch.up()
    }

    override fun migrate(
        moduleId: String,
        versionId: String,
        tags: Array<out String>,
        context: Map<String, Any>
    ) {
        logger.info {
            "migrate is called. moduleId=%s, versionId=%s, tags=%s, tag=%s, sql=`%s`".format(
                moduleId,
                versionId,
                tags.joinToString(","),
                getTag(),
                up(),
            )
        }
        super.migrate(moduleId, versionId, tags, context)
    }

    override fun rollback(
        moduleId: String,
        versionId: String,
        tags: Array<out String>,
        context: Map<String, Any>
    ) {
        logger.info {
            "rollback is called. moduleId=%s, versionId=%s, tags=%s, tag=%s, sql=`%s`".format(
                moduleId,
                versionId,
                tags.joinToString(","),
                getTag(),
                down(),
            )
        }
        super.rollback(moduleId, versionId, tags, context)
    }

}
