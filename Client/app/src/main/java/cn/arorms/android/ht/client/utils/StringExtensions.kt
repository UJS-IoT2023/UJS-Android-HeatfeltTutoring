package cn.arorms.android.ht.client.utils

/**
 * Converts gender enum string to Chinese.
 */
fun String?.toChineseGender(): String {
    return when (this?.uppercase()) {
        "MALE" -> "男"
        "FEMALE" -> "女"
        "男", "女" -> this // already Chinese
        else -> this ?: "未填写"
    }
}

/**
 * Converts subject enum string to Chinese.
 */
fun String?.toChineseSubject(): String {
    return when (this?.uppercase()) {
        "CHINESE" -> "语文"
        "MATH" -> "数学"
        "ENGLISH" -> "英语"
        "CHEMISTRY" -> "化学"
        "PHYSICS" -> "物理"
        "HISTORY" -> "历史"
        "COMPUTER_SCIENCE" -> "计算机科学"
        else -> this ?: "未填写"
    }
}
