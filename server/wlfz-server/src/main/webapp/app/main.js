(function() {
  require.config({
    shim: {
      jquery: {
        exports: '$'
      },
      underscore: {
        exports: '_'
      },
      json2: {
        exports: 'json2'
      },
      '_.str': {
        deps: ['underscore'],
        exports: '_.str'
      },
      backbone: {
        deps: ['_.str', 'jquery'],
        exports: 'Backbone'
      },
      store: {
        deps: ['json2'],
        exports: 'store'
      },
      zclip: {
        deps: ['jquery'],
        exports: 'zclip'
      },
      popover: {
        deps: ['jquery', 'tooltip'],
        exports: 'popover'
      }
    },
    paths: {
      'jquery': 'lib/jquery.min',
      'underscore': 'lib/underscore',
      '_.str': 'lib/underscore.string.min',
      'backbone': 'lib/backbone',
      'text': 'lib/text',
      'base64': 'lib/base64',
      'store': 'lib/store',
      'json2': 'lib/json2',
      'spin': 'lib/spin.min',
      'notify': 'lib/notify',
      'tagging': 'lib/tagging',
      'zclip': 'lib/jquery.zclip.min',
      'popover': 'lib/popover',
      'tooltip': 'lib/tooltip',
      'dropMenu': 'lib/dropMenu',
      'bmaps': 'lib/bmaps',
      'modal': 'lib/modal',
      'city': 'lib/city',
      'datepicker': 'lib/datepicker',
      'notify_modal': 'lib/notify_modal',
      'tagging': 'lib/tagging',
      'bmaps': 'lib/bmaps',
      'car_filter': 'lib/car_filter',
      'cargoAutoFill': 'lib/cargoAutoFill',
      'cars_filter': 'lib/cars_filter',
      'notify': 'lib/notify',
      'slide': 'lib/slide',
      'common': 'utils/common',
      'filter': 'utils/filter',
      'validator': 'utils/validator',
      'helpers': 'utils/helpers',
      'bmapapi': "http://api.map.baidu.com/getscript?v=2.0&ak=EF6aee05088be9901b4b11fcab5da1d1&services=&t=" + ((new Date).getTime())
    }
  });

  require(['backbone', 'routers/router', 'helpers'], function(Backbone, AppRouter, helpers) {
    'use strict';
    $(function() {
      var router;
      router = new AppRouter($('body'));
      window.oldURL = window.location.href;
      $(window).on("hashchange", router.hasChange());
      if ($.browser.msie && $.browser.version < 7) {
        window.isie6 = true;
      } else {
        window.isie6 = false;
      }
      return Backbone.history.start();
    });
    return this;
  });

}).call(this);
