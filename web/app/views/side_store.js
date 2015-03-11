(function() {
  var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; },
    __slice = [].slice;

  define(['backbone', 'store', 'modal', 'tagging', 'text!templates/modules/side_store.html'], function(Backbone, Store, Modal, Tagging, tmp_side_store) {
    'use strict';
    var SideStoreView;
    return SideStoreView = Backbone.View.extend({
      initialize: function(options) {
        if (options == null) {
          options = {};
        }
        this.options = options;
        this.key = options.key || 'mylist';
        this.key_len = options.key_len || 'myLen';
        this.len = Store.get(this.key_len) || 0;
        this.target = options.target;
        this.data = Store.get(this.key) || {};
        this.$count = options.$count;
        return this.render();
      },
      events: {
        'click #message_toggle': 'toggle',
        'click i.trash': 'delRow',
        'click a.send_msg': 'sendSMS',
        'click a.send_weixin': 'sendWX',
        'click a.add_notify': 'addNotify',
        'click a.store_clear': 'clearAll'
      },
      render: function() {
        this.$el.html(this.template(tmp_side_store, {
          data: this.data,
          options: this.options,
          len: this.len
        }));
        this.$body = $('tbody', this.$el);
        if (!_.isEmpty(this.data)) {
          this.show();
          return this.rebuildCheckbox();
        }
      },
      sendSMS: _.debounce(function(event) {
        var modal, self, user,
          _this = this;
        self = this;
        user = new UserModel(this._current_user());
        if (user.isGuest()) {
          this.notify().show("</p>您现在是以游客身份登录，没有相应的使用权限，请先<a href='#signup'>免费注册</a>为正式用户！</p>", 'warning');
          return;
        }
        if (user.isExpired()) {
          return modal = new Modal({
            title: "确认提醒",
            content: '<div class="modal-content">普通会员不能执行此操作，立即成为高级会员</div>',
            button: [
              {
                value: "立即开通",
                "class": "btn-small btn-confirm",
                callback: function() {
                  return _this.redirect("account/buy_packages");
                },
                autoremove: true
              }
            ]
          });
        } else {
          return this.redirect("service/send/sms?key=" + this.key + "&route=" + (encodeURIComponent(Backbone.history.fragment)));
        }
      }, 800, true),
      sendWX: _.debounce(function(event) {
        var modal, self, user,
          _this = this;
        self = this;
        user = new UserModel(this._current_user());
        if (user.isGuest()) {
          this.notify().show("</p>您现在是以游客身份登录，没有相应的使用权限，请先<a href='#signup'>免费注册</a>为正式用户！</p>", 'warning');
          return;
        }
        if (user.isExpired()) {
          return modal = new Modal({
            title: "确认提醒",
            content: '<div class="modal-content">普通会员不能执行此操作，立即成为高级会员</div>',
            button: [
              {
                value: "立即开通",
                "class": "btn-small btn-confirm",
                callback: function() {
                  return _this.redirect("account/buy_packages");
                },
                autoremove: true
              }
            ]
          });
        } else {
          return this.redirect("service/send/weixin?key=" + this.key + "&route=" + (encodeURIComponent(Backbone.history.fragment)));
        }
      }, 800, true),
      addNotify: _.debounce(function(event) {
        var modal, self, user,
          _this = this;
        self = this;
        user = new UserModel(this._current_user());
        if (user.isGuest()) {
          this.notify().show("</p>您现在是以游客身份登录，没有相应的使用权限，请先免费<a href='#signup'>注册</a>为正式用户！</p>", 'warning');
          return;
        }
        if (user.isExpired()) {
          modal = new Modal({
            title: "确认提醒",
            content: '<div class="modal-content">普通会员不能执行此操作，立即成为高级会员</div>',
            button: [
              {
                value: "立即开通",
                "class": "btn-small btn-confirm",
                callback: function() {
                  return _this.redirect("account/buy_packages");
                },
                autoremove: true
              }
            ]
          });
          return;
        }
        if (this.key === 'pv_list') {
          if (!(__indexOf.call(_.map(_.values(this.data), function(n) {
            return n[4];
          }), 1) >= 0)) {
            return modal = new Modal({
              title: "确认提醒",
              content: '<div class="modal-content">您所选择的车辆都未开通定位，为了保证您继续享用我们的位置服务，建议您立即开通车辆定位。</div>',
              button: [
                {
                  value: "立即开通",
                  "class": "btn-small btn-confirm",
                  callback: function() {
                    return self.redirect("cars?status=closed");
                  },
                  autoremove: true
                }
              ]
            });
          } else if (__indexOf.call(_.map(_.values(this.data), function(n) {
            return n[4];
          }), 0) >= 0) {
            return modal = new Modal({
              title: "确认提醒",
              content: '<div class="modal-content">您当前选择的车辆中有<span style="color:red">未开通定位</span>的车辆，如需继续使用此服务我们将会过滤未开通定位车辆。</div>',
              button: [
                {
                  value: "继续使用",
                  "class": "btn-small btn-confirm",
                  callback: function() {
                    var copy_data;
                    copy_data = {};
                    $.extend(copy_data, self.data);
                    _.each(_.values(copy_data), function(n, k) {
                      if (n[4] === 0) {
                        self.removeRow(_.keys(copy_data)[k]);
                        return self.rebuildCheckbox();
                      }
                    });
                    return self.redirect("service/notify?key=" + self.key);
                  },
                  autoremove: true
                }
              ]
            });
          } else {
            return this.redirect("service/notify?key=" + this.key);
          }
        } else {
          return this.redirect("service/notify?key=" + this.key);
        }
      }, 800, true),
      clearAll: function(event) {
        var $target, self;
        self = this;
        $target = $(event.target);
        return $('i.trash').each(function(index, item) {
          var $val;
          $val = $(item).data('id');
          self.removeRow($val);
          return self.rebuildCheckbox();
        });
      },
      delRow: function(event) {
        var $target;
        $target = $(event.target);
        $target.parents('tr').remove();
        this["delete"]($target.data('id'));
        return this.rebuildCheckbox();
      },
      load: function() {
        if (_.isEmpty(this.data)) {
          return this.hide();
        }
      },
      addRow: function() {
        var $row, args, id, self, tmp;
        id = arguments[0], args = 2 <= arguments.length ? __slice.call(arguments, 1) : [];
        if (this.inData(id)) {
          return;
        }
        self = this;
        tmp = "<%var lastArg = \"" + args[args.length - 1] + "\"%>\n<%if(lastArg =='pm_list'){%>\n<tr><td>" + args[0] + "</td><td class=\"pm_td\"><span>" + args[1] + "</span></td><td><i class=\"trash\" data-id=\"" + id + "\"></i></td></tr>\n<%}else{%>\n<tr><td>" + args[0] + "</td><td >" + args[1] + "</td><td><i class=\"trash\" data-id=\"" + id + "\"></i></td></tr>\n<%}%>";
        $row = this.template(tmp, {});
        this.$body.append($row);
        this.data[id] = args;
        this.len++;
        this.$count.html(this.len);
        Store.set(this.key, this.data);
        Store.set(this.key_len, this.len);
        return this.show();
      },
      removeRow: function(id) {
        var $target;
        $target = $("i.trash[data-id='" + id + "']", this.$el);
        $target.parents('tr').remove();
        return this["delete"](id);
      },
      show: function() {
        return $('#message_toggle, #message_block', this.$el).show();
      },
      hide: function() {
        return $('#message_toggle, #message_block', this.$el).hide();
      },
      toggle: function() {
        $('#message_block', this.$el).toggle();
        return $('#message_toggle', this.$el).toggleClass('unfold-message');
      },
      "delete": function(id) {
        delete this.data[id];
        this.len--;
        if (this.len < 0) {
          this.len = 0;
        }
        this.$count.html(this.len);
        this.load();
        Store.set(this.key, this.data);
        return Store.set(this.key_len, this.len);
      },
      inData: function(id) {
        return _.has(this.data, id);
      },
      rebuildCheckbox: function() {
        var self;
        self = this;
        if (this.target) {
          this.target.each(function() {
            var checked;
            checked = self.inData($(this).val()) ? true : false;
            return $(this).attr('checked', checked);
          });
        }
        return this.$count.html(this.len);
      },
      clear: function() {
        Store.remove(this.key);
        return Store.remove(this.key_len);
      }
    });
  });

}).call(this);
