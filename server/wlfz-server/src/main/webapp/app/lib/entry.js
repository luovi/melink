(function() {
  define(['jquery', 'underscore', 'text!templates/pages/plateNumber_control.html'], function($, _, tmp_plateNumber_control) {
    var Modal;
    return Modal = (function() {
      Modal.prototype.defaults = {
        width: 450,
        height: 'auto',
        content: ""
      };

      function Modal(options) {
        if (options == null) {
          options = {};
        }
        options = $.extend({}, this.defaults, options);
        this.options = options;
        this.arrEl = [];
        this.getEl();
      }

      Modal.prototype.bindEvent = function(oIpt, oEl) {
        var oFather, self;
        self = this;
        oFather = self.options.father;
        oIpt.click(function() {
          return oEl.show();
        });
        $('.J_plate_close', oEl).click(function() {
          oEl.hide();
          return oFather.entryValid.call(oFather, oIpt);
        });
        $(document).click(function(event) {
          var oSrc;
          oSrc = event.target;
          if (!self.isChild(oSrc, oEl[0]) && oSrc !== oIpt[0] && oEl.css('display') === 'block') {
            oEl.hide();
            return oFather.entryValid.call(oFather, oIpt);
          }
        });
        return _.each($('a.word', oEl), function(oBtn, i) {
          return $(oBtn).click(function() {
            var str, val;
            str = $(this).html();
            val = oIpt.val();
            if (val.length >= self.options.maxlen) {
              return;
            }
            return oIpt.val(val + str);
          });
        });
      };

      Modal.prototype.isChild = function(obj1, obj2) {
        while (obj1) {
          if (obj1 === obj2) {
            return true;
          } else {
            obj1 = obj1.parentNode;
          }
        }
        return false;
      };

      Modal.prototype.create = function(oIpt) {
        var oEl;
        oEl = $(_.template(tmp_plateNumber_control, {
          sClass: this.options.sClass
        }));
        oEl.css({
          top: oIpt[0].offsetHeight
        });
        oEl[0].style[this.options.pos] = 0 + 'px';
        oIpt.parent().append(oEl);
        return this.bindEvent(oIpt, oEl);
      };

      Modal.prototype.hide = function() {};

      Modal.prototype.getEl = function() {
        var self;
        self = this;
        return _.each($(this.options.inputEl), function(oIpt) {
          return self.create($(oIpt));
        });
      };

      return Modal;

    })();
  });

}).call(this);
