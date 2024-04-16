package com.dev_bayan_ibrahim.flashcards.data.util

import com.google.common.math.IntMath.pow
import java.text.DecimalFormat
import kotlin.math.absoluteValue

/*
E0 -
E3  thousand
E6  million
E9  billion
E12	trillion
E15	quadrillion
E18	quintillion
E21	sextillion
E24	septillion
E27	octillion
E30	nonillion
E33	decillion
E36	undecillion
E39	duodecillion
E42	tredecillion
E45	quattuordecillion
 */
private val groups = listOf(
    "", "K", "M", "B", "T", "Qd"
)

fun Long.asFormattedString(
    groupSuffix: (Int) -> String = { groups[it] },
): String {
    //             1 -> 1
    //            10 -> 10
    //           100 -> 0.1k
    //         1 000 -> 1k
    //        10 000 -> 10k
    //       100 000 -> 0.1m
    //     1 000 000 -> 1m
    //    10 000 000 -> 10m
    //   100 000 000 -> 0.1b
    // 1 000 100 000 -> 1b
    // 610
    val len = absoluteValue.toString().length
    val negative = this < 0
    val groups = len / 3
    val sfx = groupSuffix(groups)

    val power = (groups.times(3)).coerceAtLeast(0)
    val factor = pow(10, power).toFloat()

    return buildString {
        if (negative) {
            append("-")
        }
        append(DecimalFormat("0.#").format(this@asFormattedString / factor))
        append(sfx)
    }
}