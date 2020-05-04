package com.mctech.stocktradetracking.di

import com.mctech.stocktradetracking.domain.stock_share.interaction.*
import com.mctech.stocktradetracking.domain.stock_share.interaction.strategies.ComputeBalanceStrategy
import com.mctech.stocktradetracking.domain.stock_share.interaction.strategies.ObserveStockListStrategy
import com.mctech.stocktradetracking.domain.stock_share.interaction.strategies.SelectStockStrategy
import com.mctech.stocktradetracking.domain.timeline_balance.interaction.CreatePeriodCase
import com.mctech.stocktradetracking.domain.timeline_balance.interaction.EditPeriodCase
import com.mctech.stocktradetracking.domain.timeline_balance.interaction.GetCurrentPeriodBalanceCase
import org.koin.core.qualifier.named
import org.koin.dsl.module


val useCasesModule = module {
    // Stock Share
    single { SaveStockShareCase(service = get(), logger = get()) }
    single { DeleteStockShareCase(service = get(), logger = get()) }
    single { CloseStockShareCase(service = get(), logger = get()) }
    single { EditStockSharePriceCase(service = get(), logger = get()) }
    single { SellStockShareCase(service = get(), logger = get()) }
    single { SyncStockSharePriceCase(service = get(), logger = get()) }
    single { GetMarketStatusCase(service = get(), logger = get()) }
    single { GetFinalBalanceCase() }
    single { GroupStockShareListCase() }

    single { ObserveStockShareListCase(service = get()) as ObserveStockListStrategy }
    single(named("closedListObserver")) { ObserveStockClosedListCase(service = get()) as ObserveStockListStrategy}

    single(named("dailyWorstSelector")) { SelectWorstDailyStockShareCase(groupStockShareListCase = get()) as SelectStockStrategy }
    single(named("dailyBestSelector")) { SelectBestDailyStockShareCase(groupStockShareListCase = get()) as SelectStockStrategy }
    single(named("stockWorstSelector")) { SelectWorstStockShareCase(groupStockShareListCase = get()) as SelectStockStrategy }
    single(named("stockBestSelector")) { SelectBestStockShareCase(groupStockShareListCase = get()) as SelectStockStrategy }

    single(named("stockBalance")) { GetFinalBalanceCase() as ComputeBalanceStrategy }
    single(named("dailyBalance")) { GetFinalDailyBalanceCase() as ComputeBalanceStrategy }

    // Timeline balance
    single { CreatePeriodCase(service = get(), logger = get()) }
    single { EditPeriodCase(service = get(), logger = get()) }
    single { GetCurrentPeriodBalanceCase(service = get(), logger = get()) }
}