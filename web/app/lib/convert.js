(function() {
  var __slice = [].slice;

  define(['jquery'], function($) {
    'use strict';
    var cache, convertLimit, convertPromise, convertUrl, convertUrlParm, dataType, defaults, generatePointsParam, slicePoins;
    defaults = {
      debug: false,
      round: 0,
      encodeURI: false
    };
    dataType = 'jsonp';
    convertUrl = 'http://api.map.baidu.com/geoconv/v1/';
    convertUrlParm = '?from=1&to=5&output=json&ak=EF6aee05088be9901b4b11fcab5da1d1';
    convertLimit = 20;
    cache = true;
    slicePoins = function(points) {
      var cur, len, max, min, num, results;
      if ((len = points.length) <= 20) {
        return [points];
      }
      results = [];
      num = Math.ceil(len / convertLimit);
      cur = 0;
      while (cur !== num) {
        min = cur * convertLimit;
        cur++;
        max = cur * convertLimit;
        results.push(points.slice(min, max));
      }
      return results;
    };
    generatePointsParam = function(items, options) {
      var item, latitude, listA, listB, longitude, x, y, _i, _len, _ref;
      listA = [];
      listB = [];
      _ref = items.models;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        item = _ref[_i];
        longitude = item.longitude, latitude = item.latitude;
        if (!!options.round) {
          longitude = parseFloat(longitude).toFixed(options.round);
          latitude = parseFloat(latitude).toFixed(options.round);
        }
        listA.push(longitude);
        listB.push(latitude);
      }
      x = listA.join(',');
      y = listB.join(',');
      if (options.encodeURI) {
        x = encodeURIComponent(x);
        y = encodeURIComponent(y);
      }
      return "&coords=" + x + "," + y;
    };
    return convertPromise = function(points, options) {
      var defer, requests;
      if (options == null) {
        options = {};
      }
      defer = $.Deferred();
      if (!points.length) {
        return defer.reject();
      }
      options = $.extend(defaults, options);
      requests = slicePoins(points).map(function(items) {
        var url;
        url = convertUrl + convertUrlParm + generatePointsParam(items, options);
        return $.ajax({
          url: url,
          dataType: dataType,
          cache: cache
        });
      });
      $.when.apply($, requests).then(function() {
        var index, point, responses, results, x, y, _i, _len, _ref;
        responses = 1 <= arguments.length ? __slice.call(arguments, 0) : [];
        results = [];
        if (requests.length === 1 && responses.length === 3) {
          responses = [responses];
        }
        if (options.debug) {
          if (typeof console !== "undefined" && console !== null) {
            console.debug(+(new Date), 'all responses done.', responses);
          }
        }
        responses.forEach(function(response) {
          return results = results.concat(response[0]);
        });
        if (results.length !== points.length) {
          if (typeof console !== "undefined" && console !== null) {
            console.error(+(new Date), 'results.length isnt points.length', results.length, points.length);
          }
        }
        for (index = _i = 0, _len = points.length; _i < _len; index = ++_i) {
          point = points[index];
          _ref = results[index], x = _ref.x, y = _ref.y;
          if (!(x && y)) {
            if (typeof console !== "undefined" && console !== null) {
              console.error('unless x, y?', results[index], point);
            }
            continue;
          }
          point.longitude = x;
          point.latitude = y;
        }
        return defer.resolve(points);
      });
      return defer.promise();
    };
  });

}).call(this);
