package cn.wolfcode.tx.business.service;


public interface IBusinessService{
    void purchase(String userId, String commodityCode, int orderCount, boolean rollback);
}
