package com.example.zhong.keeps

/**
 * Created by jacktroy on 17-12-24.
 */
class KnowledgePoint(
        var name: String,
        var markdownContent: String,
        var parentKP: KnowledgePoint?,
        var childKPList: MutableList<KnowledgePoint>?
)