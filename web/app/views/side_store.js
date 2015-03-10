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
        this.dataId = this.arrData() || [];
        return this.render();
      },
      events: {
        'click #message_toggle': 'toggle',
        'click i.trash': 'delRow',
        'click a.send_msg': 'sendSMS',
        'click a.send_weixin': 'sendWX',
        'click a.edit_tag': 'editTag',
        'click a.add_notify': 'addNotify',
        'click a.store_clear': 'clearAll'
      },
      arrData: function() {
        return _.map(this.data, function(con, key) {
          return key;
        });
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
      editTag: function(event) {
        var $content, $tmp, bindClose, modal, self, tagging, timeout,
          _this = this;
        self = this;
        $content = $(this.template("\n<div id=\"form_edit_tag\" class=\"modal-content form-y\">\n    <% var len = _.size(data) %>\n    <div class=\"group\">\n        <%if(options.key == 'pm_list'){%>\n            <label class=\"label type\" data-type=\"<%=options.key%>\">所选用户：</label>\n            <% if(len>=6){ %>\n            <div class=\"tag-select\">当前已选择 <span class=\"select-number\"><%= _.size(data) %></span> 个用户\n                <span class=\"tag-unfold tag-toggle\">\n                        <a href=\"javascript:void(0);\">详情<i class=\"drop-down-gray\"></i></a>\n                </span>\n            </div>\n            <% }; %>\n\n        <%}else{%>\n            <label class=\"label\">所选车辆：</label>\n            <% if(len>=6){ %>\n            <div class=\"tag-select\">当前已选择 <span class=\"select-number\"><%= _.size(data) %></span> 辆车\n                <span class=\"tag-unfold tag-toggle\">\n                        <a href=\"javascript:void(0);\">详情<i class=\"drop-down-gray\"></i></a>\n                </span>\n            </div>\n            <% }; %>\n\n        <%}%>\n        <div class=\"controls select-cars <% if(len>=6){ %>hide<% }; %>\">\n\n        </div>\n    </div>\n    <div class=\"label-group group clearfix\" style=\"margin-left:91px;\"></div>\n    <div class=\"group clearfix\">\n        <label class=\"label\">标签：</label>\n        <div class=\"controls\">\n            <input type=\"hidden\" class=\"input-xlarge\" name=\"tags\" />\n            <div class=\"tags-suggest\"></div>\n        </div>\n    </div>\n</div>", {
          data: this.data,
          options: this.options
        }));
        $tmp = $(this.template("<% _.each(_.pairs(data), function(d){ %>\n<div class=\"filter-tag\"><span><%= d[1][0] %>：</span><em><%= d[1][1] %></em><i class=\"filter-close\" data-id=\"<%= d[0] %>\"></i></div>\n<% }); %>\n", {
          data: this.data
        }));
        timeout = setTimeout(function() {
          return $('.select-cars', $content).html($tmp);
        }, 0);
        tagging = new Tagging($('input[name=tags]', $content), {
          placeholder: '请输入标签, 空格或逗号结束',
          inputZoneClass: 'input-xlarge mr5 pull-left',
          tagwrap: $('.label-group', $content),
          tagClass: 'filter-tag car-label',
          closeClass: 'filter-close'
        });
        tagging.reload();
        modal = new Modal({
          title: "编辑标签",
          width: 700,
          content: $content,
          button: [
            {
              value: "确定",
              "class": "btn-small btn-confirm",
              callback: function(data) {
                var cids, tags;
                tags = _.reject(self._arguments(data)['tags'].split(','), function(n) {
                  return $.trim(n) === '';
                });
                cids = _.map(self.data, function(n, k) {
                  return k;
                });
                if (_.isEmpty(tags) || _.isEmpty(cids)) {
                  return;
                }
                if ($('.type').attr('data-type') === 'pm_list') {
                  return $.ajax({
                    url: "/api/partners/" + (self._current_user().id) + "/tags",
                    type: 'POST',
                    data: $.param({
                      tag: tags,
                      uid: cids
                    }, true),
                    dataType: 'json',
                    success: function() {
                      var msg;
                      msg = "<p>用户批量新增标签成功</p>";
                      return self.notify().show(msg, 'success');
                    },
                    error: function() {
                      var msg;
                      msg = "<p>用户批量新增标签失败，请稍后重试!</p>";
                      return self.notify().show(msg, 'error');
                    },
                    beforeSend: function(xhr) {
                      return xhr.setRequestHeader('Authorization', self._current_user().token);
                    }
                  });
                } else {
                  return $.ajax({
                    url: "/api/cars/" + (self._current_user().id) + "/tags",
                    type: 'POST',
                    data: $.param({
                      tag: tags,
                      cid: cids
                    }, true),
                    dataType: 'json',
                    success: function() {
                      var msg;
                      msg = "<p>车辆批量新增标签成功</p>";
                      return self.notify().show(msg, 'success');
                    },
                    error: function() {
                      var msg;
                      msg = "<p>车辆批量新增标签失败，请稍后重试!</p>";
                      return self.notify().show(msg, 'error');
                    },
                    beforeSend: function(xhr) {
                      return xhr.setRequestHeader('Authorization', self._current_user().token);
                    }
                  });
                }
              },
              autoremove: true
            }
          ]
        });
        if (_.size(this.data) < 6) {
          setTimeout(function() {
            return $('.filter-close', $content).on('click', bindClose);
          }, 0);
        }
        $('.tag-toggle', $content).on('click', function() {
          if ($('.select-cars', $content).hasClass('hide')) {
            $('.select-cars', $content).removeClass('hide');
            $('.filter-close', $content).on('click', bindClose);
            return $('.tag-toggle a', $content).html('收起<i class="drop-down-gray"></i>');
          } else {
            $('.select-cars').addClass('hide');
            return $('.tag-toggle a', $content).html('详情<i class="drop-down-gray"></i>');
          }
        });
        return bindClose = function(event) {
          var $target, id;
          self = _this;
          $target = $(event.target);
          id = $target.data('id');
          $target.parent().remove();
          self.removeRow(id);
          $('.select-number', $content).text(_.size(self.data));
          self.rebuildCheckbox();
          if (_.isEmpty(self.data)) {
            return modal.release();
          }
        };
      },
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
        $('.pm_length').html("当前选择:" + this.len + "用户");
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
        $('.pm_length').html("当前选择:" + this.len + "用户");
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
          return this.target.each(function() {
            var checked;
            checked = self.inData($(this).val()) ? true : false;
            return $(this).attr('checked', checked);
          });
        }
      },
      clear: function() {
        Store.remove(this.key);
        return Store.remove(this.key_len);
      }
    });
  });

}).call(this);
