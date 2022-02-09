package top.anagke.auto_pnc.explore

data class ExploreConfig(
    val vulnerabilityLevel: Vulnerability.Level,
    val vulnerabilityPlan: List<List<Int>>,
)
