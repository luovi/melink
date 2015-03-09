(function() {
  define(['backbone', 'store', 'modal', 'text!templates/modules/crumb.html', 'text!templates/forms/cargo_add.html'], function(Backbone, Store, Modal, tmp_crumb, tmp_cargo_add) {
    'use strict';
    var cargoAddView;
    return cargoAddView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      events: {
        'submit form': 'submit'
      },
      submit: _.debounce(function(event) {
        var $target, modal, self, tmp_1;
        self = this;
        this.dirty = false;
        event.preventDefault();
        $target = $(event.currentTarget);
        tmp_1 = "<div class=\"modal-content\">\n    <div class=\"group clearfix\">\n        <div class=\"group-y\">\n            <label class=\"label\">车牌号码：</label>\n            <div class=\"control-holder\">\n                <input type=\"text\" name=\"\" id=\"\" class=\"text input-slarge\">\n            </div>\n        </div>\n       \n    </div>\n    <div class=\"group clearfix\">\n        <div class=\"group-y\">\n            <label class=\"label\">随车手机：</label>\n            <div class=\"control-holder\">\n                <input type=\"text\" name=\"\" id=\"\" class=\"text input-slarge\">\n            </div>\n        </div>\n       \n    </div>\n    <p class=\"font_hint\">若已有车辆承运该单货，请输入车辆车牌号和司机手机号码如：浙A45321 ，13977852256</p>\n</div>";
        return modal = new Modal({
          title: "计划计划",
          width: 500,
          content: tmp_1,
          button: [
            {
              value: "确定",
              "class": "btn btn-xslarge btn-normal",
              callback: function() {},
              autoremove: true
            }
          ]
        });
      }, 800),
      render: function() {
        var $container, user;
        $container = $('<div class="container"></div>');
        user = Store.get('current_user').username;
        $container.append(this.template(tmp_crumb, {
          urls: [
            {
              url: '#',
              name: '货源信息'
            }
          ],
          current: '发布货源'
        }));
        $container.append(this.template(tmp_cargo_add, {
          user: user
        }));
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
