(function() {
  define(['underscore', 'common'], function(_, common) {
    'use strict';
    return {
      formatDate: function(date, newvalue) {
        var d, h, m, month, now, s, seconds, year;
        if (newvalue == null) {
          newvalue = '';
        }
        if (_.isNaN(date) || _.isNull(date) || date === '') {
          return newvalue;
        }
        date = common.parseDate(date);
        if (!date) {
          return newvalue;
        }
        now = new Date();
        year = now.getFullYear() - date.getFullYear();
        month = now.getMonth() - date.getMonth();
        seconds = Math.round((now - date) / 1000);
        d = Math.floor(seconds / (3600 * 24));
        h = Math.floor(seconds / 3600);
        if (year > 1 || (year === 1 && month > 0)) {
          return "" + year + "年前";
        } else if ((year === 1 && month < 0) || (month > 0 && d > 30)) {
          m = month < 0 ? 12 + month : month;
          return "" + m + "个月前";
        } else if (d > 0) {
          return "" + d + "天前";
        } else if (h > 0) {
          return "" + h + "小时前";
        } else {
          s = Math.floor(seconds / 60);
          if (s > 1) {
            return "" + s + "分钟前";
          } else {
            return "刚刚";
          }
        }
      },
      formatCity: function(city) {
        var _ref, _ref1;
        if ((_ref = city.slice(-1)) === '市' || _ref === '省' || _ref === '盟') {
          return city.slice(0, -1);
        } else if ((_ref1 = city.slice(-3)) === '自治区' || _ref1 === '直辖市') {
          return city.slice(0, -3);
        }
      }
    };
  });

}).call(this);
