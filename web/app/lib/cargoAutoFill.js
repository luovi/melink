(function() {
  define(['jquery', '_.str'], function($, str) {
    /*
    opts:
        target:
            input: ''
            icon: ''
        outTarget:
            titleEl: ''
            valueInput: ''
            descInput: ''
        
        incomeData: []
    */

    /*
        使用方法： new CargoAutoFill(opts)
    */

    var CargoAutoFill;
    return CargoAutoFill = (function() {
      CargoAutoFill.prototype.tmp_wrap = "<ul class=\"ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all\" \n     style=\"z-index: 2; position: absolute; display: none; width:190px\">\n</ul>";

      CargoAutoFill.prototype.tmp_list = " <% _.each(data, function(item){%>\n     <li class=\"ui-menu-item\">\n     <a class=\"ui-corner-all\" href=\"javascript:void(0)\">\n        <span class=\"value\"><%= item.company.company_name || item.contact_number|| \"\" %></span><br>\n        <span class=\"desc\"><%= item.contact_person || \"\" %></span><br>\n        <span class=\"num\" style=\"display:none\"><%= item.contact_number || \"\" %></span><br>\n    </a>\n</li>\n<%})%>";

      function CargoAutoFill(opts) {
        if (!opts || !_.isObject(opts)) {
          return;
        }
        this.opts = opts;
        this.data = opts.incomeData;
        this.filterData = this.data;
        this.render();
      }

      CargoAutoFill.prototype.render = function() {
        this.$ui = $(this.tmp_wrap);
        return this.iconEvent();
      };

      CargoAutoFill.prototype.setPosition = function() {
        var offset;
        offset = this.opts.target.input.offset();
        this.position = {
          'width': this.opts.target.input.width() + 10,
          'top': offset.top + this.opts.target.input.height() + 10,
          'left': offset.left
        };
        this.$ui.css({
          top: this.position.top,
          left: this.position.left,
          width: this.position.width
        });
        return $('body').append(this.$ui);
      };

      CargoAutoFill.prototype.iconEvent = function() {
        var self;
        self = this;
        this.opts.target.icon.on('click', function() {
          self.show();
          return self.alldata();
        });
        this.opts.target.input.on('keyup', function(e) {
          if (self.complate()) {
            self.show();
            return self.complatedata();
          } else {
            if ($(e.currentTarget).val().length !== 11) {
              self.outputreset();
              return self.removeEl();
            }
          }
        });
        return this.opts.target.input.on('blur', function(e) {
          if (self.complate()) {
            if ($('.ui-menu-item').first().length > 0) {
              self.chosefirt();
            }
            return self.removeEl();
          }
        });
      };

      CargoAutoFill.prototype.complate = function() {
        var self, text;
        self = this;
        text = this.opts.target.input.val();
        if (text.length > 0 && text.length <= 11) {
          return _.some(this.data, function(obj) {
            return _.some(obj, function(v) {
              return _.str.include(v, text);
            });
          });
        }
      };

      CargoAutoFill.prototype.chosefirt = function() {
        var desc, first, num, self, val;
        self = this;
        first = $('.ui-menu-item').first();
        val = $('.value', first).text();
        desc = $('.desc', first).text();
        num = $('.num', first).text();
        self.opts.target.input.val(num).change();
        self.opts.outTarget.titleEl.text(val).change();
        self.opts.outTarget.valueInput.val(val).change();
        return self.opts.outTarget.descInput.val(desc).change();
      };

      CargoAutoFill.prototype.chose = function() {
        var self;
        self = this;
        return _.each($('li a', this.$ui), function(a, i) {
          return $(a).on('click', function(e) {
            var desc, num, val;
            val = $('.value', $(e.currentTarget)).text();
            desc = $('.desc', $(e.currentTarget)).text();
            num = $('.num', $(e.currentTarget)).text();
            self.opts.target.input.val(num).change();
            self.opts.outTarget.titleEl.text(val).change();
            self.opts.outTarget.valueInput.val(val).change();
            self.opts.outTarget.descInput.val(desc).change();
            return self.removeEl();
          });
        });
      };

      CargoAutoFill.prototype.complatedata = function() {
        var self, text;
        self = this;
        text = this.opts.target.input.val();
        this.filterData = [];
        _.each(this.data, function(list, index) {
          var valid;
          valid = _.some(list, function(v) {
            return _.str.include(v, text);
          });
          if (valid) {
            return self.filterData.push(self.data[index]);
          }
        });
        $('.ui-autocomplete', $('body')).html($(_.template(this.tmp_list, {
          data: this.filterData
        })));
        return this.chose();
      };

      CargoAutoFill.prototype.alldata = function() {
        if ($('.ui-autocomplete', $('body')).length === 1) {
          $('.ui-autocomplete', $('body')).html($(_.template(this.tmp_list, {
            data: this.filterData
          })));
          return this.chose();
        }
      };

      CargoAutoFill.prototype.show = function() {
        var $target;
        $target = this.$ui;
        if (this.$ui.hasClass('active' && !this.complate())) {
          this.$ui.css('display', 'none');
          this.$ui.removeClass('active');
          return this.removeEl();
        } else {
          this.$ui.css('display', 'block');
          this.$ui.addClass('active');
          return this.setPosition();
        }
      };

      CargoAutoFill.prototype.removeEl = function() {
        $('.ui-autocomplete', $('body')).remove();
        this.$ui.css('display', 'none');
        return this.$ui.removeClass('active');
      };

      CargoAutoFill.prototype.outputreset = function() {
        this.opts.outTarget.titleEl.text('信息不全,点击这里完善');
        this.opts.outTarget.valueInput.val('');
        return this.opts.outTarget.descInput.val('');
      };

      CargoAutoFill.prototype.output = function(e) {
        var $target;
        $target = $(e.currentTarget);
        this.opts.outTarget.titleEl.text($('.value', $target).text()).change();
        this.opts.outTarget.valueInput.val($('.value', $target).text()).change();
        return this.opts.outTarget.descInput.val($('.desc', $target).text()).change();
      };

      return CargoAutoFill;

    })();
  });

}).call(this);
