package com.gaiaonline.monstergalaxy.reward;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.shop.Deal;
import com.gaiaonline.monstergalaxy.model.shop.DealListener;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoySpendPointsNotifier;

/* JADX INFO: loaded from: classes.dex */
public class TapjoyNotifierImpl implements TapjoyNotifier, TapjoySpendPointsNotifier {
    private DealListener listener;
    private int starseeds;

    public TapjoyNotifierImpl(DealListener listener) {
        this.listener = listener;
    }

    @Override // com.tapjoy.TapjoyNotifier
    public void getUpdatePoints(String currencyName, int points) {
        this.starseeds = points;
        if (this.starseeds > 0) {
            TapjoyConnect.getTapjoyConnectInstance().spendTapPoints(this.starseeds, this);
        }
    }

    @Override // com.tapjoy.TapjoyNotifier
    public void getUpdatePointsFailed(String error) {
    }

    @Override // com.tapjoy.TapjoySpendPointsNotifier
    public void getSpendPointsResponse(String currencyName, int points) {
        if (this.starseeds > 0) {
            if (this.listener != null) {
                this.listener.onRewardReceived(Deal.DealType.STARSEEDS);
            }
            Game.getTrainer().addStarSeeds(this.starseeds);
        }
        this.starseeds = 0;
    }

    @Override // com.tapjoy.TapjoySpendPointsNotifier
    public void getSpendPointsResponseFailed(String error) {
    }
}
