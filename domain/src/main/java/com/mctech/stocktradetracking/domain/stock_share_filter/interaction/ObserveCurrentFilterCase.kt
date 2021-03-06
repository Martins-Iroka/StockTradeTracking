package com.mctech.stocktradetracking.domain.stock_share_filter.interaction

import com.mctech.stocktradetracking.domain.stock_share_filter.entity.StockFilter
import com.mctech.stocktradetracking.domain.stock_share_filter.service.StockShareFilterService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCurrentFilterCase(
    private val service: StockShareFilterService
){
    fun execute() : Flow<StockFilter> {
        return service.observeStockShareFilter().map {
            it ?: StockFilter.Default()
        }
    }
}