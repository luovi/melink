(function() {
  define(['jquery', 'underscore'], function($, _) {
    var Modal, tmp_modal;
    tmp_modal = "\n<div class=\"modal-wrapper\">\n    <div class=\"modal\" style=\"width:<%= width%>px;margin-left:-<%= width/2 %>px;\">\n        <div class=\"modal-header\">\n            <i class=\"close\" ></i>\n            <h2><%= title %></h2>\n        </div>\n        <form onsubmit=\"return false;\" style=\"padding:0;\">\n        <div class=\"modal-body\" style=\"max-height:400px;_height:<%= height %>px;\"></div>\n        <div class=\"modal-footer\"></div>\n        </form>\n    </div>\n    <!--[if lte IE 6]>\n        <iframe style=\"position:absolute; visibility:inherit; top:0; left:0; width:100%; height:100%; z-index:-1; \" frameborder=\"0\"></iframe>   \n    <![endif]-->\n    \n</div>\n";
    return Modal = (function() {
      Modal.prototype.defaults = {
        title: "弹出层",
        width: 500,
        height: 'auto',
        button: "确定",
        content: "",
        target: null
      };

      function Modal(options) {
        if (options == null) {
          options = {};
        }
        options = $.extend({}, this.defaults, options);
        this.options = options;
        this.create();
      }

      Modal.prototype.bindEvent = function() {
        var self;
        self = this;
        $('.btn-cancel, i.close', this.$modal).on('click', function() {
          return self.release();
        });
        return _.each($('input[type=button]:not(.btn-cancel)', this.$modal), function(button, i) {
          return $(button).on('click', _.debounce(function(event) {
            var _ref, _ref1;
            event.stopPropagation();
            if ((_ref = self.options.button[i]) != null) {
              _ref.callback(self.$form.serialize());
            }
            if ((_ref1 = self.options.button[i]) != null ? _ref1.autoremove : void 0) {
              return self.release();
            }
          }, 800, true));
        });
      };

      Modal.prototype.create = function() {
        "<% if (button) { %>\n<% _.each(button, function(b) { %>\n<input class=\"<%= b.class %>\" style=\"<%= b.style %>\" type=\"button\" value=\"<%= b.value %>\" />\n<% }); %>\n<% }; %>\n<input class=\"btn-small btn-cancel\" type=\"button\" value=\"取消\" />";
        var footer;
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
        return $(document.body).css('overflow', 'hidden');
      };

      Modal.prototype.release = function() {
        var _base;
        this.$modal.remove();
        if (typeof (_base = this.options).close === "function") {
          _base.close();
        }
        return $(document.body).css('overflow', '');
      };

      return Modal;

    })();
  });

}).call(this);
