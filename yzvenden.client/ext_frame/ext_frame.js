(function(){
    /*系统初始化*/
    if(typeof Ext == 'undefined'){
        //未引入Ext的配置，自动化引入
        var scriptEls = document.getElementsByTagName("script"),
            path = scriptEls[scriptEls.length - 1].src,
            path = path.substring(0, path.lastIndexOf('/'));
        document.write('<script type="text/javascript" src = "'+path+'/extjs/include-ext.js?theme=classic&debug=true"></script>');
        document.write('<script type="text/javascript" src = "'+path+'/jquery-3.1.0.min.js"></script>');
        document.write('<script type="text/javascript" src = "'+path+'/EConfig.js"></script>');
        document.write('<script type="text/javascript" src = "'+path+'/E.js"></script>');
        document.write('<link rel="stylesheet" type="text/css" href="'+path+'/ext_frame.css">');
    }
})();

