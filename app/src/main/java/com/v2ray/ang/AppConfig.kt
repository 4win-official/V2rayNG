package com.v2ray.ang

object AppConfig {
    //==============================================================================//
    // Package & Logging
    //==============================================================================//
    /** The application's package name. */
    const val ANG_PACKAGE = BuildConfig.APPLICATION_ID
    /** Tag used for logging. */
    const val TAG = BuildConfig.APPLICATION_ID

    //==============================================================================//
    // Directories
    //==============================================================================//
    const val DIR_ASSETS   = "assets"
    const val DIR_BACKUPS  = "backups"

    //==============================================================================//
    // MMKV Preference Keys
    //==============================================================================//
    object Pref {
        const val SNIFFING_ENABLED            = "pref_sniffing_enabled"
        const val ROUTE_ONLY_ENABLED          = "pref_route_only_enabled"
        const val PER_APP_PROXY               = "pref_per_app_proxy"
        const val PER_APP_PROXY_SET           = "pref_per_app_proxy_set"
        const val BYPASS_APPS                 = "pref_bypass_apps"
        const val LOCAL_DNS_ENABLED           = "pref_local_dns_enabled"
        const val FAKE_DNS_ENABLED            = "pref_fake_dns_enabled"
        const val APPEND_HTTP_PROXY           = "pref_append_http_proxy"
        const val LOCAL_DNS_PORT              = "pref_local_dns_port"
        const val VPN_DNS                     = "pref_vpn_dns"
        const val VPN_BYPASS_LAN              = "pref_vpn_bypass_lan"
        const val ROUTING_DOMAIN_STRATEGY     = "pref_routing_domain_strategy"
        const val ROUTING_RULESET             = "pref_routing_ruleset"
        const val MUX_ENABLED                 = "pref_mux_enabled"
        const val MUX_CONCURRENCY             = "pref_mux_concurrency"
        const val MUX_XUDP_CONCURRENCY        = "pref_mux_xudp_concurrency"
        const val MUX_XUDP_QUIC               = "pref_mux_xudp_quic"
        const val FRAGMENT_ENABLED            = "pref_fragment_enabled"
        const val FRAGMENT_PACKETS            = "pref_fragment_packets"
        const val FRAGMENT_LENGTH             = "pref_fragment_length"
        const val FRAGMENT_INTERVAL           = "pref_fragment_interval"
        const val AUTO_UPDATE_SUBSCRIPTION    = "pref_auto_update_subscription"
        const val UPDATE_INTERVAL             = "pref_auto_update_interval"
        const val SPEED_ENABLED               = "pref_speed_enabled"
        const val CONFIRM_REMOVE              = "pref_confirm_remove"
        const val START_SCAN_IMMEDIATE        = "pref_start_scan_immediate"
        const val DOUBLE_COLUMN_DISPLAY       = "pref_double_column_display"
        const val LANGUAGE                    = "pref_language"
        const val UI_MODE_NIGHT               = "pref_ui_mode_night" // این خط به درستی تعریف شده
        const val PREFER_IPV6                 = "pref_prefer_ipv6"
        const val PROXY_SHARING               = "pref_proxy_sharing_enabled"
        const val ALLOW_INSECURE              = "pref_allow_insecure"
        const val SOCKS_PORT                  = "pref_socks_port"
        const val REMOTE_DNS                  = "pref_remote_dns"
        const val DOMESTIC_DNS                = "pref_domestic_dns"
        const val DNS_HOSTS                   = "pref_dns_hosts"
        const val DELAY_TEST_URL              = "pref_delay_test_url"
        const val LOG_LEVEL                   = "pref_core_loglevel"
        const val MODE                        = "pref_mode"
        const val IS_BOOTED                   = "pref_is_booted"
        const val CHECK_UPDATE_PRE_RELEASE    = "pref_check_update_pre_release"
        const val GEO_FILES_SOURCES           = "pref_geo_files_sources"
    }

    //==============================================================================//
    // Cache Keys
    //==============================================================================//
    const val CACHE_SUBSCRIPTION_ID = "cache_subscription_id"
    const val CACHE_KEYWORD_FILTER  = "cache_keyword_filter"

    //==============================================================================//
    // Broadcast Actions
    //==============================================================================//
    const val BROADCAST_ACTION_SERVICE      = "com.v2ray.ang.action.service"
    const val BROADCAST_ACTION_ACTIVITY     = "com.v2ray.ang.action.activity"
    const val BROADCAST_ACTION_WIDGET_CLICK = "com.v2ray.ang.action.widget.click"

    //==============================================================================//
    // Messaging Constants
    //==============================================================================//
    object Message {
        const val REGISTER_CLIENT          = 1
        const val UNREGISTER_CLIENT        = 2
        const val STATE_START              = 3
        const val STATE_STOP               = 4
        const val STATE_RESTART            = 5
        const val MEASURE_DELAY            = 6
        const val MEASURE_CONFIG           = 7

        const val STATE_RUNNING            = 11
        const val STATE_NOT_RUNNING        = 12
        const val STATE_START_SUCCESS      = 31
        const val STATE_START_FAILURE      = 32
        const val STATE_STOP_SUCCESS       = 41
        const val MEASURE_DELAY_SUCCESS    = 61
        const val MEASURE_CONFIG_SUCCESS   = 71
        const val MEASURE_CONFIG_CANCEL    = 72
    }

    //==============================================================================//
    // URLs & Networking
    //==============================================================================//
    object Url {
        const val GITHUB                = "https://github.com"
        const val GITHUB_RAW            = "https://raw.githubusercontent.com"
        const val DOWNLOAD_LATEST       = "$GITHUB/%s/releases/latest/download"
        const val PACKAGE_LIST          = "$GITHUB_RAW/2dust/androidpackagenamelist/master/proxy.txt"
        const val APP_REPO              = "2dust/v2rayNG"
        const val APP_URL               = "$GITHUB/$APP_REPO"
        const val APP_API               = "https://api.github.com/repos/$APP_REPO/releases"
        const val ISSUES_URL            = "$APP_URL/issues"
        const val WIKI_MODE_URL         = "$APP_URL/wiki/Mode"
        const val PRIVACY_POLICY        = "$GITHUB_RAW/2dust/v2rayNG/master/CR.md"
        const val DELAY_TEST1           = "https://www.gstatic.com/generate_204"
        const val DELAY_TEST2           = "https://www.google.com/generate_204"
        const val IP_API                 = "https://api.ip.sb/geoip"
    }

    //==============================================================================//
    // DNS & Network Defaults
    //==============================================================================//
    object Network {
        const val DNS_PROXY       = "1.1.1.1"
        const val DNS_DIRECT      = "223.5.5.5"
        const val DNS_VPN         = "1.1.1.1"

        const val PORT_LOCAL_DNS  = "10853"
        const val PORT_SOCKS      = "10808"
        const val LOOPBACK        = "127.0.0.1"
    }

    //==============================================================================//
    // Protocol Schemes
    //==============================================================================//
    object Scheme {
        const val VMESS      = "vmess://"
        const val SHADOWSOCKS= "ss://"
        const val SOCKS      = "socks://"
        const val HTTP       = "http://"
        const val VLESS      = "vless://"
        const val TROJAN     = "trojan://"
        // … سایر پروتکل‌ها
    }

    //==============================================================================//
    // Other Constants
    //==============================================================================//
    const val DEFAULT_PORT     = 443
    const val DEFAULT_SECURITY = "auto"
    const val DEFAULT_NETWORK  = "tcp"
    const val TLS              = "tls"
    const val REALITY          = "reality"

    /** Private IP CIDR lists **/
    val PRIVATE_IP_LIST = listOf(
        "0.0.0.0/8", "10.0.0.0/8", "127.0.0.0/8",
        "172.16.0.0/12", "192.168.0.0/16", "169.254.0.0/16",
        "224.0.0.0/4"
    )
}
