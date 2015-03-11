(function() {
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  define(['backbone', 'store', 'modal', 'bmaps', 'city', 'cars_filter', 'text!templates/forms/super_search.html'], function(Backbone, Store, Modal, Map, city, carsFilter, tmp_adv_search) {
    'use strict';
    var CarsFilterView;
    return CarsFilterView = Backbone.View.extend({
      initialize: function(options, list) {
        var self;
        if (options == null) {
          options = {};
        }
        self = this;
        this.isPublic = options.isPublic;
        this.pageSize = Store.get('carsPagesize') || 20;
        this.options = {
          data: {
            page_size: this.pageSize
          }
        };
        if (this.subViews == null) {
          this.subViews = [];
        }
        this.list = list;
        return $('#J_advSearch').click(function() {
          return self.showModal.call(self);
        });
      },
      showModal: _.debounce(function() {
        var self;
        self = this;
        return this.modal = new Modal({
          title: "高级搜索",
          width: 715,
          content: $(this.template(tmp_adv_search, {
            isPublic: this.isPublic
          })),
          i_class: 'add',
          cb: function() {
            return self.renderCb.call(self);
          },
          button: [
            {
              value: "确定",
              "class": "btn btn-normal btn-xslarge",
              autoremove: true,
              callback: function() {
                return self.search.call(self);
              }
            }
          ]
        });
      }, 800, true),
      search: _.debounce(function(event) {
        var self;
        self = this;
        $('#J_qsearchvalue').val('');
        _.extend(this.options.data, this._arguments($('#car_filter').serialize()), {
          page_no: 1,
          page_size: this.options.data.page_size,
          q: ''
        });
        this.list.fetch(this.fetchOptions(this.options, true));
        this.query = Backbone.history.fragment.split('?')[1];
        return $('#to-public-cars').attr('href', "#public/cars?" + this.query);
      }, 800, true),
      switchPos: _.debounce(function(event) {
        var modal, self, tmp_city_list, tmp_city_select;
        self = this;
        tmp_city_select = "<div class=\"modal-city-select\" id=\"modal_city_list\">\n    <div class=\"city-select-body\"><div></div></div>\n    <div class=\"city-select-header\">\n        <ul>\n            <li class=\"hot active\">热门城市</li>\n            <% _.each(tabsel, function(city) { %>\n            <li data-char=\"<%= city[0] %>\"><%= city[1] %></li>\n            <% }); %>\n        </ul>\n    </div>\n</div>";
        tmp_city_list = "<% _.each(citys, function(city) { %>\n<a href=\"javascript:;\"><%= _.isArray(city) ? city[0] : city %></a>\n<% }); %>";
        return $.when(modal = new Modal({
          title: "选择待运货物位置",
          width: 700,
          content: "<div id=\"load_map\" class=\"map-api modal-map\"></div>",
          button: [
            {
              value: "切换城市",
              "class": "btn btn-normal btn-xslarge",
              style: "float: left;",
              callback: function() {
                var $citySelect, renderCityList, tabsel;
                tabsel = _.reject(_.pairs(city.cityTabSel), function(i) {
                  return i[0] === '热门城市';
                });
                $citySelect = $(self.template(tmp_city_select, {
                  tabsel: tabsel
                }));
                renderCityList = function(citys) {
                  $('.city-select-body div', $citySelect).html(self.template(tmp_city_list, {
                    citys: citys
                  }));
                  return $('.city-select-body div a', $citySelect).on('click', function(event) {
                    var _ref;
                    if ((_ref = self.mapInstance) != null) {
                      _ref.map.centerAndZoom($(this).text(), 12);
                    }
                    return $citySelect.remove();
                  });
                };
                $('.modal', modal.$modal).append($citySelect);
                renderCityList(city.hotCity);
                $('.city-select-header li', $citySelect).on('click', function(event) {
                  var citys, keys;
                  $(this).siblings().removeClass('active');
                  $(this).addClass('active');
                  if ($(this).hasClass('hot')) {
                    citys = city.hotCity;
                  } else {
                    keys = $(this).data('char').split(',');
                    citys = _.filter(_.pairs(city.allCity), function(i) {
                      var _ref;
                      return _ref = i[1][0][0], __indexOf.call(keys, _ref) >= 0;
                    });
                  }
                  return renderCityList(citys);
                });
                $('form', modal.$modal).on('click', function() {
                  return $citySelect.remove();
                });
                return self.lookTag();
              }
            }, {
              value: "确定",
              "class": "btn btn-normal btn-xslarge",
              callback: function() {
                var label, point, tag, _ref, _ref1;
                label = (_ref = self.mapInstance) != null ? (_ref1 = _ref._marker) != null ? _ref1.getLabel() : void 0 : void 0;
                if (label) {
                  if ($('.tag')) {
                    $('.tag').remove();
                  }
                  tag = $("<div class=\"tag filter-tag\">\n<span>货物位置：</span><a class=\"pushpin-switch\" href=\"javascript:;\"><em>" + label.content + "</em></a>\n<i class=\"filter-close\"></i>\n</div>");
                  $('#cg_location').val(label.content);
                  $('.filter-close', tag).on('click', function() {
                    return self.setElement.call(self, tag);
                  });
                  $('.filter-header .tag-group').append(tag);
                  point = self.mapInstance._marker.getPosition();
                  $('#lng').val(point.lng);
                  $('#lat').val(point.lat);
                  return self.lookTag();
                }
              },
              autoremove: true
            }
          ]
        })).then(function() {
          var lat, lon, point;
          self.mapInstance = new Map("load_map");
          lon = self.getArguments('lng');
          lat = self.getArguments('lat');
          point = lon && lat ? self.mapInstance.getPoint(lat, lon) : null;
          return self.mapInstance.openMarkerTool(point);
        });
      }, 800, true),
      renderCb: function() {
        var self, timeout;
        self = this;
        carsFilter();
        this.lookTag();
        this.cityList1 = city.cityListOn($('.input-location'), {
          dataHolder: $('#location'),
          icon: true,
          multi: true
        });
        this.cityList1 = city.cityListOn($('.input-destination'), {
          dataHolder: $('#destination'),
          icon: true,
          multi: true
        });
        return timeout = setTimeout(function() {
          $('.filter-close', $('.tag')).on('click', function() {
            return self.setElement.call(self, $(this).parent('.tag'));
          });
          return $('#switch_pos').click(function() {
            return self.switchPos.call(self);
          });
        }, 0);
      },
      setElement: function(tag) {
        tag.remove();
        $('#cg_location').val('');
        $('#lng').val('');
        $('#lat').val('');
        $('#switch_pos').show();
        return this.lookTag();
      },
      lookTag: function() {
        if (_.some($('.tag-group').children(), function(oEl) {
          return $(oEl).html();
        })) {
          return $('.J_filterHead').show();
        } else {
          return $('.J_filterHead').hide();
        }
      },
      remove: function() {
        _.invoke(this.subViews, 'remove');
        return this._super('remove');
      }
    });
  });

}).call(this);
