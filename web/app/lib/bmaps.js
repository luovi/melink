(function() {
  var __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  define(['jquery', 'underscore', 'bmapapi'], function($, _, bmapapi) {
    'use strict';
    var Ak, CUR_IMG, Convert, FollowMarker, Map;
    Ak = 'EF6aee05088be9901b4b11fcab5da1d1';
    CUR_IMG = "/static/images/transparent.cur";
    Convert = (function() {
      /* 将gps坐标转换为百度坐标
      http://developer.baidu.com/map/changeposition.htm
      */

      function Convert() {
        this.urlRoot = 'http://api.map.baidu.com/geoconv/v1/';
        this.options = {
          from: 1,
          to: 5,
          output: 'jsonp',
          ak: Ak
        };
      }

      Convert.prototype.fetch = function(cols) {
        var coords, defer, i, _i, _j, _len, _len1, _ref;
        defer = $.Deferred();
        if (!cols.length) {
          return defer.reject();
        }
        coords = [];
        if (cols.models) {
          _ref = cols.models;
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            i = _ref[_i];
            coords.push("" + (i.get('longitude')) + "," + (i.get('latitude')));
            _.extend(this.options, {
              coords: coords.join(';')
            });
          }
        } else {
          for (_j = 0, _len1 = cols.length; _j < _len1; _j++) {
            i = cols[_j];
            coords.push("" + i.longitude + "," + i.latitude);
            _.extend(this.options, {
              coords: coords.join(';')
            });
          }
        }
        $.ajax({
          url: this.urlRoot,
          data: this.options,
          dataType: 'jsonp',
          success: function(data, success, xhr) {
            var index, _k, _l, _len2, _len3, _ref1;
            if (data.status === 0) {
              if (cols.models) {
                _ref1 = cols.models;
                for (index = _k = 0, _len2 = _ref1.length; _k < _len2; index = ++_k) {
                  i = _ref1[index];
                  i.set('x', data.result[index].x);
                  i.set('y', data.result[index].y);
                }
              } else {
                for (index = _l = 0, _len3 = cols.length; _l < _len3; index = ++_l) {
                  i = cols[index];
                  i.x = data.result[index].x;
                  i.y = data.result[index].y;
                }
              }
              return defer.resolve(cols);
            } else {
              return defer.reject();
            }
          },
          error: function() {
            return defer.reject();
          }
        });
        return defer.promise();
      };

      return Convert;

    })();
    FollowMarker = (function(_super) {
      __extends(FollowMarker, _super);

      function FollowMarker(point, opt) {
        FollowMarker.__super__.constructor.call(this, point, opt || {});
        this._lab = new BMap.Label('左击添加标注', {
          offset: new BMap.Size(20, 0)
        });
        this.setLabel(this._lab);
        this.setZIndex(1000);
        this.hide();
      }

      return FollowMarker;

    })(BMap.Marker);
    return Map = (function() {
      function Map(element) {
        this.map = new BMap.Map(element);
        this.map.centerAndZoom(new BMap.Point(106.346, 33.866), 5);
        this.map.addControl(new BMap.NavigationControl());
        this.map.enableScrollWheelZoom();
        this.map.enableContinuousZoom();
        this.map.addEventListener('tilesloaded', this.removeCopy());
        this.geocoder = new BMap.Geocoder;
        this._markers = [];
        this._points = [];
      }

      Map.prototype.removeCopy = function() {
        var self;
        self = this;
        return function() {
          return $('.BMap_cpyCtrl', self.map.getContainer()).remove();
        };
      };

      Map.prototype.getIcon = function(style) {
        var l, offset, t, url, _ref;
        url = '/static/v/v1/images/ico-location-4.png';
        offset = {
          origin: [0, 0],
          dest: [-30, 0],
          orbit: [-60, 0],
          cur: [-90, 0]
        };
        _ref = offset[style], l = _ref[0], t = _ref[1];
        return new BMap.Icon(url, new BMap.Size(30, 37), {
          anchor: new BMap.Size(16, 36),
          imageOffset: new BMap.Size(l, t)
        });
      };

      Map.prototype.getLabel = function(index) {
        var content, label, offset;
        if (index <= 9) {
          offset = [11, 5];
        } else if (index >= 100) {
          offset = [4, 5];
        } else {
          offset = [8, 5];
        }
        content = "<b>" + index + "</b>";
        label = new BMap.Label(content, {
          offset: new BMap.Size(offset[0], offset[1])
        });
        label.setStyle({
          border: 'none',
          fontSize: '12px',
          backgroundColor: 'transparent',
          color: '#fff'
        });
        return label;
      };

      Map.prototype.getInfoWindow = function(name, time, location) {
        var content;
        content = "<b class='iw_poi_title'> " + name + " </b>\n<div class='iw_poi_content'> " + time + " 到达 " + location + " </div>";
        return new BMap.InfoWindow(content, {
          offset: new BMap.Size(0, -35)
        });
      };

      Map.prototype.showOrbits = function(cols, title) {
        var convert, self;
        self = this;
        convert = new Convert;
        return convert.fetch(cols).then(function(cols) {
          return self.render(cols, title);
        });
      };

      Map.prototype.render = function(cols, title) {
        var i, index, marker, model, point, _i, _j, _len, _len1, _ref, _ref1, _ref2;
        if (cols.models) {
          _ref = cols.models;
          for (i = _i = 0, _len = _ref.length; _i < _len; i = ++_i) {
            model = _ref[i];
            index = cols.models.length - i;
            _ref1 = this.addOrbit(model, title, index, cols.models.length), marker = _ref1[0], point = _ref1[1];
            this.map.addOverlay(marker);
            this._points.push(point);
            this._markers.push(marker);
          }
        } else {
          for (i = _j = 0, _len1 = cols.length; _j < _len1; i = ++_j) {
            model = cols[i];
            index = cols.length - i;
            _ref2 = this.addOrbit(model, title, index, cols.length), marker = _ref2[0], point = _ref2[1];
            this.map.addOverlay(marker);
            this._points.push(point);
            this._markers.push(marker);
          }
        }
        return this.setCenter();
      };

      Map.prototype.setCenter = function() {
        this.map.setViewport(this._points);
        this.map.setCenter(_.first(this._points));
        this.map.openInfoWindow(_.first(this._markers).infowindow, _.first(this._points));
        return this.map.setZoom(13);
      };

      Map.prototype.resetIndex = function() {
        return this.setZIndex(this.zindex);
      };

      Map.prototype.showInfoEvent = function() {
        return this.openInfoWindow(this.infowindow);
      };

      Map.prototype.showInfoByOrbit = function(index) {
        var marker, point;
        marker = this._markers[index];
        marker.openInfoWindow(marker.infowindow);
        marker.setZIndex(marker.zindex + 200);
        point = marker.getPosition();
        this.map.setCenter(point);
        return this.map.setZoom(13);
      };

      Map.prototype.addOrbit = function(model, title, index, length, style) {
        var icon, infowindow, label, marker, point, x, y, _ref, _ref1, _ref2;
        if (model.longitude && model.latitude) {
          _ref = [model.longitude, model.latitude], x = _ref[0], y = _ref[1];
        } else if (model.get('x') && model.get('y')) {
          _ref1 = [model.get('x'), model.get('y')], x = _ref1[0], y = _ref1[1];
        } else {
          _ref2 = [model.get('longitude'), model.get('latitude')], x = _ref2[0], y = _ref2[1];
        }
        point = new BMap.Point(x, y);
        if (model.locate_time && model.location) {
          infowindow = this.getInfoWindow(title, model.locate_time, model.location);
        } else {
          infowindow = this.getInfoWindow(title, model.get('locate_time'), model.get('location'));
        }
        icon = style ? this.getIcon(style) : this.getIcon(index < length ? 'orbit' : 'cur');
        label = index < length ? this.getLabel(index) : null;
        marker = new BMap.Marker(point);
        marker.enableMassClear();
        if (label) {
          marker.setLabel(label);
        }
        marker.setIcon(icon);
        marker.infowindow = infowindow;
        marker.zindex = index;
        marker.setZIndex(index);
        marker.addEventListener('click', this.showInfoEvent);
        marker.addEventListener('infowindowclose', this.resetIndex);
        return [marker, point];
      };

      Map.prototype.prependOrbit = function(model, title) {
        var first_marker, marker, point, _ref;
        first_marker = _.first(this._markers);
        if (first_marker) {
          first_marker.setIcon(this.getIcon('orbit'));
        }
        _ref = this.addOrbit(model, title, this._markers.length + 1, 'cur'), marker = _ref[0], point = _ref[1];
        this._points.unshift(point);
        return this._markers.unshift(marker);
      };

      Map.prototype.appendOrbits = function(newcols, title) {
        var i, icon, index, label, length, marker, _i, _len, _ref;
        this.map.clearOverlays();
        _ref = this._markers;
        for (i = _i = 0, _len = _ref.length; _i < _len; i = ++_i) {
          marker = _ref[i];
          length = newcols.models.length + this._markers.length;
          index = length - i;
          label = index < length ? this.getLabel(index) : null;
          icon = this.getIcon(index < length ? 'orbit' : 'cur');
          marker.setIcon(icon);
          marker.setLabel(label);
          marker.zindex = index;
          marker.setZIndex(index);
          this.map.addOverlay(marker);
        }
        return this.showOrbits(newcols, title);
      };

      Map.prototype.openMarkerTool = function(city) {
        var myCity, point, self;
        self = this;
        if (!this._marker) {
          this._marker = new BMap.Marker(this.map.getCenter());
          this._marker.hide();
          this.map.addOverlay(this._marker);
        }
        if (!this._followMarker) {
          this._followMarker = new FollowMarker(this.map.getCenter(), {
            offset: new BMap.Size(-10, -10)
          });
          this.map.addOverlay(this._followMarker);
          this.map.setDefaultCursor("url(" + CUR_IMG + "), default");
        }
        if (city) {
          if (typeof city === 'object') {
            point = city;
          }
          this.map.centerAndZoom(city, 12);
        } else {
          myCity = new BMap.LocalCity();
          myCity.get(function(result) {
            return self.map.centerAndZoom(result.name, 12);
          });
        }
        this.map.addEventListener('mousemove', function(event) {
          self._followMarker.setPosition(event.point);
          return self._followMarker.show();
        });
        this.map.addEventListener('click', function(event) {
          return self.getLocation(event.point, function(result) {
            var evtPix, iconPix;
            evtPix = event.pixel;
            iconPix = new BMap.Pixel(evtPix.x - 10, evtPix.y - 10);
            point = self.map.pixelToPoint(iconPix);
            return self.addEventPoint(point, result.address);
          });
        });
        if (point) {
          return self.getLocation(point, function(result) {
            return self.addEventPoint(point, result.address);
          });
        }
      };

      Map.prototype.addEventPoint = function(point, text) {
        var label, self;
        self = this;
        self._marker.setPosition(point);
        label = self._marker.getLabel();
        if (!label) {
          label = new BMap.Label(text, {
            offset: new BMap.Size(20, 0)
          });
          self._marker.setLabel(label);
        } else {
          label.setContent(text);
        }
        return self._marker.show();
      };

      Map.prototype.getLocation = function(point, callback) {
        return this.geocoder.getLocation(point, callback);
      };

      Map.prototype.addCityPoint = function(city, style, index) {
        var self;
        self = this;
        return this.geocoder.getPoint(city, function(point) {
          var icon, marker;
          icon = self.getIcon(style);
          marker = new BMap.Marker(point);
          marker.enableMassClear();
          marker.setIcon(icon);
          marker.zindex = index;
          marker.setZIndex(index);
          return self.map.addOverlay(marker);
        });
      };

      Map.prototype.getPoint = function(lat, lon) {
        return new BMap.Point(lon, lat);
      };

      return Map;

    })();
  });

}).call(this);
