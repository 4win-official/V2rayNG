package com.v2ray.ang

object AppConfig {

    //------------------------------------------------------------------------------
    // Application identifiers
    //------------------------------------------------------------------------------
    /** Application package at runtime */
    const val ANG_PACKAGE = BuildConfig.APPLICATION_ID
    /** Log tag */
    const val TAG = BuildConfig.APPLICATION_ID

    //------------------------------------------------------------------------------
    // MMKV preference keys
    //------------------------------------------------------------------------------
    object Pref {
        const val SNIFFING_ENABLED = "pref_sniffing_enabled"
        const val ROUTE_ONLY_ENABLED = "pref_route_only_enabled"
        const val PER_APP_PROXY = "pref_per_app_proxy"
        const val PER_APP_PROXY_SET = "pref_per_app_proxy_set"
        const val BYPASS_APPS = "pref_bypass_apps"
        const val LOCAL_DNS_ENABLED = "pref_local_dns_enabled"
        const val FAKE_DNS_ENABLED = "pref_fake_dns_enabled"
        const val APPEND_HTTP_PROXY = "pref_append_http_proxy"
        const val LOCAL_DNS_PORT = "pref_local_dns_port"
        const val VPN_DNS = "pref_vpn_dns"
        const val VPN_BYPASS_LAN = "pref_vpn_bypass_lan"
        const val ROUTING_DOMAIN_STRATEGY = "pref_routing_domain_strategy"
        const val ROUTING_RULESET = "pref_routing_ruleset"
        const val MUX_ENABLED = "pref_mux_enabled"
        const val MUX_CONCURRENCY = "pref_mux_concurrency"
        const val MUX_XUDP_CONCURRENCY = "pref_mux_xudp_concurrency"
        const val MUX_XUDP_QUIC = "pref_mux_xudp_quic"
        const val FRAGMENT_ENABLED = "pref_fragment_enabled"
        const val FRAGMENT_PACKETS = "pref_fragment_packets"
        const val FRAGMENT_LENGTH = "pref_fragment_length"
        const val FRAGMENT_INTERVAL = "pref_fragment_interval"
        const val AUTO_UPDATE_SUBSCRIPTION = "pref_auto_update_subscription"
        const val UPDATE_INTERVAL = "pref_auto_update_interval"
        const val SPEED_ENABLED = "pref_speed_enabled"
        const val CONFIRM_REMOVE = "pref_confirm_remove"
        const val START_SCAN_IMMEDIATE = "pref_start_scan_immediate"
        const val DOUBLE_COLUMN_DISPLAY = "pref_double_column_display"
        const val LANGUAGE = "pref_language"
        const val UI_MODE_NIGHT = "pref_ui_mode_night"
        const val PREFER_IPV6 = "pref_prefer_ipv6" // اصلاح شده
        const val PROXY_SHARING = "pref_proxy_sharing_enabled"
        const val ALLOW_INSECURE = "pref_allow_insecure"
        const val SOCKS_PORT = "pref_socks_port"
        const val REMOTE_DNS = "pref_remote_dns"
        const val DOMESTIC_DNS = "pref_domestic_dns"
        const val DNS_HOSTS = "pref_dns_hosts"
        const val DELAY_TEST_URL = "pref_delay_test_url"
        const val LOG_LEVEL = "pref_core_loglevel"
        const val MODE = "pref_mode"
        const val IS_BOOTED = "pref_is_booted"
        const val CHECK_UPDATE_PRE_RELEASE = "pref_check_update_pre_release"
        const val GEO_FILES_SOURCES = "pref_geo_files_sources"
    }

    //------------------------------------------------------------------------------
    // Cache keys
    //------------------------------------------------------------------------------
    object Cache {
        const val SUBSCRIPTION_ID = "cache_subscription_id"
        const val KEYWORD_FILTER = "cache_keyword_filter"
    }

    //------------------------------------------------------------------------------
    // Broadcast actions
    //------------------------------------------------------------------------------
    object Broadcast {
        const val SERVICE = "com.v2ray.ang.action.service"
        const val ACTIVITY = "com.v2ray.ang.action.activity"
        const val WIDGET_CLICK = "com.v2ray.ang.action.widget.click"
    }

    //------------------------------------------------------------------------------
    // Messaging constants
    //------------------------------------------------------------------------------
    object Message {
        const val REGISTER_CLIENT = 1
        const val UNREGISTER_CLIENT = 2
        const val STATE_START = 3
        const val STATE_STOP = 4
        const val STATE_RESTART = 5
        const val MEASURE_DELAY = 6
        const val MEASURE_CONFIG = 7

        const val STATE_RUNNING = 11
        const val STATE_NOT_RUNNING = 12
        const val STATE_START_SUCCESS = 31
        const val STATE_START_FAILURE = 32
        const val STATE_STOP_SUCCESS = 41
        const val MEASURE_DELAY_SUCCESS = 61
        const val MEASURE_CONFIG_SUCCESS = 71
        const val MEASURE_CONFIG_CANCEL = 72
    }

    //------------------------------------------------------------------------------
    // URLs & networking
    //------------------------------------------------------------------------------
    object Url {
        const val GITHUB = "https://github.com"
        const val GITHUB_RAW = "https://raw.githubusercontent.com"
        const val DOWNLOAD_LATEST_FORMAT = "$GITHUB/%s/releases/latest/download"
        const val PACKAGE_LIST = "$GITHUB_RAW/2dust/androidpackagenamelist/master/proxy.txt"
        const val APP_REPO = "2dust/v2rayNG"
        const val APP_URL = "$GITHUB/$APP_REPO"
        const val APP_API = "https://api.github.com/repos/$APP_REPO/releases"
        const val ISSUES_URL = "$APP_URL/issues"
        const val WIKI_MODE_URL = "$APP_URL/wiki/Mode"
        const val PRIVACY_POLICY_URL = "$GITHUB_RAW/2dust/v2rayNG/master/CR.md"
        const val DELAY_GENERATE_204 = "https://www.gstatic.com/generate_204"
        const val DELAY_GOOGLE_204 = "https://www.google.com/generate_204"
        const val IP_API_URL = "https://api.ip.sb/geoip"
    }

    //------------------------------------------------------------------------------
    // Network defaults
    //------------------------------------------------------------------------------
    object Network {
        const val DNS_PROXY = "1.1.1.1"
        const val DNS_DIRECT = "223.5.5.5"
        const val DNS_VPN = "1.1.1.1"
        const val PORT_LOCAL_DNS = "10853"
        const val PORT_SOCKS = "10808"
        const val LOOPBACK = "127.0.0.1"
    }

    //------------------------------------------------------------------------------
    // Protocol schemes
    //------------------------------------------------------------------------------
    object Scheme {
        const val VMESS = "vmess://"
        const val SHADOWSOCKS = "ss://"
        const val SOCKS = "socks://"
        const val HTTP = "http://"
        const val VLESS = "vless://"
        const val TROJAN = "trojan://"
        const val WIREGUARD = "wireguard://"
        const val TUIC = "tuic://"
        const val HYSTERIA2 = "hysteria2://"
        const val HY2 = "hy2://"
    }

    //------------------------------------------------------------------------------
    // Other constants
    //------------------------------------------------------------------------------
    const val DEFAULT_PORT = 443
    const val DEFAULT_SECURITY = "auto"
    const val DEFAULT_NETWORK = "tcp"
    const val TLS = "tls"
    const val REALITY = "reality"

    val PRIVATE_IP_LIST = listOf(
        "0.0.0.0/8", "10.0.0.0/8", "127.0.0.0/8",
        "172.16.0.0/12", "192.168.0.0/16", "169.254.0.0/16",
        "224.0.0.0/4"
    )
}
