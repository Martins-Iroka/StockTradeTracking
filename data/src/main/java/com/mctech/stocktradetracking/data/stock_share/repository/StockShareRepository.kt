package com.mctech.stocktradetracking.data.stock_share.repository

import com.mctech.library.logger.Logger
import com.mctech.stocktradetracking.data.stock_share.datasource.LocalStockShareDataSource
import com.mctech.stocktradetracking.data.stock_share.datasource.RemoteStockShareDataSource
import com.mctech.stocktradetracking.domain.stock_share.entity.StockShare
import com.mctech.stocktradetracking.domain.stock_share.service.StockShareService
import kotlinx.coroutines.*

class StockShareRepository(
    private val logger: Logger,
    private val localDataSource: LocalStockShareDataSource,
    private val remoteDataSource: RemoteStockShareDataSource
) : StockShareService {

    private var syncPrinceScope : CoroutineScope? = null
    private var syncPricesDefer  = mutableListOf<Deferred<Unit>>()

    override suspend fun observeStockShareList() = localDataSource.observeStockShareList()

    override suspend fun observeStockClosedList() = localDataSource.observeStockClosedList()

    override suspend fun saveStockShare(share: StockShare) = localDataSource.saveStockShare(share)

    override suspend fun sellStockShare(share: StockShare) = localDataSource.sellStockShare(share)

    override suspend fun deleteStockShare(share: StockShare) = localDataSource.deleteStockShare(share)

    override suspend fun closeStockShare(share: StockShare) = localDataSource.closeStockShare(share)

    override suspend fun getMarketStatus() = localDataSource.getMarketStatus()

    override suspend fun editStockShareValue(shareCode: String, value: Double) =
        localDataSource.editStockShareValue(
            shareCode,
            value
        )

    override suspend fun syncStockSharePrice() {
        syncPrinceScope?.cancel()
        syncPrinceScope = GlobalScope.plus(Job())
        syncPricesDefer.clear()

        val stockShare = localDataSource.getDistinctStockCode()

        for(stockCode in stockShare) syncPrinceScope?.async {
            try{
                val currentPrice = remoteDataSource.getCurrentStockSharePrice(stockCode)

                if(currentPrice.price != null){
                    localDataSource.editStockShareValue(
                        stockCode,
                        currentPrice.price,
                        currentPrice.marketChange,
                        currentPrice.previousClose
                    )
                }
            }catch (ex : Exception){
                logger.e(e = ex)
            }
        }?.run {
            syncPricesDefer.add(this)
        }

        syncPricesDefer.awaitAll()
    }
}
