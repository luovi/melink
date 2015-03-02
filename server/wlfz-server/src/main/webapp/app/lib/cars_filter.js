(function() {
  define(['jquery', 'underscore'], function($, _) {
    var filterIpt, filterLink, init, lookTag, updateTag;
    lookTag = function() {
      if (_.some($('.tag-group').children(), function(oEl) {
        return $(oEl).html();
      })) {
        return $('.J_filterHead').show();
      } else {
        return $('.J_filterHead').hide();
      }
    };
    updateTag = function(obj, content, clear) {
      if (obj.oTag.html()) {
        obj.oTag.find('em').text(content);
      } else {
        $('<div>').addClass('filter-tag').append($('<span>').text(obj.title), $('<em>').text(obj.text), $('<i>').addClass('filter-close').on('click', function() {
          return clear && clear(obj);
        })).appendTo(obj.oTag);
      }
      return lookTag();
    };
    filterLink = (function() {
      filterLink.prototype.defaults = {
        title: {
          wheel: '挂轮挂轴：',
          cart: '货箱结构：',
          spec: '特殊功能：'
        }
      };

      function filterLink(options) {
        if (options == null) {
          options = {};
        }
        options = $.extend({}, this.defaults, options);
        this.options = options;
        this.bindEv();
        this.backfill();
      }

      filterLink.prototype.bindEv = function() {
        var self;
        self = this;
        $('.filter-body .more').click(function() {
          return $('.filter-hidden', $(this).siblings()).toggle();
        });
        return $('.filter-link a').click(function() {
          var obj, type, _this;
          _this = $(this);
          _this.toggleClass('current');
          type = _this.data('type');
          obj = self.getLink(type, _this);
          self.linkClick(obj);
          if (obj.oHidden.val()) {
            return updateTag(obj, obj.oHidden.attr('data-content').replace(/\u002f/g, '，'), self.clearLink);
          } else {
            obj.oTag.empty();
            return lookTag();
          }
        });
      };

      filterLink.prototype.backfill = function() {
        var self;
        self = this;
        return _.each($('.CONVERT_CAR_DATA'), function(oHidden) {
          var $oHidden, aLinks, arr, idArr, type;
          $oHidden = $(oHidden);
          if ($oHidden.val()) {
            type = $(oHidden).attr('name');
            idArr = $(oHidden).val().split(',');
            aLinks = $('.filter-link a[data-type=' + type + ']');
            arr = [];
            _.each(aLinks, function(aLink, i) {
              var $aLink, id;
              $aLink = $(aLink);
              id = $aLink.data(type);
              if (_.indexOf(idArr, id) !== -1) {
                $aLink.addClass('current');
                return arr.push($aLink.html());
              }
            });
            if (arr.length) {
              $oHidden.attr('data-content', arr.join(','));
              return updateTag({
                title: self.options.title[type],
                text: $oHidden.attr('data-content').replace(/\u002f/g, '，'),
                oTag: $('.tag-holder-' + type),
                type: type,
                oHidden: $oHidden
              }, null, self.clearLink);
            }
          }
        });
      };

      filterLink.prototype.getLink = function(type, _this) {
        var obj;
        return obj = {
          el: _this,
          type: type,
          id: _this.data(type),
          text: _this.text(),
          oHidden: $('#' + type),
          oTag: $('.tag-holder-' + type),
          title: this.options.title[type]
        };
      };

      filterLink.prototype.clearLink = function(obj) {
        obj.oHidden.val('');
        obj.oHidden.attr('data-content', '');
        obj.oTag.empty();
        $('.filter-link a[data-type=' + obj.type + ']').removeClass('current');
        return lookTag();
      };

      filterLink.prototype.linkClick = function(obj) {
        var arr, arr1, dup, icontent, value;
        value = obj.oHidden.val();
        icontent = obj.oHidden.attr('data-content');
        dup = false;
        if (!value) {
          obj.oHidden.val(obj.id);
          return obj.oHidden.attr('data-content', obj.text);
        } else {
          arr = value.split(',');
          arr1 = obj.oHidden.attr('data-content').split(',');
          _.each(arr, function(item, i) {
            if (item === obj.id) {
              arr.splice(i, 1);
              obj.oHidden.val(arr.join(','));
              dup = true;
            }
          });
          _.each(arr1, function(item, i) {
            if (item === obj.text) {
              arr1.splice(i, 1);
              obj.oHidden.attr('data-content', arr1.join(','));
              dup = true;
            }
          });
          if (!dup) {
            obj.oHidden.val(value + ',' + obj.id);
            return obj.oHidden.attr('data-content', icontent + ',' + obj.text);
          }
        }
      };

      return filterLink;

    })();
    filterIpt = (function() {
      filterIpt.prototype.defaults = {
        title: {
          filter_plate: '车牌号码：',
          filter_mobile: '随车电话：',
          filter_name: '司机姓名：',
          location: '发货城市：',
          destination: '到货城市：',
          carlen: '箱体长度：',
          load: '载重吨位：'
        },
        unit: {
          carlen: '米',
          load: '吨'
        }
      };

      function filterIpt(options) {
        if (options == null) {
          options = {};
        }
        options = $.extend({}, this.defaults, options);
        this.options = options;
        this.bindEv();
        this.backfill();
      }

      filterIpt.prototype.backfill = function() {
        _.each($('.J_filter_ipt'), function(item, i) {
          return $(item).keyup();
        });
        _.each($('.J_filter_count'), function(item, i) {
          return $(item).keyup();
        });
        $('#location').change();
        return $('#destination').change();
      };

      filterIpt.prototype.bindEv = function() {
        var self;
        self = this;
        $('.J_filter_ipt').on('keyup', function() {
          return self.setInfo.call(self, $(this).attr('id'), $(this));
        });
        $('#location').on('change', function() {
          return self.setInfo.call(self, 'location', $('#location'));
        });
        $('#destination').on('change', function() {
          return self.setInfo.call(self, 'destination', $('#destination'));
        });
        return $('.J_filter_count').on('keyup', function() {
          return self.setCount.call(self, $(this));
        });
      };

      filterIpt.prototype.setCount = function(_this) {
        var aIpt, oMsg, obj, text, type, unit, val1, val2;
        type = _this.data('type');
        aIpt = $('.J_' + type);
        oMsg = $('.help-msg', _this.parent());
        val1 = aIpt.eq(0).val();
        val2 = aIpt.eq(1).val();
        oMsg.hide();
        if (this.checkValid(val1, val2)) {
          text = '';
          unit = this.options.unit[type];
          if (val1 === '') {
            text = val2 + unit + '以下';
          } else if (val2 === '') {
            text = val1 + unit + '以上';
          } else {
            text = val1 + '-' + val2 + unit;
          }
          obj = this.getIpt(type, _this, text);
          if (val1 === '' && val2 === '') {
            this.clearUnit(obj, type);
            return;
          }
          return updateTag(obj, text, this.clearUnit);
        } else {
          return oMsg.show();
        }
      };

      filterIpt.prototype.setInfo = function(type, _this) {
        var obj;
        obj = this.getIpt(type, _this);
        if (obj.text) {
          return updateTag(obj, obj.text, this.clearIpt);
        } else {
          obj.oTag.empty();
          return lookTag();
        }
      };

      filterIpt.prototype.clearUnit = function(obj) {
        $('.J_' + obj.type).val('');
        obj.oTag.empty();
        return lookTag();
      };

      filterIpt.prototype.clearIpt = function(obj) {
        obj.el.val('');
        obj.oTag.empty();
        return lookTag();
      };

      filterIpt.prototype.getIpt = function(type, _this, text) {
        var obj;
        return obj = {
          el: _this,
          type: type,
          text: text || _this.val(),
          oTag: $('.tag-holder-' + type),
          title: this.options.title[type]
        };
      };

      filterIpt.prototype.checkValid = function(min, max) {
        var b, pass;
        b = (min === '' && max === '') || (this.isFloat(min) && max === '') || (this.isFloat(max) && min === '') || (this.isFloat(min) && this.isFloat(max) && parseFloat(min) < parseFloat(max));
        return pass = b ? true : false;
      };

      filterIpt.prototype.isFloat = function(data) {
        var result;
        return result = data.match(/^\+{0,1}\d+(\.\d{1,2})?$/) ? true : false;
      };

      return filterIpt;

    })();
    return init = function() {
      new filterLink;
      return new filterIpt;
    };
  });

}).call(this);
