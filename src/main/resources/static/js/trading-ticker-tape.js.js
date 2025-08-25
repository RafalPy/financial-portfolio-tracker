document.addEventListener("DOMContentLoaded", function () {
    new TradingView.widget({
        "container_id": "tradingview-widget-container",
        "symbols": [
            {
                "proName": "FOREXCOM:SPXUSD",
                "title": "S&P 500 Index"
            },
            {
                "proName": "FOREXCOM:NSXUSD",
                "title": "US 100 Cash CFD"
            },
            {
                "proName": "FX_IDC:EURUSD",
                "title": "EUR to USD"
            },
            {
                "proName": "BITSTAMP:BTCUSD",
                "title": "Bitcoin"
            },
            {
                "proName": "BITSTAMP:ETHUSD",
                "title": "Ethereum"
            }
        ],
        "colorTheme": "light",
        "locale": "en",
        "largeChartUrl": "",
        "isTransparent": false,
        "showSymbolLogo": true,
        "displayMode": "adaptive"
    });
});