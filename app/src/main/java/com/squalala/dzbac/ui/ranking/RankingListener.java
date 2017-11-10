package com.squalala.dzbac.ui.ranking;

import com.squalala.dzbac.data.api.ApiResponse;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public interface RankingListener {

    void onLoadListUsers(ApiResponse.Rankings rankings);
}
