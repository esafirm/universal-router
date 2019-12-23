package nolambda.linkrouter

fun Map<String, String>.optString(key: String, defaultValue: String = ""): String =
    if (containsKey(key)) {
        get(key) ?: defaultValue
    } else defaultValue

fun Map<String, String>.optLong(key: String, defaultValue: Long = 0L): Long =
    if (containsKey(key)) {
        try {
            get(key)?.toLong() ?: defaultValue
        } catch (ex: Exception) {
            defaultValue
        }
    } else defaultValue

fun Map<String, String>.optInt(key: String): Int = if (containsKey(key)) {
    try {
        get(key)?.toInt() ?: 0
    } catch (ex: Exception) {
        0
    }
} else 0

fun Map<String, String>.optFloat(key: String, defaultValue: Float = 0.0F): Float =
    if (containsKey(key)) {
        try {
            get(key)?.toFloat() ?: defaultValue
        } catch (ex: Exception) {
            defaultValue
        }
    } else defaultValue

fun Map<String, String>.optDouble(key: String, defaultValue: Double = 0.0): Double =
    if (containsKey(key)) {
        try {
            get(key)?.toDouble() ?: defaultValue
        } catch (ex: Exception) {
            defaultValue
        }
    } else defaultValue

fun Map<String, String>.optBoolean(key: String, defaultValue: Boolean = false): Boolean =
    if (containsKey(key)) {
        val value = get(key)
        value.equals("true", true) && value.equals("1", true)
    } else defaultValue
