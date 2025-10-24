// ==UserScript==
// @name         屏蔽管理
// @namespace    http://tampermonkey.net/
// @version      1.3.2
// @description  屏蔽微博、知乎、小红书、B站含关键词的内容和指定用户，支持精准用户ID屏蔽和B站卡片屏蔽，新增知乎低赞内容屏蔽、文章屏蔽和盐选内容屏蔽
// @match        https://www.zhihu.com/
// @match        https://www.xiaohongshu.com/*
// @match        https://www.bilibili.com/
// @match        https://www.bilibili.com/?*
// @match        https://www.bilibili.com/v/*
// @match        https://search.bilibili.com/*
// @match        https://weibo.com/*
// @match        https://www.weibo.com/*
// @match        https://s.weibo.com/*
// @grant        GM_setValue
// @grant        GM_getValue
// @license      MIT
// ==/UserScript==

(function() {
    'use strict';

    // 默认的屏蔽关键词列表
    const DEFAULT_KEYWORDS = [];

    // 默认的屏蔽用户ID列表
    const DEFAULT_USER_IDS = [];

    // 存储和获取屏蔽关键词和用户
    const STORAGE_KEYWORDS_KEY = 'keyword_blocker_words';
    const STORAGE_USER_IDS_KEY = 'keyword_blocker_user_ids';
    const DISABLED_SITES_KEY = 'keyword_blocker_disabled_sites';
    const BILIBILI_CARD_BLOCK_KEY = 'bilibili_card_block_enabled';
    const ZHIHU_LOW_LIKE_KEY = 'zhihu_low_like_settings';
    const ZHIHU_ARTICLE_BLOCK_KEY = 'zhihu_article_block_enabled';
    const ZHIHU_SALT_BLOCK_KEY = 'zhihu_salt_block_enabled';
    const LAST_BACKUP_TIME_KEY = 'keyword_blocker_last_backup';
    const STATS_KEY = 'keyword_blocker_stats';

    // 知乎低赞屏蔽设置
    const DEFAULT_LOW_LIKE_SETTINGS = {
        enabled: false,
        threshold: 10
    };

    // 屏蔽统计
    const DEFAULT_STATS = {
        totalBlocked: 0,
        keywordBlocked: 0,
        useridBlocked: 0,
        lastBlockTime: null,
        startupCount: 0
    };

    // 性能优化缓存
    let cachedElements = new Map();
    let observers = [];

    // 屏蔽统计
    let stats = {...DEFAULT_STATS};

    function saveKeywords(keywords) {
        localStorage.setItem(STORAGE_KEYWORDS_KEY, JSON.stringify(keywords));
    }

    function loadKeywords() {
        try {
            const saved = localStorage.getItem(STORAGE_KEYWORDS_KEY);
            return saved ? JSON.parse(saved) : [...DEFAULT_KEYWORDS];
        } catch (e) {
            console.error('加载屏蔽词失败:', e);
            return [...DEFAULT_KEYWORDS];
        }
    }

    function saveUserIds(userIds) {
        localStorage.setItem(STORAGE_USER_IDS_KEY, JSON.stringify(userIds));
    }

    function loadUserIds() {
        try {
            const saved = localStorage.getItem(STORAGE_USER_IDS_KEY);
            return saved ? JSON.parse(saved) : [...DEFAULT_USER_IDS];
        } catch (e) {
            console.error('加载屏蔽用户ID失败:', e);
            return [...DEFAULT_USER_IDS];
        }
    }

    // 加载统计
    function loadStats() {
        try {
            const saved = localStorage.getItem(STATS_KEY);
            const loadedStats = saved ? JSON.parse(saved) : {...DEFAULT_STATS};
            loadedStats.startupCount = (loadedStats.startupCount || 0) + 1;
            return loadedStats;
        } catch (e) {
            console.error('加载统计失败:', e);
            return {...DEFAULT_STATS, startupCount: 1};
        }
    }

    // 保存统计
    function saveStats() {
        try {
            localStorage.setItem(STATS_KEY, JSON.stringify(stats));
        } catch (e) {
            console.error('保存统计失败:', e);
        }
    }

    // B站卡片屏蔽状态管理
    function isBilibiliCardBlockEnabled() {
        try {
            const saved = localStorage.getItem(BILIBILI_CARD_BLOCK_KEY);
            return saved ? JSON.parse(saved) : true;
        } catch (e) {
            console.error('加载B站卡片屏蔽设置失败:', e);
            return true;
        }
    }

    function saveBilibiliCardBlockState(enabled) {
        localStorage.setItem(BILIBILI_CARD_BLOCK_KEY, JSON.stringify(enabled));
    }

    // 知乎低赞屏蔽设置管理
    function loadLowLikeSettings() {
        try {
            const saved = localStorage.getItem(ZHIHU_LOW_LIKE_KEY);
            return saved ? JSON.parse(saved) : {...DEFAULT_LOW_LIKE_SETTINGS};
        } catch (e) {
            console.error('加载知乎低赞设置失败:', e);
            return {...DEFAULT_LOW_LIKE_SETTINGS};
        }
    }

    function saveLowLikeSettings(settings) {
        localStorage.setItem(ZHIHU_LOW_LIKE_KEY, JSON.stringify(settings));
    }

    // 知乎文章屏蔽状态管理
    function isZhihuArticleBlockEnabled() {
        try {
            const saved = localStorage.getItem(ZHIHU_ARTICLE_BLOCK_KEY);
            return saved ? JSON.parse(saved) : true;
        } catch (e) {
            console.error('加载知乎文章屏蔽设置失败:', e);
            return true;
        }
    }

    function saveZhihuArticleBlockState(enabled) {
        localStorage.setItem(ZHIHU_ARTICLE_BLOCK_KEY, JSON.stringify(enabled));
    }

    // 知乎盐选屏蔽状态管理
    function isZhihuSaltBlockEnabled() {
        try {
            const saved = localStorage.getItem(ZHIHU_SALT_BLOCK_KEY);
            return saved ? JSON.parse(saved) : true;
        } catch (e) {
            console.error('加载知乎盐选屏蔽设置失败:', e);
            return true;
        }
    }

    function saveZhihuSaltBlockState(enabled) {
        localStorage.setItem(ZHIHU_SALT_BLOCK_KEY, JSON.stringify(enabled));
    }

    // 禁用网站管理
    function saveDisabledSites(sites) {
        localStorage.setItem(DISABLED_SITES_KEY, JSON.stringify(sites));
    }

    function loadDisabledSites() {
        try {
            const saved = localStorage.getItem(DISABLED_SITES_KEY);
            return saved ? JSON.parse(saved) : [];
        } catch (e) {
            console.error('加载禁用网站失败:', e);
            return [];
        }
    }

    function isCurrentSiteDisabled() {
        const disabledSites = loadDisabledSites();
        const currentSite = getCurrentSite();
        return disabledSites.includes(currentSite);
    }

    function disableCurrentSite() {
        const disabledSites = loadDisabledSites();
        const currentSite = getCurrentSite();
        if (!disabledSites.includes(currentSite)) {
            disabledSites.push(currentSite);
            saveDisabledSites(disabledSites);
        }
    }

    function enableCurrentSite() {
        const disabledSites = loadDisabledSites();
        const currentSite = getCurrentSite();
        const index = disabledSites.indexOf(currentSite);
        if (index > -1) {
            disabledSites.splice(index, 1);
            saveDisabledSites(disabledSites);
        }
    }

    // 获取当前网站类型
    function getCurrentSite() {
        const hostname = window.location.hostname;
        if (hostname.includes('zhihu.com')) return 'zhihu';
        if (hostname.includes('xiaohongshu.com')) return 'xiaohongshu';
        if (hostname.includes('bilibili.com')) return 'bilibili';
        if (hostname.includes('weibo.com')) return 'weibo';
        return 'unknown';
    }

    // 网站特定的配置 - 简化版本
    const siteConfigs = {
        zhihu: {
            containerSelector: '.ContentItem, .TopstoryItem, .ArticleItem, .css-79elbk',
            titleSelector: '.ContentItem-title a, .ArticleItem-title a, .QuestionItem-title a, .css-1j9i4q2',
            userSelector: '.AuthorInfo-name, .UserLink-link, .ArticleItem-authorInfo .UserLink, .AuthorInfo .UserLink, .css-1gomreu',
            userIdSelector: '.UserLink-link',
            logPrefix: '已屏蔽知乎内容',
            userLogPrefix: '已屏蔽知乎用户',
            likeCountSelector: 'button.VoteButton',
            articleSelector: '.ArticleItem, .ContentItem.ArticleItem',
            saltSelectors: [
                'div.KfeCollection-OrdinaryLabel-content',
                'p.KfeCollection-IntroCard-contentName-newStyle-pc',
                '.KfeCollection-CommonCard-SingleCardV2',
                '.KfeCollection-AnswerCard-V2',
                '.KfeCollection-PureVideoCard-V2'
            ],
            saltContainerSelector: 'div.Card.TopstoryItem.TopstoryItem-isRecommend',
            extractUserId: function(userElement) {
                if (userElement && userElement.href) {
                    const match = userElement.href.match(/zhihu\.com\/people\/([^\/?]+)/);
                    if (match && match[1] && !match[1].includes('.')) {
                        return match[1];
                    }
                }
                return null;
            }
        },
        xiaohongshu: {
            containerSelector: 'section.note-item, [data-v-]',
            titleSelector: 'a.title, .title, [class*="title"]',
            userSelector: '.author .name, .username, [class*="name"], [class*="author"]',
            userIdSelector: 'a[href*="/user/profile/"]',
            logPrefix: '已屏蔽小红书内容',
            userLogPrefix: '已屏蔽小红书用户',
            extractUserId: function(userElement) {
                if (userElement && userElement.href) {
                    const match = userElement.href.match(/\/user\/profile\/([a-f0-9]{24})/);
                    return match ? match[1] : null;
                }
                return null;
            }
        },
        bilibili: {
            containerSelector: '.bili-feed-card, .bili-video-card, [class*="card"]',
            titleSelector: '.bili-video-card__info--tit, .bili-video-card__info--tit a, .bili-video-card__wrap .bili-video-card__info--tit, [class*="title"]',
            userSelector: '.bili-video-card__info--author, .up-name__text, .bili-video-card__info--author a, .up-name, [class*="author"], [class*="name"]',
            userIdSelector: '.bili-video-card__info--author a',
            logPrefix: '已屏蔽B站内容',
            userLogPrefix: '已屏蔽B站UP主',
            cardSelector: '.floor-single-card, .bili-live-card.is-rcmd',
            extractUserId: function(userElement) {
                if (userElement && userElement.href) {
                    const match = userElement.href.match(/space\.bilibili\.com\/(\d+)/);
                    if (match && match[1] && !isNaN(parseInt(match[1]))) {
                        return match[1];
                    }
                }
                return null;
            }
        },
        weibo: {
            containerSelector: '.wbpro-scroller-item, [class*="item"]',
            titleSelector: '.wbpro-feed-content .detail_wbtext_4CRf9, [class*="text"]',
            userSelector: '.wbpro-feed-content .name, .woo-box-item .name, [class*="name"], [class*="user"]',
            userIdSelector: '.woo-box-item .name',
            logPrefix: '已屏蔽微博内容',
            userLogPrefix: '已屏蔽微博用户',
            extractUserId: function(userElement) {
                if (userElement && userElement.href) {
                    // 处理微博用户链接
                    const href = userElement.href;
                    // 匹配 https://weibo.com/u/7365927199 格式
                    const uMatch = href.match(/weibo\.com\/u\/(\d+)/);
                    if (uMatch && uMatch[1]) {
                        return uMatch[1];
                    }
                    // 匹配 https://weibo.com/n/用户名 格式（需要进一步处理）
                    const nMatch = href.match(/weibo\.com\/n\/([^\/?]+)/);
                    if (nMatch && nMatch[1]) {
                        return nMatch[1]; // 返回用户名
                    }
                    // 匹配 https://weibo.com/用户名 格式
                    const directMatch = href.match(/weibo\.com\/([^\/?]+)/);
                    if (directMatch && directMatch[1] &&
                        !['u', 'n', 'p', 'search', 'home', 'login'].includes(directMatch[1])) {
                        return directMatch[1]; // 返回用户名
                    }
                }
                return null;
            }
        }
    };

    // 当前屏蔽关键词列表和用户ID列表
    let BLOCK_KEYWORDS = loadKeywords();
    let BLOCK_USER_IDS = loadUserIds();

    // B站卡片屏蔽状态
    let BILIBILI_CARD_BLOCK_ENABLED = isBilibiliCardBlockEnabled();
    let bilibiliCardStyleElement = null;

    // 知乎低赞屏蔽设置
    let ZHIHU_LOW_LIKE_SETTINGS = loadLowLikeSettings();

    // 知乎文章和盐选屏蔽状态
    let ZHIHU_ARTICLE_BLOCK_ENABLED = isZhihuArticleBlockEnabled();
    let ZHIHU_SALT_BLOCK_ENABLED = isZhihuSaltBlockEnabled();
    let zhihuSaltStyleElement = null;

    // 创建管理UI
    function createManagementUI() {
        // 创建CSS样式
        const style = document.createElement('style');
        style.textContent = `
            #keyword-blocker-toggle {
                position: fixed;
                left: 20px;
                top: 50%;
                transform: translateY(-50%);
                z-index: 10000;
                background: #1890ff;
                color: white;
                border: none;
                border-radius: 6px;
                padding: 12px 8px;
                cursor: pointer;
                font-size: 14px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.15);
                transition: all 0.3s ease;
                writing-mode: vertical-lr;
                text-orientation: mixed;
            }

            #keyword-blocker-toggle:hover {
                background: #40a9ff;
                transform: translateY(-50%) scale(1.05);
            }

            #keyword-blocker-panel {
                position: fixed;
                left: -420px;
                top: 50%;
                transform: translateY(-50%);
                z-index: 9999;
                width: 390px;
                max-height: 80vh;
                background: white;
                border: 1px solid #d9d9d9;
                border-radius: 8px;
                box-shadow: 0 4px 16px rgba(0,0,0,0.15);
                transition: left 0.3s ease;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                overflow: hidden;
            }

            #keyword-blocker-panel.show {
                left: 20px;
            }

            .kb-panel-header {
                padding: 16px;
                border-bottom: 1px solid #f0f0f0;
                background: #fafafa;
                border-radius: 8px 8px 0 0;
            }

            .kb-title-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 12px;
            }

            .kb-panel-title {
                margin: 0;
                font-size: 16px;
                font-weight: 500;
                color: #262626;
                flex: 1;
            }

            .kb-tab-container {
                display: flex;
                margin-bottom: 12px;
                border-bottom: 1px solid #f0f0f0;
            }

            .kb-tab {
                flex: 1;
                padding: 8px 12px;
                background: none;
                border: none;
                border-bottom: 2px solid transparent;
                cursor: pointer;
                font-size: 14px;
                color: #666;
                transition: all 0.3s ease;
            }

            .kb-tab.active {
                color: #1890ff;
                border-bottom-color: #1890ff;
                background: #f0f8ff;
            }

            .kb-tab-content {
                display: none;
            }

            .kb-tab-content.active {
                display: block;
            }

            .kb-input-group {
                display: flex;
                gap: 8px;
                margin-bottom: 8px;
            }

            .kb-input {
                flex: 1;
                padding: 8px 12px;
                border: 1px solid #d9d9d9;
                border-radius: 4px;
                font-size: 14px;
                outline: none;
            }

            .kb-input:focus {
                border-color: #1890ff;
                box-shadow: 0 0 0 2px rgba(24,144,255,0.2);
            }

            .kb-btn {
                padding: 8px 16px;
                background: #1890ff;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
                transition: background 0.3s ease;
            }

            .kb-btn:hover {
                background: #40a9ff;
            }

            .kb-btn-danger {
                background: #ff4d4f;
            }

            .kb-btn-danger:hover {
                background: #ff7875;
            }

            .kb-list-container {
                max-height: 300px;
                overflow-y: auto;
                padding: 0;
                border: 1px solid #f0f0f0;
                border-radius: 4px;
                margin-bottom: 12px;
            }

            .kb-list {
                list-style: none;
                margin: 0;
                padding: 0;
            }

            .kb-list-item {
                display: flex;
                justify-content: space-between;
                align-items: flex-start;
                padding: 12px 16px;
                border-bottom: 1px solid #f0f0f0;
                transition: background 0.2s ease;
            }

            .kb-list-item:hover {
                background: #f5f5f5;
            }

            .kb-item-content {
                flex: 1;
                display: flex;
                flex-direction: column;
            }

            .kb-item-text {
                font-size: 14px;
                color: #262626;
                word-break: break-all;
                margin-bottom: 4px;
            }

            .kb-item-type {
                font-size: 12px;
                color: #666;
                background: #f0f0f0;
                padding: 2px 6px;
                border-radius: 3px;
                align-self: flex-start;
            }

            .kb-item-type.userid {
                background: #f6ffed;
                color: #52c41a;
            }

            .kb-delete-btn {
                padding: 4px 8px;
                background: #ff4d4f;
                color: white;
                border: none;
                border-radius: 3px;
                cursor: pointer;
                font-size: 12px;
                transition: background 0.3s ease;
                margin-left: 8px;
            }

            .kb-delete-btn:hover {
                background: #ff7875;
            }

            .kb-confirm-group {
                display: flex;
                gap: 8px;
            }

            .kb-confirm-btn {
                padding: 4px 8px;
                border: none;
                border-radius: 3px;
                cursor: pointer;
                font-size: 12px;
                transition: background 0.3s ease;
            }

            .kb-confirm-delete {
                background: #ff4d4f;
                color: white;
            }

            .kb-confirm-delete:hover {
                background: #ff7875;
            }

            .kb-confirm-cancel {
                background: #8c8c8c;
                color: white;
            }

            .kb-confirm-cancel:hover {
                background: #a6a6a6;
            }

            .kb-stats {
                padding: 12px 16px;
                background: #f9f9f9;
                border-top: 1px solid #f0f0f0;
                font-size: 12px;
                color: #666;
                text-align: center;
            }

            .kb-stats-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 8px;
                margin-top: 8px;
            }

            .kb-stat-item {
                padding: 8px;
                background: white;
                border-radius: 4px;
                border: 1px solid #f0f0f0;
            }

            .kb-stat-value {
                font-size: 16px;
                font-weight: bold;
                color: #1890ff;
            }

            .kb-stat-label {
                font-size: 11px;
                color: #999;
            }

            .kb-import-export-container {
                padding: 12px 16px;
                background: #f9f9f9;
                border-top: 1px solid #f0f0f0;
                border-radius: 0 0 8px 8px;
            }

            .kb-import-export-group {
                display: flex;
                gap: 8px;
            }

            .kb-import-export-group .kb-btn {
                flex: 1;
                padding: 8px;
                font-size: 13px;
            }

            .kb-close-btn {
                position: absolute;
                top: 10px;
                right: 10px;
                background: none;
                border: none;
                font-size: 18px;
                cursor: pointer;
                color: #999;
                padding: 4px;
                border-radius: 3px;
                transition: all 0.2s ease;
            }

            .kb-close-btn:hover {
                background: #f0f0f0;
                color: #666;
            }

            .kb-disable-site-btn {
                padding: 4px 8px;
                background: #8c8c8c;
                color: white;
                border: none;
                border-radius: 3px;
                cursor: pointer;
                font-size: 10px;
                white-space: nowrap;
                margin-left: 12px;
                transition: background 0.3s ease;
            }

            .kb-disable-site-btn:hover {
                background: #a6a6a6;
            }

            .kb-input-hint {
                font-size: 12px;
                color: #999;
                margin-top: 4px;
                margin-bottom: 12px;
            }

            .kb-card-block-section, .kb-low-like-section, .kb-zhihu-section {
                padding: 20px;
                text-align: center;
            }

            .kb-card-block-title, .kb-low-like-title, .kb-zhihu-title {
                font-size: 16px;
                font-weight: 500;
                margin-bottom: 16px;
                color: #262626;
            }

            .kb-card-block-desc, .kb-low-like-desc, .kb-zhihu-desc {
                font-size: 14px;
                color: #666;
                margin-bottom: 20px;
                line-height: 1.5;
            }

            .kb-toggle-btn {
                padding: 12px 24px;
                background: #1890ff;
                color: white;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                font-weight: 500;
                transition: all 0.3s ease;
            }

            .kb-toggle-btn:hover {
                background: #40a9ff;
                transform: translateY(-1px);
            }

            .kb-toggle-btn.disabled {
                background: #8c8c8c;
            }

            .kb-toggle-btn.disabled:hover {
                background: #a6a6a6;
                transform: none;
            }

            .kb-status-indicator {
                display: inline-block;
                width: 8px;
                height: 8px;
                border-radius: 50%;
                margin-right: 8px;
            }

            .kb-status-enabled {
                background: #52c41a;
            }

            .kb-status-disabled {
                background: #ff4d4f;
            }

            .kb-threshold-control {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 12px;
                margin-bottom: 16px;
            }

            .kb-threshold-input {
                width: 80px;
                padding: 8px 12px;
                border: 1px solid #d9d9d9;
                border-radius: 4px;
                font-size: 14px;
                text-align: center;
            }

            .kb-threshold-input:focus {
                border-color: #1890ff;
                box-shadow: 0 0 0 2px rgba(24,144,255,0.2);
            }

            .kb-threshold-label {
                font-size: 14px;
                color: #262626;
            }

            .kb-zhihu-feature {
                margin-bottom: 20px;
                padding: 16px;
                border: 1px solid #f0f0f0;
                border-radius: 8px;
                background: #fafafa;
            }

            .kb-zhihu-feature:last-child {
                margin-bottom: 0;
            }

            .kb-backup-reminder {
                background: #fff7e6;
                border: 1px solid #ffd591;
                border-radius: 6px;
                padding: 12px;
                margin: 12px 0;
                font-size: 13px;
            }

            .kb-backup-reminder.hidden {
                display: none;
            }
        `;
        document.head.appendChild(style);

        // 创建切换按钮
        const toggleBtn = document.createElement('button');
        toggleBtn.id = 'keyword-blocker-toggle';
        toggleBtn.textContent = '屏蔽管理';
        toggleBtn.title = 'Ctrl+Shift+B 打开/关闭面板';
        document.body.appendChild(toggleBtn);

        // 创建管理面板
        const panel = document.createElement('div');
        panel.id = 'keyword-blocker-panel';
        const currentSite = getCurrentSite();
        const siteNames = {
            'zhihu': '知乎',
            'xiaohongshu': '小红书',
            'bilibili': 'B站',
            'weibo': '微博'
        };
        const siteName = siteNames[currentSite] || '当前网站';

        const isDisabled = isCurrentSiteDisabled();
        const btnText = isDisabled ? `重新启用${siteName}屏蔽` : `有BUG？停止屏蔽${siteName}`;
        const statusText = isDisabled ? `⚠️ ${siteName}屏蔽功能已停用` : '屏蔽管理';

        // 构建选项卡HTML - 移除用户名屏蔽选项卡
        const tabHtml = `
            <div class="kb-tab-container">
                <button class="kb-tab active" data-tab="keywords">关键词屏蔽</button>
                <button class="kb-tab" data-tab="userids">用户ID屏蔽</button>
                ${currentSite === 'bilibili' ? '<button class="kb-tab" data-tab="cards">卡片屏蔽</button>' : ''}
                ${currentSite === 'zhihu' ? '<button class="kb-tab" data-tab="zhihu-advanced">知乎增强</button>' : ''}
                <button class="kb-tab" data-tab="stats">统计</button>
            </div>
        `;

        // 构建内容区域HTML - 移除了关键词优化建议
        const contentHtml = `
            <!-- 关键词屏蔽标签页 -->
            <div id="kb-tab-keywords" class="kb-tab-content active">
                <div class="kb-input-group">
                    <input type="text" id="kb-keyword-input" class="kb-input" placeholder="输入屏蔽词，用 , 或 / 分隔" />
                    <button id="kb-add-keyword-btn" class="kb-btn">新增</button>
                </div>
                <div class="kb-input-hint">多个关键词可用逗号或斜杠分隔</div>
                <div class="kb-backup-reminder hidden" id="kb-backup-reminder">
                    <strong>💡 备份提醒</strong><br>
                    您已经一周没有备份数据了，建议导出备份以防数据丢失。
                </div>
                <div class="kb-list-container">
                    <ul id="kb-keyword-list" class="kb-list"></ul>
                </div>
                <div class="kb-stats">
                    当前共有 <span id="kb-keyword-count">0</span> 个屏蔽词
                </div>
            </div>

            <!-- 用户ID屏蔽标签页 -->
            <div id="kb-tab-userids" class="kb-tab-content">
                <div class="kb-input-group">
                    <input type="text" id="kb-userid-input" class="kb-input" placeholder="输入用户主页链接" />
                    <button id="kb-add-userid-btn" class="kb-btn">新增</button>
                </div>
                <div class="kb-input-hint" id="kb-userid-hint">
                    输入用户主页链接进行精准屏蔽
                </div>
                <div class="kb-list-container">
                    <ul id="kb-userid-list" class="kb-list"></ul>
                </div>
                <div class="kb-stats">
                    当前共有 <span id="kb-userid-count">0</span> 个屏蔽用户ID
                </div>
            </div>

            <!-- B站卡片屏蔽标签页 -->
            ${currentSite === 'bilibili' ? `
            <div id="kb-tab-cards" class="kb-tab-content">
                <div class="kb-card-block-section">
                    <div class="kb-card-block-title">
                        <span class="kb-status-indicator ${BILIBILI_CARD_BLOCK_ENABLED ? 'kb-status-enabled' : 'kb-status-disabled'}"></span>
                        B站卡片屏蔽
                    </div>
                    <div class="kb-card-block-desc">
                        启用后会同时隐藏B站的分区推荐卡片（如"国创"、"综艺"等）和直播推荐卡片，让界面更加清爽。
                    </div>
                    <button id="kb-toggle-card-block" class="kb-toggle-btn ${BILIBILI_CARD_BLOCK_ENABLED ? '' : 'disabled'}">
                        ${BILIBILI_CARD_BLOCK_ENABLED ? '已启用 - 点击关闭' : '已关闭 - 点击启用'}
                    </button>
                </div>
            </div>
            ` : ''}

            <!-- 知乎增强屏蔽标签页 -->
            ${currentSite === 'zhihu' ? `
            <div id="kb-tab-zhihu-advanced" class="kb-tab-content">
                <div class="kb-zhihu-section">
                    <!-- 低赞屏蔽功能 -->
                    <div class="kb-zhihu-feature">
                        <div class="kb-zhihu-title">
                            <span class="kb-status-indicator ${ZHIHU_LOW_LIKE_SETTINGS.enabled ? 'kb-status-enabled' : 'kb-status-disabled'}"></span>
                            知乎低赞内容屏蔽
                        </div>
                        <div class="kb-zhihu-desc">
                            启用后会隐藏点赞数低于设定阈值的内容，让您只看到高质量的回答。
                        </div>
                        <div class="kb-threshold-control">
                            <label class="kb-threshold-label">点赞阈值:</label>
                            <input type="number" id="kb-low-like-threshold" class="kb-threshold-input" value="${ZHIHU_LOW_LIKE_SETTINGS.threshold}" min="0" />
                        </div>
                        <button id="kb-toggle-low-like" class="kb-toggle-btn ${ZHIHU_LOW_LIKE_SETTINGS.enabled ? '' : 'disabled'}">
                            ${ZHIHU_LOW_LIKE_SETTINGS.enabled ? '已启用 - 点击关闭' : '已关闭 - 点击启用'}
                        </button>
                    </div>

                    <!-- 文章屏蔽功能 -->
                    <div class="kb-zhihu-feature">
                        <div class="kb-zhihu-title">
                            <span class="kb-status-indicator ${ZHIHU_ARTICLE_BLOCK_ENABLED ? 'kb-status-enabled' : 'kb-status-disabled'}"></span>
                            知乎文章屏蔽
                        </div>
                        <div class="kb-zhihu-desc">
                            启用后会隐藏知乎的所有文章内容，只显示问答内容。
                        </div>
                        <button id="kb-toggle-article-block" class="kb-toggle-btn ${ZHIHU_ARTICLE_BLOCK_ENABLED ? '' : 'disabled'}">
                            ${ZHIHU_ARTICLE_BLOCK_ENABLED ? '已启用 - 点击关闭' : '已关闭 - 点击启用'}
                        </button>
                    </div>

                    <!-- 盐选内容屏蔽功能 -->
                    <div class="kb-zhihu-feature">
                        <div class="kb-zhihu-title">
                            <span class="kb-status-indicator ${ZHIHU_SALT_BLOCK_ENABLED ? 'kb-status-enabled' : 'kb-status-disabled'}"></span>
                            知乎盐选内容屏蔽
                        </div>
                        <div class="kb-zhihu-desc">
                            启用后会隐藏知乎的盐选付费内容，包括盐选专栏、付费故事等。
                        </div>
                        <button id="kb-toggle-salt-block" class="kb-toggle-btn ${ZHIHU_SALT_BLOCK_ENABLED ? '' : 'disabled'}">
                            ${ZHIHU_SALT_BLOCK_ENABLED ? '已启用 - 点击关闭' : '已关闭 - 点击启用'}
                        </button>
                    </div>
                </div>
            </div>
            ` : ''}

            <!-- 统计标签页 -->
            <div id="kb-tab-stats" class="kb-tab-content">
                <div class="kb-stats" style="text-align: left; padding: 20px;">
                    <h3 style="margin-top: 0; margin-bottom: 16px;">屏蔽统计</h3>
                    <div class="kb-stats-grid">
                        <div class="kb-stat-item">
                            <div class="kb-stat-value" id="stat-total">${stats.totalBlocked}</div>
                            <div class="kb-stat-label">总屏蔽次数</div>
                        </div>
                        <div class="kb-stat-item">
                            <div class="kb-stat-value" id="stat-keyword">${stats.keywordBlocked}</div>
                            <div class="kb-stat-label">关键词屏蔽</div>
                        </div>
                        <div class="kb-stat-item">
                            <div class="kb-stat-value" id="stat-userid">${stats.useridBlocked}</div>
                            <div class="kb-stat-label">用户ID屏蔽</div>
                        </div>
                        <div class="kb-stat-item">
                            <div class="kb-stat-value" id="stat-startup">${stats.startupCount}</div>
                            <div class="kb-stat-label">启动次数</div>
                        </div>
                    </div>
                    <div style="margin-top: 16px; font-size: 12px; color: #999;">
                        最后屏蔽时间: <span id="stat-last-time">${stats.lastBlockTime ? new Date(stats.lastBlockTime).toLocaleString() : '暂无'}</span>
                    </div>
                    <button id="kb-reset-stats" class="kb-btn" style="margin-top: 16px; width: 100%; background: #ff4d4f;">重置统计</button>
                </div>
            </div>
        `;

        panel.innerHTML = `
            <button class="kb-close-btn" id="kb-close">×</button>
            <div class="kb-panel-header">
                <div class="kb-title-row">
                    <h3 class="kb-panel-title">${statusText}</h3>
                    <button class="kb-disable-site-btn" id="kb-disable-site">${btnText}</button>
                </div>
                ${tabHtml}
                ${contentHtml}
            </div>
            <div class="kb-import-export-container">
                <div class="kb-import-export-group">
                    <button id="kb-export-btn" class="kb-btn">导出数据</button>
                    <button id="kb-import-btn" class="kb-btn">导入数据</button>
                    <input type="file" id="kb-import-file" accept=".json" style="display: none;" />
                </div>
            </div>
        `;
        document.body.appendChild(panel);

        return { toggleBtn, panel };
    }

    // 更新用户ID屏蔽提示信息
    function updateUseridInputHint() {
        const hintElement = document.getElementById('kb-userid-hint');
        if (!hintElement) return;

        const site = getCurrentSite();
        const siteExamples = {
            'zhihu': '例如：https://www.zhihu.com/people/user-id',
            'xiaohongshu': '例如：https://www.xiaohongshu.com/user/profile/用户ID',
            'bilibili': '例如：https://space.bilibili.com/用户ID',
            'weibo': '例如：https://weibo.com/u/用户ID'
        };
        const example = siteExamples[site] || '请输入完整的用户主页链接';
        hintElement.textContent = `输入用户主页链接进行精准屏蔽（推荐）\n${example}`;
    }

    // 从用户链接中提取用户ID
    function extractUserIdFromInput(input, site) {
        if (!input || typeof input !== 'string') return null;

        const config = siteConfigs[site];
        if (!config || !config.extractUserId) return null;

        try {
            // 确保输入是完整的URL
            let url = input;
            if (!url.startsWith('http://') && !url.startsWith('https://')) {
                url = 'https://' + url;
            }

            // 创建一个临时元素来模拟用户链接
            const tempElement = document.createElement('a');
            tempElement.href = url;

            return config.extractUserId(tempElement);
        } catch (error) {
            console.error('提取用户ID失败:', error);
            return null;
        }
    }

    // 渲染屏蔽词列表
    function renderKeywordList() {
        const list = document.getElementById('kb-keyword-list');
        const count = document.getElementById('kb-keyword-count');

        if (!list || !count) return;

        list.innerHTML = '';
        count.textContent = BLOCK_KEYWORDS.length;

        BLOCK_KEYWORDS.forEach((keyword, index) => {
            const li = document.createElement('li');
            li.className = 'kb-list-item';
            li.dataset.index = index;
            li.innerHTML = `
                <span class="kb-item-text">${keyword}</span>
                <button class="kb-delete-btn" data-index="${index}" data-type="keyword">删除</button>
            `;
            list.appendChild(li);
        });
    }

    // 渲染用户ID列表
    function renderUseridList() {
        const list = document.getElementById('kb-userid-list');
        const count = document.getElementById('kb-userid-count');

        if (!list || !count) return;

        list.innerHTML = '';
        count.textContent = BLOCK_USER_IDS.length;

        BLOCK_USER_IDS.forEach((userid, index) => {
            const li = document.createElement('li');
            li.className = 'kb-list-item';
            li.dataset.index = index;
            li.innerHTML = `
                <div class="kb-item-content">
                    <span class="kb-item-text">${userid}</span>
                    <span class="kb-item-type userid">用户ID</span>
                </div>
                <button class="kb-delete-btn" data-index="${index}" data-type="userid">删除</button>
            `;
            list.appendChild(li);
        });
    }

    // 显示确认删除界面
    function showDeleteConfirm(listItem, index, type) {
        let itemText = '';
        let typeText = '';

        if (type === 'keyword') {
            itemText = BLOCK_KEYWORDS[index];
            typeText = '关键词';
        } else if (type === 'userid') {
            itemText = BLOCK_USER_IDS[index];
            typeText = '用户ID';
        }

        listItem.innerHTML = `
            <div class="kb-item-content">
                <span class="kb-item-text">${itemText}</span>
                <span class="kb-item-type ${type === 'userid' ? 'userid' : ''}">${typeText}</span>
            </div>
            <div class="kb-confirm-group">
                <button class="kb-confirm-btn kb-confirm-delete" data-index="${index}" data-type="${type}">确认删除</button>
                <button class="kb-confirm-btn kb-confirm-cancel" data-index="${index}" data-type="${type}">手滑了</button>
            </div>
        `;
    }

    // 恢复正常显示
    function restoreNormalView(listItem, index, type) {
        let itemText = '';
        let typeText = '';
        let typeClass = '';

        if (type === 'keyword') {
            itemText = BLOCK_KEYWORDS[index];
            typeText = '关键词';
        } else if (type === 'userid') {
            itemText = BLOCK_USER_IDS[index];
            typeText = '用户ID';
            typeClass = 'userid';
        }

        listItem.innerHTML = `
            <div class="kb-item-content">
                <span class="kb-item-text">${itemText}</span>
                ${type !== 'keyword' ? `<span class="kb-item-type ${typeClass}">${typeText}</span>` : ''}
            </div>
            <button class="kb-delete-btn" data-index="${index}" data-type="${type}">删除</button>
        `;
    }

    // 添加屏蔽词
    function addKeywords(input) {
        const words = input.split(/[,，/]/)
            .map(word => word.replace(/\s+/g, ''))
            .filter(word => word.length > 0 && !BLOCK_KEYWORDS.includes(word));

        if (words.length > 0) {
            BLOCK_KEYWORDS.unshift(...words);
            saveKeywords(BLOCK_KEYWORDS);
            renderKeywordList();
            console.log('新增屏蔽词:', words);
            return true;
        }
        return false;
    }

    // 添加屏蔽用户ID
    function addUserIds(input) {
        const userId = extractUserIdFromInput(input, getCurrentSite());

        if (userId && !BLOCK_USER_IDS.includes(userId)) {
            BLOCK_USER_IDS.unshift(userId);
            saveUserIds(BLOCK_USER_IDS);
            renderUseridList();
            console.log('新增屏蔽用户ID:', userId);
            return true;
        } else {
            alert('无法从输入中提取有效的用户ID，请检查链接格式');
            return false;
        }
    }

    // 删除屏蔽词
    function removeKeyword(index) {
        if (index >= 0 && index < BLOCK_KEYWORDS.length) {
            const removed = BLOCK_KEYWORDS.splice(index, 1);
            saveKeywords(BLOCK_KEYWORDS);
            renderKeywordList();
            console.log('删除屏蔽词:', removed[0]);
            return true;
        }
        return false;
    }

    // 删除屏蔽用户ID
    function removeUserid(index) {
        if (index >= 0 && index < BLOCK_USER_IDS.length) {
            const removed = BLOCK_USER_IDS.splice(index, 1);
            saveUserIds(BLOCK_USER_IDS);
            renderUseridList();
            console.log('删除屏蔽用户ID:', removed[0]);
            return true;
        }
        return false;
    }

    // 检查备份提醒
    function checkBackupReminder() {
        const reminderElement = document.getElementById('kb-backup-reminder');
        if (!reminderElement) return;

        const lastBackup = localStorage.getItem(LAST_BACKUP_TIME_KEY);
        const now = Date.now();
        const oneWeek = 7 * 24 * 60 * 60 * 1000;

        if (!lastBackup || (now - parseInt(lastBackup)) > oneWeek) {
            reminderElement.classList.remove('hidden');
        } else {
            reminderElement.classList.add('hidden');
        }
    }

    // B站卡片屏蔽功能
    function initBilibiliCardBlock() {
        if (getCurrentSite() !== 'bilibili') return;

        updateBilibiliCardBlockStyle();
        setupBilibiliCardMutationObserver();
    }

    function updateBilibiliCardBlockStyle() {
        if (!bilibiliCardStyleElement) {
            bilibiliCardStyleElement = document.createElement('style');
            document.head.appendChild(bilibiliCardStyleElement);
        }

        if (BILIBILI_CARD_BLOCK_ENABLED) {
            bilibiliCardStyleElement.innerHTML = `${siteConfigs.bilibili.cardSelector} { display: none !important; }`;
        } else {
            bilibiliCardStyleElement.innerHTML = '';
        }
    }

    function setupBilibiliCardMutationObserver() {
        const observer = new MutationObserver(function(mutations) {
            let shouldUpdate = false;
            for (let mutation of mutations) {
                if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {
                    for (let node of mutation.addedNodes) {
                        if (node.nodeType === 1 && (
                            node.matches(siteConfigs.bilibili.cardSelector) ||
                            (node.querySelector && node.querySelector(siteConfigs.bilibili.cardSelector))
                        )) {
                            shouldUpdate = true;
                            break;
                        }
                    }
                }
                if (shouldUpdate) break;
            }
            if (shouldUpdate) {
                updateBilibiliCardBlockStyle();
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });

        observers.push(observer);
    }

    function toggleBilibiliCardBlock() {
        BILIBILI_CARD_BLOCK_ENABLED = !BILIBILI_CARD_BLOCK_ENABLED;
        saveBilibiliCardBlockState(BILIBILI_CARD_BLOCK_ENABLED);
        updateBilibiliCardBlockStyle();
        updateBilibiliCardTabUI();
        return BILIBILI_CARD_BLOCK_ENABLED;
    }

    // 知乎低赞屏蔽功能
    function initZhihuLowLikeBlock() {
        if (getCurrentSite() !== 'zhihu') return;

        // 初始处理
        processLowLikeContent();

        // 监听DOM变化
        const observer = new MutationObserver(function(mutations) {
            let shouldProcess = false;
            for (let mutation of mutations) {
                if (mutation.addedNodes.length > 0) {
                    for (let node of mutation.addedNodes) {
                        if (node.nodeType === 1 && (
                            node.matches(siteConfigs.zhihu.containerSelector) ||
                            (node.querySelector && node.querySelector(siteConfigs.zhihu.containerSelector))
                        )) {
                            shouldProcess = true;
                            break;
                        }
                    }
                }
                if (shouldProcess) break;
            }
            if (shouldProcess) {
                processLowLikeContent();
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });

        observers.push(observer);
    }

    function processLowLikeContent() {
        if (getCurrentSite() !== 'zhihu' || !ZHIHU_LOW_LIKE_SETTINGS.enabled) return;

        const config = siteConfigs.zhihu;
        const elements = document.querySelectorAll(config.containerSelector);

        elements.forEach(element => {
            const likeCountElement = element.querySelector(config.likeCountSelector);
            if (likeCountElement) {
                const likeCount = parseLikeCount(likeCountElement);

                if (likeCount !== null && likeCount < ZHIHU_LOW_LIKE_SETTINGS.threshold) {
                    const container = element.closest('.Card.TopstoryItem') || element;
                    container.remove();
                    console.log(`已屏蔽低赞内容，点赞数: ${likeCount}`);
                }
            }
        });
    }

    // 修复点赞数解析函数
    function parseLikeCount(likeElement) {
        if (!likeElement) return null;

        // 方法1: 从按钮的文本内容中提取
        let likeText = likeElement.textContent.trim();

        // 方法2: 从 aria-label 属性中提取
        if (!likeText || likeText === '赞同') {
            const ariaLabel = likeElement.getAttribute('aria-label');
            if (ariaLabel && ariaLabel.includes('赞同')) {
                likeText = ariaLabel;
            }
        }

        if (!likeText) return null;

        // 提取数字
        let likeCount = 0;

        // 处理"万"单位
        if (likeText.includes('万')) {
            const match = likeText.match(/(\d+(\.\d+)?)万/);
            if (match) {
                likeCount = Math.round(parseFloat(match[1]) * 10000);
            }
        } else {
            // 处理普通数字
            const match = likeText.match(/\d+/);
            if (match) {
                likeCount = parseInt(match[0]);
            }
        }

        return isNaN(likeCount) ? 0 : likeCount;
    }

    function toggleZhihuLowLikeBlock() {
        ZHIHU_LOW_LIKE_SETTINGS.enabled = !ZHIHU_LOW_LIKE_SETTINGS.enabled;
        saveLowLikeSettings(ZHIHU_LOW_LIKE_SETTINGS);

        if (ZHIHU_LOW_LIKE_SETTINGS.enabled) {
            processLowLikeContent();
        } else {
            // 如果关闭低赞屏蔽，重新显示所有内容
            const config = siteConfigs.zhihu;
            document.querySelectorAll(config.containerSelector).forEach(element => {
                element.style.display = '';
            });
        }

        updateZhihuLowLikeTabUI();
        return ZHIHU_LOW_LIKE_SETTINGS.enabled;
    }

    function updateLowLikeThreshold(threshold) {
        ZHIHU_LOW_LIKE_SETTINGS.threshold = threshold;
        saveLowLikeSettings(ZHIHU_LOW_LIKE_SETTINGS);

        if (ZHIHU_LOW_LIKE_SETTINGS.enabled) {
            processLowLikeContent();
        }
    }

    // 知乎文章屏蔽功能
    function initZhihuArticleBlock() {
        if (getCurrentSite() !== 'zhihu') return;

        if (ZHIHU_ARTICLE_BLOCK_ENABLED) {
            processZhihuArticles();
            setupZhihuArticleMutationObserver();
        }
    }

    function processZhihuArticles() {
        if (getCurrentSite() !== 'zhihu' || !ZHIHU_ARTICLE_BLOCK_ENABLED) return;

        const config = siteConfigs.zhihu;
        const articleElements = document.querySelectorAll(config.articleSelector);

        articleElements.forEach(article => {
            // 屏蔽所有文章
            const container = article.closest(config.saltContainerSelector) || article.closest('.TopstoryItem') || article;
            container.remove();
            console.log('已屏蔽知乎文章');
        });
    }

    function setupZhihuArticleMutationObserver() {
        const observer = new MutationObserver(function(mutations) {
            let shouldProcess = false;
            for (let mutation of mutations) {
                if (mutation.addedNodes.length > 0) {
                    for (let node of mutation.addedNodes) {
                        if (node.nodeType === 1 && (
                            node.matches(siteConfigs.zhihu.articleSelector) ||
                            (node.querySelector && node.querySelector(siteConfigs.zhihu.articleSelector))
                        )) {
                            shouldProcess = true;
                            break;
                        }
                    }
                }
                if (shouldProcess) break;
            }
            if (shouldProcess) {
                processZhihuArticles();
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });

        observers.push(observer);
    }

    // 知乎盐选屏蔽功能
    function initZhihuSaltBlock() {
        if (getCurrentSite() !== 'zhihu') return;

        updateZhihuSaltBlockStyle();
        setupZhihuSaltMutationObserver();
    }

    function updateZhihuSaltBlockStyle() {
        if (!zhihuSaltStyleElement) {
            zhihuSaltStyleElement = document.createElement('style');
            document.head.appendChild(zhihuSaltStyleElement);
        }

        if (ZHIHU_SALT_BLOCK_ENABLED) {
            const saltSelectors = siteConfigs.zhihu.saltSelectors.map(selector =>
                `${selector} { display: none !important; }`
            ).join('\n');

            const containerRules = `
                ${siteConfigs.zhihu.saltContainerSelector}:has(${siteConfigs.zhihu.saltSelectors.join(', ')}) {
                    display: none !important;
                }
            `;

            zhihuSaltStyleElement.innerHTML = saltSelectors + '\n' + containerRules;
        } else {
            zhihuSaltStyleElement.innerHTML = '';
        }
    }

    function setupZhihuSaltMutationObserver() {
        const observer = new MutationObserver(function(mutations) {
            let shouldUpdate = false;
            for (let mutation of mutations) {
                if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {
                    for (let node of mutation.addedNodes) {
                        if (node.nodeType === 1) {
                            // 检查是否是盐选内容
                            const isSaltContent = siteConfigs.zhihu.saltSelectors.some(selector =>
                                node.matches && node.matches(selector) ||
                                (node.querySelector && node.querySelector(selector))
                            );

                            if (isSaltContent) {
                                shouldUpdate = true;
                                break;
                            }
                        }
                    }
                }
                if (shouldUpdate) break;
            }
            if (shouldUpdate) {
                updateZhihuSaltBlockStyle();
                processZhihuSaltContent();
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });

        observers.push(observer);
    }

    function processZhihuSaltContent() {
        if (getCurrentSite() !== 'zhihu' || !ZHIHU_SALT_BLOCK_ENABLED) return;

        const config = siteConfigs.zhihu;

        // 移除盐选内容容器
        config.saltSelectors.forEach(selector => {
            document.querySelectorAll(selector).forEach(element => {
                const container = element.closest(config.saltContainerSelector);
                if (container) {
                    container.remove();
                    console.log('已屏蔽盐选内容');
                }
            });
        });
    }

    // 切换函数
    function toggleZhihuArticleBlock() {
        ZHIHU_ARTICLE_BLOCK_ENABLED = !ZHIHU_ARTICLE_BLOCK_ENABLED;
        saveZhihuArticleBlockState(ZHIHU_ARTICLE_BLOCK_ENABLED);

        if (ZHIHU_ARTICLE_BLOCK_ENABLED) {
            processZhihuArticles();
        }
        updateZhihuArticleTabUI();
        return ZHIHU_ARTICLE_BLOCK_ENABLED;
    }

    function toggleZhihuSaltBlock() {
        ZHIHU_SALT_BLOCK_ENABLED = !ZHIHU_SALT_BLOCK_ENABLED;
        saveZhihuSaltBlockState(ZHIHU_SALT_BLOCK_ENABLED);
        updateZhihuSaltBlockStyle();

        if (ZHIHU_SALT_BLOCK_ENABLED) {
            processZhihuSaltContent();
        }
        updateZhihuSaltTabUI();
        return ZHIHU_SALT_BLOCK_ENABLED;
    }

    // 更新B站卡片屏蔽选项卡的UI
    function updateBilibiliCardTabUI() {
        if (getCurrentSite() !== 'bilibili') return;

        const toggleBtn = document.getElementById('kb-toggle-card-block');
        const statusIndicator = document.querySelector('#kb-tab-cards .kb-status-indicator');

        if (toggleBtn && statusIndicator) {
            if (BILIBILI_CARD_BLOCK_ENABLED) {
                toggleBtn.textContent = '已启用 - 点击关闭';
                toggleBtn.classList.remove('disabled');
                statusIndicator.classList.remove('kb-status-disabled');
                statusIndicator.classList.add('kb-status-enabled');
            } else {
                toggleBtn.textContent = '已关闭 - 点击启用';
                toggleBtn.classList.add('disabled');
                statusIndicator.classList.remove('kb-status-enabled');
                statusIndicator.classList.add('kb-status-disabled');
            }
        }
    }

    // 更新知乎低赞屏蔽选项卡的UI
    function updateZhihuLowLikeTabUI() {
        if (getCurrentSite() !== 'zhihu') return;

        const toggleBtn = document.getElementById('kb-toggle-low-like');
        const statusIndicator = document.querySelector('#kb-tab-zhihu-advanced .kb-zhihu-feature:first-child .kb-status-indicator');

        if (toggleBtn && statusIndicator) {
            if (ZHIHU_LOW_LIKE_SETTINGS.enabled) {
                toggleBtn.textContent = '已启用 - 点击关闭';
                toggleBtn.classList.remove('disabled');
                statusIndicator.classList.remove('kb-status-disabled');
                statusIndicator.classList.add('kb-status-enabled');
            } else {
                toggleBtn.textContent = '已关闭 - 点击启用';
                toggleBtn.classList.add('disabled');
                statusIndicator.classList.remove('kb-status-enabled');
                statusIndicator.classList.add('kb-status-disabled');
            }
        }
    }

    // 更新知乎文章屏蔽选项卡的UI
    function updateZhihuArticleTabUI() {
        if (getCurrentSite() !== 'zhihu') return;

        const toggleBtn = document.getElementById('kb-toggle-article-block');
        const statusIndicator = document.querySelector('#kb-tab-zhihu-advanced .kb-zhihu-feature:nth-child(2) .kb-status-indicator');

        if (toggleBtn && statusIndicator) {
            if (ZHIHU_ARTICLE_BLOCK_ENABLED) {
                toggleBtn.textContent = '已启用 - 点击关闭';
                toggleBtn.classList.remove('disabled');
                statusIndicator.classList.remove('kb-status-disabled');
                statusIndicator.classList.add('kb-status-enabled');
            } else {
                toggleBtn.textContent = '已关闭 - 点击启用';
                toggleBtn.classList.add('disabled');
                statusIndicator.classList.remove('kb-status-enabled');
                statusIndicator.classList.add('kb-status-disabled');
            }
        }
    }

    // 更新知乎盐选屏蔽选项卡的UI
    function updateZhihuSaltTabUI() {
        if (getCurrentSite() !== 'zhihu') return;

        const toggleBtn = document.getElementById('kb-toggle-salt-block');
        const statusIndicator = document.querySelector('#kb-tab-zhihu-advanced .kb-zhihu-feature:last-child .kb-status-indicator');

        if (toggleBtn && statusIndicator) {
            if (ZHIHU_SALT_BLOCK_ENABLED) {
                toggleBtn.textContent = '已启用 - 点击关闭';
                toggleBtn.classList.remove('disabled');
                statusIndicator.classList.remove('kb-status-disabled');
                statusIndicator.classList.add('kb-status-enabled');
            } else {
                toggleBtn.textContent = '已关闭 - 点击启用';
                toggleBtn.classList.add('disabled');
                statusIndicator.classList.remove('kb-status-enabled');
                statusIndicator.classList.add('kb-status-disabled');
            }
        }
    }

    // 更新统计显示
    function updateStatsDisplay() {
        document.getElementById('stat-total').textContent = stats.totalBlocked;
        document.getElementById('stat-keyword').textContent = stats.keywordBlocked;
        document.getElementById('stat-userid').textContent = stats.useridBlocked;
        document.getElementById('stat-startup').textContent = stats.startupCount;
        document.getElementById('stat-last-time').textContent =
            stats.lastBlockTime ? new Date(stats.lastBlockTime).toLocaleString() : '暂无';
    }

    // 重置统计
    function resetStats() {
        stats = {...DEFAULT_STATS, startupCount: stats.startupCount};
        saveStats();
        updateStatsDisplay();
    }

    // 更新屏蔽统计
    function updateStats(type) {
        stats.totalBlocked++;
        stats.lastBlockTime = Date.now();

        if (type === 'keyword') stats.keywordBlocked++;
        if (type === 'userid') stats.useridBlocked++;

        saveStats();
        updateStatsDisplay();
    }

    // 添加导入导出功能函数
    function setupImportExport() {
        const exportBtn = document.getElementById('kb-export-btn');
        const importBtn = document.getElementById('kb-import-btn');
        const importFile = document.getElementById('kb-import-file');

        // 导出功能
        exportBtn.addEventListener('click', () => {
            const currentSite = getCurrentSite();
            const siteNames = {
                'zhihu': '知乎',
                'xiaohongshu': '小红书',
                'bilibili': 'B站',
                'weibo': '微博'
            };
            const sourceName = siteNames[currentSite] || currentSite;

            const exportData = {
                keywords: BLOCK_KEYWORDS,
                userIds: BLOCK_USER_IDS,
                lowLikeSettings: ZHIHU_LOW_LIKE_SETTINGS,
                bilibiliCardBlock: BILIBILI_CARD_BLOCK_ENABLED,
                zhihuArticleBlock: ZHIHU_ARTICLE_BLOCK_ENABLED,
                zhihuSaltBlock: ZHIHU_SALT_BLOCK_ENABLED,
                stats: stats,
                exportTime: new Date().toISOString(),
                version: '1.3.1'
            };

            const dataStr = JSON.stringify(exportData, null, 2);
            const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);

            const exportFileDefaultName = `${sourceName}_屏蔽数据_${new Date().toLocaleDateString().replace(/\//g, '-')}.json`;

            const linkElement = document.createElement('a');
            linkElement.setAttribute('href', dataUri);
            linkElement.setAttribute('download', exportFileDefaultName);
            linkElement.click();

            // 更新最后备份时间
            localStorage.setItem(LAST_BACKUP_TIME_KEY, Date.now().toString());
            checkBackupReminder();

            console.log(`${sourceName}屏蔽数据已导出`);
        });

        // 导入功能
        importBtn.addEventListener('click', () => {
            importFile.click();
        });

        importFile.addEventListener('change', (e) => {
            const file = e.target.files[0];
            if (!file) return;

            const reader = new FileReader();
            reader.onload = (event) => {
                try {
                    const importedData = JSON.parse(event.target.result);

                    if (!Array.isArray(importedData.keywords) || !Array.isArray(importedData.userIds)) {
                        throw new Error('文件格式不正确，必须是包含keywords和userIds数组的JSON文件');
                    }

                    const validKeywords = importedData.keywords.filter(k => typeof k === 'string');
                    const validUserIds = importedData.userIds.filter(u => typeof u === 'string');

                    if (validKeywords.length === 0 && validUserIds.length === 0) {
                        throw new Error('文件中没有有效的屏蔽数据');
                    }

                    const keywordMsg = validKeywords.length > 0 ? `${validKeywords.length} 个屏蔽词` : '';
                    const useridMsg = validUserIds.length > 0 ? `${validUserIds.length} 个屏蔽用户ID` : '';

                    const msgParts = [keywordMsg, useridMsg].filter(msg => msg);
                    const confirmMsg = `确定要导入 ${msgParts.join('、')} 吗？\n这将替换当前的屏蔽数据。`;

                    if (confirm(confirmMsg)) {
                        BLOCK_KEYWORDS = [...validKeywords];
                        BLOCK_USER_IDS = [...validUserIds];

                        // 导入低赞设置（如果存在）
                        if (importedData.lowLikeSettings) {
                            ZHIHU_LOW_LIKE_SETTINGS = {...importedData.lowLikeSettings};
                            saveLowLikeSettings(ZHIHU_LOW_LIKE_SETTINGS);
                        }

                        // 导入B站卡片设置（如果存在）
                        if (importedData.bilibiliCardBlock !== undefined) {
                            BILIBILI_CARD_BLOCK_ENABLED = importedData.bilibiliCardBlock;
                            saveBilibiliCardBlockState(BILIBILI_CARD_BLOCK_ENABLED);
                        }

                        // 导入知乎文章屏蔽设置（如果存在）
                        if (importedData.zhihuArticleBlock !== undefined) {
                            ZHIHU_ARTICLE_BLOCK_ENABLED = importedData.zhihuArticleBlock;
                            saveZhihuArticleBlockState(ZHIHU_ARTICLE_BLOCK_ENABLED);
                        }

                        // 导入知乎盐选屏蔽设置（如果存在）
                        if (importedData.zhihuSaltBlock !== undefined) {
                            ZHIHU_SALT_BLOCK_ENABLED = importedData.zhihuSaltBlock;
                            saveZhihuSaltBlockState(ZHIHU_SALT_BLOCK_ENABLED);
                        }

                        // 导入统计（如果存在）
                        if (importedData.stats) {
                            stats = {...importedData.stats};
                            saveStats();
                        }

                        saveKeywords(BLOCK_KEYWORDS);
                        saveUserIds(BLOCK_USER_IDS);
                        renderKeywordList();
                        renderUseridList();

                        // 更新UI
                        updateBilibiliCardTabUI();
                        updateZhihuLowLikeTabUI();
                        updateZhihuArticleTabUI();
                        updateZhihuSaltTabUI();
                        updateStatsDisplay();
                        checkBackupReminder();

                        alert('屏蔽数据导入成功！');

                        processAllContent();
                    }
                } catch (error) {
                    alert('导入失败: ' + error.message);
                }

                e.target.value = '';
            };

            reader.readAsText(file);
        });
    }

    // 安全处理内容元素（添加错误处理）
    function safeProcessContentElement(element, config) {
        try {
            processContentElement(element, config);
        } catch (error) {
            console.error('处理内容元素时出错:', error, element);
        }
    }

    // 处理单个内容元素（只扫描标题，不扫描整个内容）
    function processContentElement(element, config) {
        const site = getCurrentSite();

        // B站特殊处理：检测反屏蔽提示并删除
        if (site === 'bilibili') {
            if (element.classList.contains('bili-video-card') &&
                element.classList.contains('is-rcmd') &&
                !element.classList.contains('enable-no-interest')) {

                let targetContainer = element.closest('.feed-card') || element.closest('.bili-feed-card');

                if (targetContainer) {
                    targetContainer.remove();
                    console.log(`${config.logPrefix}: 反屏蔽提示 (删除整个容器)`);
                } else {
                    element.remove();
                    console.log(`${config.logPrefix}: 反屏蔽提示 (删除元素)`);
                }
                return;
            }
        }

        // 知乎文章和盐选屏蔽
        if (site === 'zhihu') {
            // 文章屏蔽
            if (ZHIHU_ARTICLE_BLOCK_ENABLED) {
                const articleElement = element.querySelector(config.articleSelector);
                if (articleElement) {
                    const container = element.closest(config.saltContainerSelector) || element.closest('.TopstoryItem') || element;
                    container.remove();
                    console.log('已屏蔽知乎文章');
                    return;
                }
            }

            // 盐选屏蔽
            if (ZHIHU_SALT_BLOCK_ENABLED) {
                const isSaltContent = config.saltSelectors.some(selector =>
                    element.querySelector(selector)
                );

                if (isSaltContent) {
                    const container = element.closest(config.saltContainerSelector) || element;
                    container.remove();
                    console.log('已屏蔽盐选内容');
                    return;
                }
            }
        }

        // 只从标题元素获取文本，不扫描整个内容
        const titleElement = element.querySelector(config.titleSelector);
        let title = '';

        if (titleElement) {
            title = titleElement.textContent.trim();
        }
        // 如果没有找到标题元素，直接跳过关键词检查，只检查用户ID
        // 这样避免了扫描整个内容文本

        // 检查用户信息
        let userId = '';
        const userIdElement = element.querySelector(config.userIdSelector);

        // 获取用户ID
        if (userIdElement && config.extractUserId) {
            const extractedId = config.extractUserId(userIdElement);
            if (extractedId) {
                userId = extractedId;
            }
        }

        // 如果userElement有href属性，也尝试提取用户ID
        const userElement = element.querySelector(config.userSelector);
        if (userElement && userElement.href && config.extractUserId && !userId) {
            const extractedId = config.extractUserId(userElement);
            if (extractedId) {
                userId = extractedId;
            }
        }

        // 检查是否包含屏蔽关键词（只在标题中检查）
        const hasBlockedKeyword = title && BLOCK_KEYWORDS.some(keyword => title.includes(keyword));

        // 检查是否包含屏蔽用户ID（精确匹配）
        const hasBlockedUserid = BLOCK_USER_IDS.some(userid =>
            userId && userId === userid
        );

        if (hasBlockedKeyword || hasBlockedUserid) {
            let containerRemoved = false;
            let logMessage = '';

            if (hasBlockedKeyword) {
                logMessage = `${config.logPrefix}: ${title}`;
                updateStats('keyword');
            } else if (hasBlockedUserid) {
                logMessage = `${config.userLogPrefix}(ID): ${userId} (内容: ${title || '无标题'})`;
                updateStats('userid');
            }

            // 针对不同网站的特殊处理：删除整个容器
            if (site === 'zhihu') {
                let cardElement = element.closest('.Card.TopstoryItem.TopstoryItem-isRecommend');
                if (cardElement) {
                    cardElement.remove();
                    console.log(`${logMessage} (删除整个卡片)`);
                    containerRemoved = true;
                }
            } else if (site === 'bilibili') {
                let feedCardElement = element.closest('.feed-card') || element.closest('.bili-feed-card');
                if (feedCardElement) {
                    feedCardElement.remove();
                    console.log(`${logMessage} (删除整个B站容器)`);
                    containerRemoved = true;
                }
            } else if (site === 'xiaohongshu') {
                let noteItemElement = element.closest('.note-item');
                if (noteItemElement) {
                    noteItemElement.remove();
                    console.log(`${logMessage} (删除整个note-item)`);
                    containerRemoved = true;
                }
            }

            // 如果没找到特定容器或其他网站，使用原来的隐藏方式
            if (!containerRemoved) {
                element.style.display = 'none';
                console.log(`${logMessage} (隐藏元素)`);
            }
        }
    }

    // 处理所有内容元素（使用缓存优化）
    function processAllContent() {
        const site = getCurrentSite();
        const config = siteConfigs[site];

        if (!config) {
            console.log('未支持的网站:', window.location.hostname);
            return;
        }

        // 使用缓存优化
        if (!cachedElements.has(config.containerSelector)) {
            cachedElements.set(config.containerSelector,
                document.querySelectorAll(config.containerSelector));
        }

        const elements = cachedElements.get(config.containerSelector);
        elements.forEach(element => {
            if (element.isConnected) { // 确保元素仍在DOM中
                safeProcessContentElement(element, config);
            }
        });

        // 定期清除缓存
        setTimeout(() => cachedElements.clear(), 5000);
    }

    // 防抖处理函数
    function debounce(func, wait) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func(...args), wait);
        };
    }

    const debouncedProcessAllContent = debounce(processAllContent, 500);

    // 初始化键盘快捷键
    function initKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            // Ctrl+Shift+B 打开/关闭屏蔽面板
            if (e.ctrlKey && e.shiftKey && e.key === 'B') {
                e.preventDefault();
                const panel = document.getElementById('keyword-blocker-panel');
                const toggleBtn = document.getElementById('keyword-blocker-toggle');
                const isShowing = panel.classList.contains('show');

                if (isShowing) {
                    panel.classList.remove('show');
                    toggleBtn.style.display = 'block';
                } else {
                    panel.classList.add('show');
                    toggleBtn.style.display = 'none';
                }
            }

            // ESC 关闭面板
            if (e.key === 'Escape') {
                const panel = document.getElementById('keyword-blocker-panel');
                const toggleBtn = document.getElementById('keyword-blocker-toggle');
                panel.classList.remove('show');
                toggleBtn.style.display = 'block';
            }
        });
    }

    // 清理函数（防止内存泄漏）
    function cleanup() {
        observers.forEach(observer => observer.disconnect());
        observers = [];
        cachedElements.clear();

        // 清理动态添加的样式
        if (bilibiliCardStyleElement) {
            bilibiliCardStyleElement.remove();
        }
        if (zhihuSaltStyleElement) {
            zhihuSaltStyleElement.remove();
        }
    }

    // 初始化UI事件
    function initUIEvents() {
        const toggleBtn = document.getElementById('keyword-blocker-toggle');
        const panel = document.getElementById('keyword-blocker-panel');
        const closeBtn = document.getElementById('kb-close');
        const addKeywordBtn = document.getElementById('kb-add-keyword-btn');
        const addUseridBtn = document.getElementById('kb-add-userid-btn');
        const keywordInput = document.getElementById('kb-keyword-input');
        const useridInput = document.getElementById('kb-userid-input');
        const keywordList = document.getElementById('kb-keyword-list');
        const useridList = document.getElementById('kb-userid-list');
        const disableSiteBtn = document.getElementById('kb-disable-site');
        const tabs = document.querySelectorAll('.kb-tab');
        const cardToggleBtn = document.getElementById('kb-toggle-card-block');
        const lowLikeToggleBtn = document.getElementById('kb-toggle-low-like');
        const lowLikeThresholdInput = document.getElementById('kb-low-like-threshold');
        const articleToggleBtn = document.getElementById('kb-toggle-article-block');
        const saltToggleBtn = document.getElementById('kb-toggle-salt-block');
        const resetStatsBtn = document.getElementById('kb-reset-stats');

        // 标签页切换
        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                const tabName = tab.dataset.tab;

                tabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');

                document.querySelectorAll('.kb-tab-content').forEach(content => {
                    content.classList.remove('active');
                });
                document.getElementById(`kb-tab-${tabName}`).classList.add('active');
            });
        });

        // 切换面板显示
        toggleBtn.addEventListener('click', () => {
            const isShowing = panel.classList.contains('show');
            if (isShowing) {
                panel.classList.remove('show');
                toggleBtn.style.display = 'block';
            } else {
                panel.classList.add('show');
                toggleBtn.style.display = 'none';
            }
        });

        // 关闭面板函数
        function closePanel() {
            panel.classList.remove('show');
            toggleBtn.style.display = 'block';
        }

        // 关闭面板
        closeBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            closePanel();
        });

        // 点击面板外区域关闭
        document.addEventListener('click', (e) => {
            if (!panel.contains(e.target) && !toggleBtn.contains(e.target)) {
                closePanel();
            }
        });

        // 新增屏蔽词
        addKeywordBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            const value = keywordInput.value.trim();
            if (value) {
                if (addKeywords(value)) {
                    keywordInput.value = '';
                } else {
                    alert('请输入有效的屏蔽词');
                }
            }
        });

        // 新增屏蔽用户ID
        addUseridBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            const value = useridInput.value.trim();
            if (value) {
                if (addUserIds(value)) {
                    useridInput.value = '';
                }
            }
        });

        // 回车新增
        keywordInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                addKeywordBtn.click();
            }
        });

        useridInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                addUseridBtn.click();
            }
        });

        // 处理删除相关事件
        function setupListEvents(listElement, type) {
            listElement.addEventListener('click', (e) => {
                const index = parseInt(e.target.dataset.index);
                const itemType = e.target.dataset.type;

                if (e.target.classList.contains('kb-delete-btn') && itemType === type) {
                    e.stopPropagation();
                    const listItem = e.target.closest('.kb-list-item');
                    showDeleteConfirm(listItem, index, type);
                } else if (e.target.classList.contains('kb-confirm-delete') && itemType === type) {
                    e.stopPropagation();
                    if (type === 'keyword') {
                        removeKeyword(index);
                    } else if (type === 'userid') {
                        removeUserid(index);
                    }
                } else if (e.target.classList.contains('kb-confirm-cancel') && itemType === type) {
                    e.stopPropagation();
                    const listItem = e.target.closest('.kb-list-item');
                    restoreNormalView(listItem, index, type);
                }
            });
        }

        setupListEvents(keywordList, 'keyword');
        setupListEvents(useridList, 'userid');

        // B站卡片屏蔽开关
        if (cardToggleBtn) {
            cardToggleBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleBilibiliCardBlock();
            });
        }

        // 知乎低赞屏蔽开关
        if (lowLikeToggleBtn) {
            lowLikeToggleBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleZhihuLowLikeBlock();
            });
        }

        // 知乎低赞阈值设置
        if (lowLikeThresholdInput) {
            lowLikeThresholdInput.addEventListener('change', (e) => {
                const threshold = parseInt(e.target.value);
                if (isNaN(threshold) || threshold < 0) {
                    alert('请输入有效的阈值（非负整数）');
                    e.target.value = ZHIHU_LOW_LIKE_SETTINGS.threshold;
                    return;
                }
                updateLowLikeThreshold(threshold);
            });
        }

        // 知乎文章屏蔽开关
        if (articleToggleBtn) {
            articleToggleBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleZhihuArticleBlock();
            });
        }

        // 知乎盐选屏蔽开关
        if (saltToggleBtn) {
            saltToggleBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleZhihuSaltBlock();
            });
        }

        // 重置统计
        if (resetStatsBtn) {
            resetStatsBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                if (confirm('确定要重置所有统计信息吗？')) {
                    resetStats();
                }
            });
        }

        // 禁用/启用当前网站
        disableSiteBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            const currentSite = getCurrentSite();
            const siteNames = {
                'zhihu': '知乎',
                'xiaohongshu': '小红书',
                'bilibili': 'B站',
                'weibo': '微博'
            };
            const siteName = siteNames[currentSite] || '当前网站';
            const isCurrentlyDisabled = isCurrentSiteDisabled();

            if (isCurrentlyDisabled) {
                if (confirm(`确定要重新启用在${siteName}的屏蔽功能吗？\n\n启用后，屏蔽功能将在下次刷新页面时生效。`)) {
                    enableCurrentSite();
                    alert(`已重新启用在${siteName}的屏蔽功能。\n请刷新页面使设置生效。`);
                    closePanel();
                }
            } else {
                if (confirm(`确定要停止在${siteName}的屏蔽功能吗？\n\n下次访问${siteName}时，屏蔽功能将不会生效。`)) {
                    disableCurrentSite();
                    alert(`已停止在${siteName}的屏蔽功能。\n请刷新页面使设置生效。`);
                    closePanel();
                }
            }
        });

        // 设置导入导出功能
        setupImportExport();
    }

    // 初始化管理UI
    function initManagementUI() {
        createManagementUI();
        renderKeywordList();
        renderUseridList();
        initUIEvents();
        initKeyboardShortcuts();
        updateStatsDisplay();
        checkBackupReminder();

        // 初始化用户ID屏蔽提示
        updateUseridInputHint();

        // 如果是B站，初始化卡片屏蔽功能
        if (getCurrentSite() === 'bilibili') {
            initBilibiliCardBlock();
        }

        // 如果是知乎，初始化低赞屏蔽、文章屏蔽和盐选屏蔽功能
        if (getCurrentSite() === 'zhihu') {
            initZhihuLowLikeBlock();
            initZhihuArticleBlock();
            initZhihuSaltBlock();
        }

        // 添加清理监听
        window.addEventListener('beforeunload', cleanup);
    }

    // 主初始化函数
    function init() {
        // 加载统计
        stats = loadStats();
        saveStats();

        // 检查当前网站是否被禁用
        if (isCurrentSiteDisabled()) {
            console.log(`四平台关键词和用户屏蔽器: ${getCurrentSite()} 已被禁用，仅显示管理界面`);
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', initManagementUI);
            } else {
                initManagementUI();
            }
            return;
        }

        // 初始化处理
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => {
                processAllContent();
                initManagementUI();
            });
        } else {
            processAllContent();
            initManagementUI();
        }
        window.addEventListener('load', processAllContent);

        // 监听DOM变化
        const observer = new MutationObserver(mutations => {
            const site = getCurrentSite();
            const config = siteConfigs[site];
            if (!config) return;

            let shouldProcess = false;
            mutations.forEach(mutation => {
                if (mutation.addedNodes.length) {
                    mutation.addedNodes.forEach(node => {
                        if (node.nodeType === 1) {
                            if (node.matches && node.matches(config.containerSelector)) {
                                safeProcessContentElement(node, config);
                            } else if (node.querySelectorAll) {
                                const elements = node.querySelectorAll(config.containerSelector);
                                if (elements.length > 0) {
                                    shouldProcess = true;
                                    elements.forEach(element => safeProcessContentElement(element, config));
                                }
                            }
                        }
                    });
                }
            });

            if (shouldProcess) debouncedProcessAllContent();
        });

        observer.observe(document.body, { childList: true, subtree: true });
        observers.push(observer);

        // 滚动事件监听
        let scrollTimeout;
        window.addEventListener('scroll', () => {
            clearTimeout(scrollTimeout);
            scrollTimeout = setTimeout(debouncedProcessAllContent, 1000);
        }, { passive: true });

        // 定时扫描
        setInterval(processAllContent, 5000);

        console.log(`四平台关键词和用户屏蔽器已启动，当前网站: ${getCurrentSite()}`);
    }

    // 启动脚本
    init();
})();