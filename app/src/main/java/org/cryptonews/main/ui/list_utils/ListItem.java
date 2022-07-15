package org.cryptonews.main.ui.list_utils;

import org.cryptonews.main.network.Coin;
import org.cryptonews.main.network.Info;

import java.io.Serializable;

public class ListItem implements Serializable {

    private Coin coin;
    private Info info;

    public ListItem(Coin coin,Info info) {
        this.coin = coin;
        this.info = info;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }


}
