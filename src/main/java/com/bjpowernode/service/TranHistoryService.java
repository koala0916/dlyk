package com.bjpowernode.service;

import com.bjpowernode.query.TranHistoryQuery;

public interface TranHistoryService {
    boolean modifyTranStage(TranHistoryQuery tranHistoryQuery);
}
