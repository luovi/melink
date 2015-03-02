define(function(){
    // GA
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-33206214-1']);
    _gaq.push(['_addOrganic', 'baidu', 'word']);
    _gaq.push(['_addOrganic', 'soso', 'w']);
    _gaq.push(['_addOrganic', 'youdao', 'q']);
    _gaq.push(['_addOrganic', 'sogou', 'query']);
    _gaq.push(['_addOrganic', 'bing', 'q']);
    _gaq.push(['_addOrganic', '360', 'q']);
    _gaq.push(['_addOrganic', 'so.com', 'q']);
    _gaq.push(['_trackPageview']);
    _gaq.push(['_trackPageLoadTime']);
    (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();
    // 百度统计
    var _hmt = _hmt || [];
    var bdhm = document.createElement('script'); bdhm.type = 'text/javascript'; bdhm.async = true;
    bdhm.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'hm.baidu.com/h.js?895b01ff241d03784ef8124233214748';
    var t = document.getElementsByTagName('script')[0]; t.parentNode.insertBefore(bdhm, t);
    _hmt.push(['_trackPageview', '/mytest3'])
    return {'google':_gaq, 'baidu':_hmt}
})
