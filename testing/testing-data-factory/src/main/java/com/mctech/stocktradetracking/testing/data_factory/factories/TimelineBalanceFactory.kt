package com.mctech.stocktradetracking.testing.data_factory.factories

import com.mctech.stocktradetracking.domain.timeline_balance.entity.TimelineBalance
import java.util.*

object TimelineBalanceFactory {
    fun listOf(count: Int = 0): List<TimelineBalance> {
        val list = mutableListOf<TimelineBalance>()
        for (x in 0 until count) {
            list.add(single())
        }
        return list
    }

    fun single(
        id: Long = 0,
        periodTag: String = "",
        parentPeriodId: Long? = null,
        startDate: Date = Calendar.getInstance().time,
        periodInvestment: Double = 0.0,
        periodProfit: Double = 0.0
    ) = TimelineBalance(
        id = id,
        periodTag = periodTag,
        parentPeriodId = parentPeriodId,
        startDate = startDate,
        periodInvestment = periodInvestment,
        periodProfit = periodProfit
    )

    fun linkedEntitiesWithId() : List<TimelineBalance>{
        val first = TimelineBalance(
            id = 1,
            periodTag = "First",
            periodInvestment = 10000.0,
            periodProfit = 10000.0
        )

        val second = TimelineBalance(
            id = 2,
            parentPeriodId = 1,
            parent = first,
            periodTag = "Second",
            periodInvestment = 10000.0,
            periodProfit = -2000.0
        )

        return mutableListOf(
            first,
            second
        )
    }
}