(function() {
  define(['backbone', 'store', 'modal', 'views/user_info', 'text!templates/modules/crumb.html', 'text!templates/modules/sidebar.html'], function(Backbone, Store, Modal, userInfoView, tmp_crumb, tmp_sidebar) {
    'use strict';
    var centerView;
    return centerView = Backbone.View.extend({
      initialize: function() {
        return this.render();
      },
      events: {
        'submit form': 'submit'
      },
      submit: _.debounce(function(event) {
        var $target, modal, self;
        self = this;
        this.dirty = false;
        event.preventDefault();
        $target = $(event.currentTarget);
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
        var $container;
        if (this.subViews == null) {
          this.subViews = [];
        }
        this.userInfo = new userInfoView;
        this.subViews.push(this.userInfo);
        $container = $('<div class="container"></div>');
        $container.append(this.template(tmp_crumb, {
          urls: [
            {
              url: '#',
              name: '用户中心'
            }
          ],
          current: '修改资料'
        })).append($('<div class="row content-box"><div class="span2"></div><div class="span8"></div></div>'));
        $('.row>.span2', $container).append(this.template(tmp_sidebar, {}));
        $('.row>.span8', $container).append(this.userInfo.el);
        return this.$el.html($container);
      },
      remove: function() {
        return this._super('remove');
      }
    });
  });

}).call(this);
