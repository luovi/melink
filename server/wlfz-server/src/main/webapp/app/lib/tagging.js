(function() {
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

  define(['jquery', 'underscore'], function($, _) {
    var Tagging;
    return Tagging = (function() {
      Tagging.prototype.defaults = {
        debug: true,
        placeholder: 'tags',
        tagBoxClass: 'tagging-group pull-left',
        tagBoxFocusClass: 'focus',
        tagwrapClass: 'tagging-wrapper',
        tagClass: 'tagging-item',
        closeClass: 'tagging-close',
        closeChar: '&times;',
        inputZoneClass: 'tagging-input-zone'
      };

      function Tagging(elem, settings) {
        var self;
        self = this;
        this.settings = _.extend(this.defaults, settings);
        this.$elem = elem;
        this.tags = [];
        this.$input_zone = $(document.createElement("input")).addClass(self.settings.inputZoneClass).attr("placeholder", self.settings.placeholder).attr("contenteditable", true).attr("type", 'text').attr('maxlength', 16).on('focus', function() {
          return self.$box.addClass(self.settings.tagBoxFocusClass);
        }).on('blur', function() {
          return self.$box.removeClass('focus');
        });
        this.$box = $(document.createElement("div")).addClass(self.settings.tagBoxClass).append(self.$input_zone).insertAfter(self.$elem).on('click', function() {
          return self.$input_zone.focus();
        });
        this.$input_zone.on('keydown', function(e) {
          var $last, actual_text, pressed_key, text;
          actual_text = self.val();
          pressed_key = e.which;
          if (__indexOf.call(self.keys.add, pressed_key) >= 0) {
            if (actual_text.length > 0) {
              self.add(actual_text);
            }
            self.input('');
            return e.preventDefault();
          } else if (__indexOf.call(self.keys.remove, pressed_key) >= 0 && actual_text.length === 0) {
            $last = self.tags.pop();
            if ($last != null) {
              text = $last.attr("data-v");
              self.remove($last);
            }
            if (text != null) {
              self.input(text);
            }
            return e.preventDefault();
          }
        });
        this.$button_add = $('<a href="javascript:;" class="btn-menu add-label" tabindex="-1"><i class="add-gray"></i>新增</a><span class="er error" style="display:none; margin-top:5px;"> 请输入标签内容</span>');
        this.$button_add.insertAfter(self.$box).on('click', function() {
          var text;
          text = self.$input_zone.val();
          if (text.length > 0) {
            self.$input_zone.val('');
            self.add(text);
            return $('.er').css('display', 'none');
          } else {
            return $('.er').css('display', 'block');
          }
        });
        if (this.settings.tagwrap) {
          this.$tagwrap = this.settings.tagwrap;
        } else {
          this.$tagwrap = $(document.createElement("div")).addClass(self.settings.tagwrapClass).insertAfter(self.$elem);
        }
      }

      Tagging.prototype.keys = {
        add: [188, 13, 32],
        remove: [46, 8]
      };

      Tagging.prototype.val = function() {
        return $.trim(this.$input_zone.val());
      };

      Tagging.prototype.input = function(text) {
        this.$input_zone.val(text);
        return this.$input_zone.trigger('change');
      };

      Tagging.prototype.add = function(text) {
        var $tag, self, values;
        self = this;
        values = self.getValues();
        if (__indexOf.call(values, text) >= 0 || text === '') {
          return;
        }
        $tag = $(document.createElement("div")).addClass(self.settings.tagClass).attr("data-v", text).html(text);
        $(document.createElement("i")).addClass(self.settings.closeClass).on('click', function() {
          return self.remove($tag, text);
        }).appendTo($tag);
        self.$tagwrap.append($tag);
        self.tags.push($tag);
        self.$elem.trigger('change');
        return self.save();
      };

      Tagging.prototype.remove = function($tag) {
        var self, tag;
        self = this;
        self.tags = (function() {
          var _i, _len, _ref, _results;
          _ref = self.tags;
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            tag = _ref[_i];
            if (tag !== $tag) {
              _results.push(tag);
            }
          }
          return _results;
        })();
        $tag.remove();
        self.$elem.trigger('change');
        return self.save();
      };

      Tagging.prototype.getValues = function() {
        var t;
        return (function() {
          var _i, _len, _ref, _results;
          _ref = this.tags;
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            t = _ref[_i];
            _results.push(t.attr("data-v"));
          }
          return _results;
        }).call(this);
      };

      Tagging.prototype.save = function() {
        var self, values;
        self = this;
        values = self.getValues();
        return self.$elem.val(values.toString());
      };

      Tagging.prototype.reload = function() {
        var self, val;
        self = this;
        val = this.$elem.val();
        if (val) {
          return _.each(val.split(','), function(text) {
            return self.add(text);
          });
        }
      };

      Tagging.prototype.release = function() {
        this.$tagwrap.remove();
        return this.$box.remove();
      };

      return Tagging;

    })();
  });

}).call(this);
