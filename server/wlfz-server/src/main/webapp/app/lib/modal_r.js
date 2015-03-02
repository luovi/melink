(function() {
  define(['jquery', 'underscore'], function($, _) {
    var ModalR, tmp_modal;
    tmp_modal = "    \n<div class=\"modal_r\" style=\"right:-715px;display:none\">\n    <div class=\"modal-header <%= i_class%> \"><p class=\"title \"><label class=\"close\">×</label><i class=\"<%= i_class%>\"></i><%= title %></p></div>\n\n    <form class=\"form-y\" onsubmit=\"return false;\" id=\"form_send_msg\">\n        <div class=\"modal-body\" style=\"height:<%= height%>px\">\n           \n        </div>\n    \n     <div class=\"modal-footer\">\n        \n        </div>\n    </form>\n    <!--[if lte IE 6]>\n        <iframe style=\"position:absolute; visibility:inherit; top:0; left:0; width:100%; height:100%; z-index:-1; \" frameborder=\"0\"></iframe>   \n    <![endif]-->\n</div>\n\n    ";
    return ModalR = (function() {
      ModalR.prototype.defaults = {
        title: "侧滑弹出层",
        width: 500,
        height: 'auto',
        button: "确定",
        content: "",
        target: null,
        i_class: "",
        hasMask: true
      };

      function ModalR(options) {
        if (options == null) {
          options = {};
        }
        options = $.extend({}, this.defaults, options);
        options.height = this.countHeight();
        this.options = options;
        this.create();
      }

      ModalR.prototype.bindEvent = function() {
        var self;
        self = this;
        $('.btn-cancel, label.close', this.$modal).on('click', function() {
          return self.destroy();
        });
        _.each($('input[type=button]:not(.btn-cancel)', this.$modal), function(button, i) {
          return $(button).on('click', _.debounce(function(event) {
            var _ref, _ref1;
            event.stopPropagation();
            if ((_ref = self.options.button[i]) != null) {
              _ref.callback(self.$form.serialize(), event);
            }
            if ((_ref1 = self.options.button[i]) != null ? _ref1.autoremove : void 0) {
              return self.destroy();
            }
          }, 800, true));
        });
        return $(window).on('resize', function() {
          return $('.modal-body', self.$modal).height(self.countHeight() + 'px');
        });
      };

      ModalR.prototype.countHeight = function() {
        var h1, h2, h3, height;
        h1 = $('#header') ? $('#header').height() : 0;
        h2 = $('#footer') ? $('#footer').height() : 0;
        h3 = $('.nav-top') ? $('.nav-top').height() : 0;
        return height = $(window).height() - 96;
      };

      ModalR.prototype.create = function() {
        "<% if (button) { %>\n<% _.each(button, function(b) { %>\n<input class=\"<%= b.class %>\" style=\"<%= b.style %>\" type=\"button\" value=\"<%= b.value %>\" />\n<% }); %>\n<% }; %>\n<input class=\"btn-small btn-cancel\" type=\"button\" value=\"取消\" />";
        var footer, _base;
        if (this.options.hasMask && $('.modal-wrapper').length === 0) {
          this.$mask = $('<div class="modal-wrapper"></div>');
          $('body').append(this.$mask);
        }
        this.$modal = $(_.template(tmp_modal, this.options));
        footer = [];
        _.each(this.options.button, function(b) {
          return footer.push("<input class=\"" + b["class"] + "\" style=\"" + b.style + "\" type=\"button\" value=\"" + b.value + "\" />");
        });
        $('.modal-footer', this.$modal).html(footer.join('') + "<input class=\"btn-small btn-cancel\" type=\"button\" value=\"取消\" />");
        $('.modal-body', this.$modal).html(this.options.content);
        this.$form = $('form', this.$modal);
        $('body').append(this.$modal);
        this.bindEvent();
        this.show();
        return typeof (_base = this.options).cb === "function" ? _base.cb() : void 0;
      };

      ModalR.prototype.show = function() {
        this.$modal.show().stop().animate({
          right: "0px"
        }, {
          easing: 'easeOutQuart',
          duration: 500
        });
        return $(document.body).css('overflow', 'hidden');
      };

      ModalR.prototype.destroy = function() {
        var self;
        self = this;
        $(document.body).css('overflow', '');
        return this.$modal.stop().animate({
          right: "-715px"
        }, {
          easing: 'easeOutExpo',
          duration: 400,
          complete: function() {
            var _ref;
            self.$modal.remove();
            return (_ref = self.$mask) != null ? _ref.remove() : void 0;
          }
        });
      };

      return ModalR;

    })();
  });

}).call(this);
